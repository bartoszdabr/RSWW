package reservation.reservationreadservice.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class TransportModel {

    private String id;

    private String sourcePlace;

    private String destinationPlace;

    private LocalDate date;

    private Integer availableSeats;

}
