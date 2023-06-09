package reservation.reservationwriteservice.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReservationResponseModel {

    private final String reservationId;

}
