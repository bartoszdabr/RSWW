package transport.write.messaging;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessage {
  private String id;
  private String status;
}
