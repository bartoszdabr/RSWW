package reservation.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import reservation.AgeRange;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;

@Builder
@Getter
@Setter
@Document("events")
public class ReservationEvent implements Serializable {

    @MongoId
    private String eventId;

    private Instant timestamp;

    private String status;

    private String username;

    private String transportId;

    private Long roomReservationId;

    private String reservationId;

    private HashMap<AgeRange, Long> ageGroupsSize;

    private Double cost;

    public Long getNumOfPeople() {
        return ageGroupsSize.values().stream().reduce(0L, Long::sum);
    }

}
