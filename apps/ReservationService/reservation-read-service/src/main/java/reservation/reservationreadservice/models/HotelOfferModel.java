package reservation.reservationreadservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class HotelOfferModel {

    private final String hotelId;

    private final String name;

    private final Double rating;

    private final Double stars;

    private final String location;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final Integer numOfPeople;

    private List<TransportModel> transports;

    private Double cost;
}
