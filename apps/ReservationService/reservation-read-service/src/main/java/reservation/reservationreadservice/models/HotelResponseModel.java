package reservation.reservationreadservice.models;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class HotelResponseModel {

    private List<HotelOfferModel> availableHotels;
}
