package reservation.reservationreadservice.models;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import reservation.AgeRange;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Builder
@Getter
@Document("reservations")
public class ReservationModel {

    @MongoId
    private String id;

    private String username;

    private Long transportId;

    private Long roomReservationId;

    private HashMap<AgeRange, Long> ageGroupsSize;

    private Double cost;

    private Instant timestamp;
}
