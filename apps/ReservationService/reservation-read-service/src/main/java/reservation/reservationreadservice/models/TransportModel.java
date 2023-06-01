package reservation.reservationreadservice.models;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransportModel {

    private final String id;

    private final String sourcePlace;

    private final String destinationPlace;

    private final LocalDateTime date;

    private final Integer availableSeats;

}
