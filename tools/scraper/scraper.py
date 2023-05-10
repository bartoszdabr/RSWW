import dataclasses
import itertools
import logging
import json
import os.path
import random
import time
import urllib
import re
import requests

from pathlib import Path
from http import HTTPStatus
from typing import List, Dict, Tuple, Optional

from bs4 import BeautifulSoup, Tag
from requests import Response
from retrying import retry

from trip_model import TripInfo, FlightInfo, ScrapData

log = logging.getLogger()

ROOT_URL = 'https://itaka.pl'
SUBPAGES = [
    '/all-inclusive/?view=offerList&package-type=wczasy&adults=2&date-from=2023-05-25&food=allInclusive&order=popular&total-price=0&currency=PLN&page=20',
    '/last-minute/?view=offerList&package-type=wczasy&adults=2&date-from=2023-05-25&promo=lastMinute&order=popular&total-price=0&transport=flight&currency=PLN&page=20'
]
SCRAPER_PARSER = 'html.parser'
DESTINATION_DIRECTORY = os.getenv('DESTINATION_DIRECTORY', str(Path.home()))
DESTINATION_FILE_NAME = os.getenv('DESTINATION_FILE_NAME', 'data_with_names.json')

dates_regex = re.compile(r'[0-9]{2}\.[0-9]{2}')


@retry(stop_max_attempt_number=5, wait_random_min=1000, wait_random_max=2000)
def call_url(url: str) -> Response:
    """Call url and check if succeeded."""
    log.info('Calling %s', url)
    resp = requests.get(url=url, timeout=15)
    if resp.status_code != HTTPStatus.OK:
        log.error('Calling %s failed with error code %i', url, resp.status_code)
        raise Exception('Could not connect to webpage %s', url)
    log.info('Call successfull, status code: %i', resp.status_code)
    return resp


def get_single_offer_url(offer: Tag) -> str:
    """Get single offer url from extracted tag."""
    return offer.find_all('a', href=True, title=True)[0].attrs.get('href')


def get_start_end_dates(content: BeautifulSoup) -> Tuple[str, str]:
    """Extract trip start date from BeautifulSoup object."""
    dates_title = content.find('a', class_='dropdown-toggle btn', title=True).attrs.get('title')
    return tuple(dates_regex.findall(dates_title))


def get_cost(content: BeautifulSoup) -> int:
    """Extract trip cost from BeautifulSoup object."""
    return int(content.find('strong', title=True, attrs={'data-js-value': 'offerPrice'}).string.replace('\xa0', ''))  # always second char is \xa0, needs to be skipped


def get_destination(content: BeautifulSoup) -> str:
    """Extract destination from BeautifulSoup object."""
    return content.find('span', class_='destination-title destination-country-region').text


def get_tags(content: BeautifulSoup) -> List[str]:
    """Extract tags from BeautifulSoup object."""
    tag_list = content.find('td', class_='event-assets hidden-xs').find_all('li')
    return [tag.text.lower() for tag in tag_list]


def get_score(content: BeautifulSoup) -> float:
    """Extract score from BeautifulSoup object."""
    try:
        return float(content.find('div', class_='event-opinion-flag', title=True).find('strong').text)
    except Exception:
        return round(random.uniform(1.0, 6.0), 1)


def get_stars(content: BeautifulSoup) -> float:
    """Extract stars from BeautifulSoup object."""
    try:
        stars_not_formatted = content.find('span', title='subiektywna kategoryzacja B.P. ITAKA*') \
            .attrs.get('class')[1].removeprefix('star')
        stars_formatted = f'{stars_not_formatted[0]}.{stars_not_formatted[1]}'  # stars information does not contain dot sign
        return float(stars_formatted)
    except Exception:
        return round(random.uniform(1, 5), 1)


def get_images(content: BeautifulSoup) -> List[str]:
    """Extract image urls from BeautifulSoup object."""
    images_list_tags = content.find('div', id='slider', class_='flexslider flex-slider').find_all('li')
    images_links = [tag.find('a').attrs.get('data-href') for tag in images_list_tags if tag.find('a') is not None]
    return images_links


def get_description(content: BeautifulSoup) -> Dict[str, str]:
    """Extract description divided by sections from BeautifulSoup object."""
    description_panel_tags = content.find('div', id='product-tab-productdescription').find_all('p')
    description_tags_divided_by_sections = [strong_tag.previous_element for tag in description_panel_tags if (strong_tag := tag.find('strong'))]
    description_tuples = [tag.text.split(sep=':', maxsplit=1) for tag in description_tags_divided_by_sections]
    trip_description = {description_title: description_content for description_title, description_content in description_tuples}
    return trip_description


def get_start_location(content: BeautifulSoup) -> str:
    """Extract start location."""
    return content.find('a', class_='dropdown-toggle btn', attrs={'data-js-value': 'departure-selected'}).text.strip()


def get_hotel_name(content: BeautifulSoup) -> str:
    """Extract hotel name."""
    return content.find('span', class_='productName-holder').find('h1').text


def extract_offer(offer_url: str) -> Optional[TripInfo]:
    """Call offer url and prepare dataclass"""
    try:
        resp = call_url(offer_url)
        content = BeautifulSoup(resp.content, features=SCRAPER_PARSER)
        start_date, end_date = get_start_end_dates(content)
        return TripInfo(
            start_date=start_date,
            end_date=end_date,
            cost=get_cost(content),
            start_location=get_start_location(content),
            destination=get_destination(content),
            description=get_description(content),
            tags=get_tags(content),
            opinion_score=get_score(content),
            stars=get_stars(content),
            images=get_images(content),
            hotel_name=get_hotel_name(content)
        )
    except Exception:
        return None


def extract_offers_from_subpage(resp: Response, home_page: str) -> List[TripInfo]:
    """Get offers from web response."""
    soup = BeautifulSoup(resp.content, features=SCRAPER_PARSER)
    offer_list = soup.find_all('article', class_='offer clearfix')
    offer_urls = [urllib.parse.urljoin(home_page, get_single_offer_url(offer)) for offer in offer_list]
    offers = [offer for offer_url in offer_urls if (offer := extract_offer(offer_url))]

    return offers


def get_subpage_name(subpage: str) -> str:
    """Extract subpage name from uri."""
    return subpage.split('/')[1]


def get_trip_offers(home_page: str, subpages: List[str]) -> Dict[str, List[TripInfo]]:
    """Prepare trip offers by calling provided urls."""
    trip_offers = {}
    for subpage in subpages:
        url = urllib.parse.urljoin(home_page, subpage)
        resp = call_url(url)
        subpage_name = get_subpage_name(subpage)
        trip_offers[subpage_name] = extract_offers_from_subpage(resp, home_page)

    return trip_offers


def save_data(trip_offers: ScrapData) -> None:
    """Save scrapper offers to json file"""
    save_directory = DESTINATION_DIRECTORY
    json_file_name = DESTINATION_FILE_NAME
    save_path = os.path.join(save_directory, json_file_name)
    with open(save_path, 'w') as f:
        json.dump(trip_offers, f, default=lambda o: dataclasses.asdict(o), indent=4)
    log.info('Data saved to %s.', save_path)


def generate_flights(trip_offers: Dict[str, List[TripInfo]]) -> List[FlightInfo]:
    """Generate flights data from scrapped trips."""
    return [FlightInfo(start_location=trip.start_location, end_location=trip.destination.split('/')[0],
                       departure=trip.start_date, seats=random.randint(0, 200))
            for trip in list(itertools.chain(*trip_offers.values()))]


def get_scrap_data() -> ScrapData:
    """Prepare and return scrap dataclass."""
    start_time = time.time()
    trip_offers = get_trip_offers(ROOT_URL, SUBPAGES)
    end_time = time.time() - start_time
    log.info('Ended scrapping website %s', ROOT_URL)
    log.info('Scrapping took %f s', end_time)
    flights = generate_flights(trip_offers)
    log.info('Generated flights data.')
    return ScrapData(flights=flights, trip_offers=trip_offers)


def web_scrap() -> None:
    """Start crawling website."""
    data = get_scrap_data()
    save_data(data)
