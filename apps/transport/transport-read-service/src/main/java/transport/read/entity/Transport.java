package transport.read.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import transport.common.AddReservationEvent;
import transport.common.CancelReservationEvent;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transport {
    @Id
    private String id;
    private String destinationPlace;
    private String sourcePlace;
    private LocalDate date;
    private Integer availableSeats;

    public void handle(CancelReservationEvent event) {
        this.availableSeats += event.getNumberOfPeople();
    }

    public void handle(AddReservationEvent event) {
        this.availableSeats -= event.getNumberOfPeople();
    }
}
