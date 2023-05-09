package transport.write.events;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import transport.common.TransportEventType;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportEvent {
  @Id private String id;

  @Enumerated(EnumType.STRING)
  private TransportEventType type;

  private String transportId;
  private String eventJson;
}
