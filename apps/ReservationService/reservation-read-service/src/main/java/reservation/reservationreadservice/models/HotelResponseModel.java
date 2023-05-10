package reservation.reservationreadservice.models;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class HotelResponseModel {

    private final List<HotelModel> availableHotels;
}
