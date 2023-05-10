from dataclasses import dataclass
from typing import List, Dict


@dataclass
class TripInfo:
    """Single trip information."""
    start_date: str
    end_date: str
    cost: float
    start_location: str
    destination: str
    description: Dict[str, str]
    tags: List[str]
    opinion_score: float
    stars: float
    images: List[str]
    hotel_name: str


@dataclass
class FlightInfo:
    """Single flight information."""
    start_location: str
    end_location: str
    departure: str
    seats: int


@dataclass
class ScrapData:
    """Web scrap result data format."""
    flights: List[FlightInfo]
    trip_offers: Dict[str, List[TripInfo]]
