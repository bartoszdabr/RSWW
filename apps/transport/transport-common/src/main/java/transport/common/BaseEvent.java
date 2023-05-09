package transport.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEvent implements Serializable {
  private String eventId;
  private Instant timeStamp;
  private String transportId;
}
