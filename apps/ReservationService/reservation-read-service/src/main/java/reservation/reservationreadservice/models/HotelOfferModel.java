package reservation.reservationreadservice.models;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class HotelOfferModel {

    private String hotelId;

    private String name;

    private Double rating;

    private Double stars;

    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer numOfPeople;

    private List<TransportModel> transports;

    private Double cost;
}
