package reservation.reservationwriteservice.models;

import reservation.AgeRange;

import java.util.HashMap;

public record AddReservation(String username, String transportId, String roomReservationId,
                             HashMap<AgeRange, Long> ageGroupsSize) {
}
