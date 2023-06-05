package reservation.reservationreadservice.models;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class HotelResponseModel {

    private List<HotelOfferModel> availableHotels;
}
