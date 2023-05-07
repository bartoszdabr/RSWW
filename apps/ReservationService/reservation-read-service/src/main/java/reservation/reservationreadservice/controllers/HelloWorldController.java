package reservation.reservationreadservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reservation.AddReservationEvent;

@RestController
public class HelloWorldController {

    @GetMapping("helloWorld")
    public ResponseEntity<String> helloWorld() {
        new AddReservationEvent();
        return ResponseEntity.ok("Hello World!");
    }
}
