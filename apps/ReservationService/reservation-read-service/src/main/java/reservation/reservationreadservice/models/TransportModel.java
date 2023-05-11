package reservation.reservationreadservice.models;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransportModel {

    private final String transportId;

    private final String startLocation;

    private final String endLocation;

    private final LocalDateTime startTime;

    private final LocalDateTime endTime;

}
