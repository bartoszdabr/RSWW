package transport.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
public class CreateTransportEvent extends BaseEvent {
    private String destinationPlace;
    private String sourcePlace;
    private LocalDate date;
    private Integer availableSeats;
}
