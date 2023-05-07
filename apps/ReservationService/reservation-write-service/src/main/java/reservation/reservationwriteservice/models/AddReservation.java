package reservation.reservationwriteservice.models;

import reservation.AgeRange;

import java.util.HashMap;

public record AddReservation(String username, Long transportId, Long reservationId,
                             HashMap<AgeRange, Long> ageGroupsSize, Double cost) {
}
