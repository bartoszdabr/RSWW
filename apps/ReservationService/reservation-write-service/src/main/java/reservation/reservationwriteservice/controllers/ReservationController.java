package reservation.reservationwriteservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reservation.reservationwriteservice.models.AddReservation;
import reservation.reservationwriteservice.models.RemoveReservation;
import reservation.reservationwriteservice.services.EventService;

@RestController
public class ReservationController {

    Logger log = LogManager.getLogger(ReservationController.class);

    private final EventService eventService;

    public ReservationController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("add")
    public ResponseEntity<Void> addNewReservation(@RequestBody AddReservation addReservation) {
        log.info("New reservation event request");
        eventService.addNewReservation(addReservation);

        log.info("Reservation event request succeeded");
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("remove")
    public ResponseEntity<Void> removeReservation(@RequestBody RemoveReservation removeReservation) {
        log.info("New reservation event request");
        eventService.removeReservation(removeReservation);

        log.info("Reservation event request succeeded");
        return ResponseEntity
                .ok()
                .build();
    }
}