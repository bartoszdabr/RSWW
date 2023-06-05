import os

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait

DRIVER_PATH = os.getenv("CHROME_DRIVER_PATH")
URL = os.getenv("SERVICE_URL", "http://localhost:8080")


class RswwTest:

    def __init__(self, driver: webdriver.Chrome, url: str):
        self.driver = driver
        self.url = url

    def test(self):
        self.driver.get(self.url)
        self.login()
        self.filter_offers(destination='Grecja', departure='Katowice',
                           departureDate='05/31/2023', endDate='06/30/2023',
                           adults=2, childrenUnder3=2, childrenUnder10=0, childrenUnder18=0)
        self.select_offer()
        self.order_offer()
        self.wait_for_purchase()
        self.driver.close()

    def login(self):
        self.driver.find_element(by=By.ID, value='username').send_keys('aaa')
        self.driver.find_element(by=By.ID, value='password').send_keys('123')
        self.driver.find_element(By.XPATH, value='//button[text()="Login"]').click()

    def filter_offers(self, destination, departure, departureDate, endDate,
                      adults, childrenUnder3, childrenUnder10, childrenUnder18):
        self.driver.find_element(By.XPATH, value='//input[@placeholder="Enter destination"]').send_keys(destination)
        self.driver.find_element(By.XPATH, value='//input[@placeholder="Enter place of departure"]').send_keys(departure)
        dates = self.driver.find_elements(By.XPATH, value='//input[@type="date"]')
        dates[0].send_keys(departureDate)
        dates[1].send_keys(endDate)
        ages = self.driver.find_elements(By.XPATH, value='//input[@type="number"]')
        ages[0].send_keys(adults)
        ages[1].send_keys(childrenUnder3)
        ages[2].send_keys(childrenUnder10)
        ages[3].send_keys(childrenUnder18)
        self.driver.find_element(By.XPATH, value='//button[text()="Submit"]').click()

    def select_offer(self):
        self.driver.implicitly_wait(2)
        self.driver.find_element(by=By.TAG_NAME, value='td').click()

    def order_offer(self):
        self.driver.find_element(By.XPATH, value='//button[text()="Order Offer"]').click()

    def wait_for_purchase(self):
        wait = WebDriverWait(driver=self.driver, timeout=65)
        wait.until(expected_conditions.any_of(
            expected_conditions.text_to_be_present_in_element((By.TAG_NAME, 'H1'), 'Happy vacations. Offer was sucesfully purchased.'),
            expected_conditions.text_to_be_present_in_element((By.TAG_NAME, 'H!'), 'Unfortunately given offer could not be booked.')
        ))


def create_chrome_driver() -> webdriver.Chrome:
    options = webdriver.ChromeOptions()
    options.add_argument('start-maximized')
    return webdriver.Chrome(service=Service(DRIVER_PATH), options=options)


if __name__ == '__main__':
    driver = create_chrome_driver()
    RswwTest(driver, URL).test()
