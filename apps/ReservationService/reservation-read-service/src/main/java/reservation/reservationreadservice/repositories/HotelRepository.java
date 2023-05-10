package reservation.reservationreadservice.repositories;

import reservation.reservationreadservice.models.HotelResponseModel;

import java.time.LocalDate;
import java.util.Optional;

public interface HotelRepository {
    Optional<HotelResponseModel> findHotels(
            Optional<LocalDate> startDate,
            Optional<LocalDate> endDate,
            Optional<Integer> numOfPeople);
}
