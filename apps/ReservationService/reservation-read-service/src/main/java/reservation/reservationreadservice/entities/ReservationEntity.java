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
@Document("#{@environment.getProperty('reservations.collection')}")
public class ReservationEntity {

    @MongoId
    private String id;

    private String username;

    private String transportId;

    private Long roomReservationId;

    private HashMap<AgeRange, Long> ageGroupsSize;

    private Double cost;

    private Instant timestamp;

    private String status;
}
