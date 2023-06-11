package reservation.reservationwriteservice.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reservation.reservationwriteservice.models.AddReservation;
import reservation.reservationwriteservice.models.ReservationResponseModel;
import reservation.reservationwriteservice.services.EventService;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final Logger log = LogManager.getLogger(ReservationController.class);

    private final EventService eventService;

    @PostMapping("reservations")
    public ResponseEntity<ReservationResponseModel> addNewReservation(@RequestBody AddReservation addReservation) {
        log.info("New reservation event request");
        var reservationResponse = eventService.addNewReservation(addReservation);
        log.info("Reservation event request succeeded");
        
        return ResponseEntity.ok(reservationResponse);
    }
}
