package reservation.reservationreadservice.models;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class NotificationResponseModel {

    private final Instant buyTimestamp;

}
