package reservation.reservationreadservice.entities;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import reservation.AgeRange;

import java.time.Instant;
import java.util.HashMap;

@Builder
@Getter
@Document("reservations")
public class ReservationEntity {

    @MongoId
    private String id;

    private String username;

    private Long transportId;

    private Long roomReservationId;

    private HashMap<AgeRange, Long> ageGroupsSize;

    private Double cost;

    private Instant timestamp;
}
