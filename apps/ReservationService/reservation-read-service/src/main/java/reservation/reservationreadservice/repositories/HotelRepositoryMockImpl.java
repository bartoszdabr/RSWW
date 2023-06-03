package reservation.reservationreadservice.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reservation.reservationreadservice.models.HotelOfferModel;
import reservation.reservationreadservice.models.HotelResponseModel;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Profile("mock")
@Repository
public class HotelRepositoryMockImpl implements HotelRepository {
    @Override
    public Optional<HotelResponseModel> findHotels(Optional<LocalDate> startDate, Optional<LocalDate> endDate,
                                                   Optional<Integer> numOfPeople, Optional<String> destinationLocation) {
        return Optional.of(HotelResponseModel.builder()
                .availableHotels(Arrays.asList(
                        HotelOfferModel.builder()
                                .hotelId("123")
                                .name("mockHotel")
                                .rating(5.0)
                                .stars(5.0)
                                .location("Grecja")
                                .startDate(LocalDate.of(2023, 5, 31))
                                .endDate(LocalDate.of(2023, 6, 30))
                                .numOfPeople(10)
                                .build(),
                        HotelOfferModel.builder()
                                .hotelId("1234")
                                .name("mockHotel2")
                                .rating(5.0)
                                .stars(5.0)
                                .location("Turcja")
                                .startDate(LocalDate.of(2023, 5, 5))
                                .endDate(LocalDate.of(2023, 6, 30))
                                .numOfPeople(2)
                                .build(),
                        HotelOfferModel.builder()
                                .hotelId("1235")
                                .name("mockHotel3")
                                .rating(5.0)
                                .stars(5.0)
                                .location("Albania")
                                .startDate(LocalDate.of(2023, 6, 10))
                                .endDate(LocalDate.of(2023, 6, 30))
                                .numOfPeople(3)
                                .build(),
                        HotelOfferModel.builder()
                                .hotelId("1236")
                                .name("mockHotel4")
                                .rating(5.0)
                                .stars(5.0)
                                .location("Dominikana")
                                .startDate(LocalDate.of(2023, 4, 1))
                                .endDate(LocalDate.of(2023, 6, 30))
                                .numOfPeople(4)
                                .build(),
                        HotelOfferModel.builder()
                                .hotelId("1237")
                                .name("mockHotel5")
                                .rating(5.0)
                                .stars(5.0)
                                .location("Oman")
                                .startDate(LocalDate.of(2023, 4, 10))
                                .endDate(LocalDate.of(2023, 6, 30))
                                .numOfPeople(5)
                                .build()
                        )
                ).build());
    }
}
