package reservation.reservationreadservice.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SagaStatusResponse {

    private final String status;
}
