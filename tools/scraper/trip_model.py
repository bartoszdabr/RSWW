from dataclasses import dataclass
from typing import List, Dict


@dataclass
class TripInfo:
    """Single trip information."""
    start_date: str
    end_date: str
    cost: float
    destination: str
    description: Dict[str, str]
    tags: List[str]
    opinion_score: float
    stars: float
    images: List[str]
