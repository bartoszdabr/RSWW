package reservation.reservationreadservice.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reservation.reservationreadservice.models.HotelOfferModel;
import reservation.reservationreadservice.models.HotelResponseModel;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Profile("mock")
@Repository
public class HotelRepositoryMockImpl implements HotelRepository {
    @Override
    public Optional<HotelResponseModel> findHotels(Optional<LocalDate> startDate, Optional<LocalDate> endDate,
                                                   Optional<Integer> numOfPeople, Optional<String> destinationLocation) {
        return Optional.of(HotelResponseModel.builder()
                .availableHotels(Collections.singletonList(HotelOfferModel.builder()
                        .hotelId("123")
                        .name("mockHotel")
                        .rating(5.0)
                        .stars(5.0)
                        .location(destinationLocation.get())
                        .startDate(startDate.get())
                        .endDate(endDate.get())
                        .numOfPeople(numOfPeople.get())
                        .build())
                ).build());
    }
}
