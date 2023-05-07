package reservation;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.Instant;

@Document("events")
@SuperBuilder
@Getter
public abstract class BaseEvent implements Serializable {

    @MongoId
    private String eventId;

    private Instant timestamp;
}
