package reservation.reservationwriteservice.models;

import reservation.AgeRange;

import java.util.HashMap;

public record AddReservation(String username, String transportId, Long roomReservationId,
                             HashMap<AgeRange, Long> ageGroupsSize, Long numOfDays) {
}
