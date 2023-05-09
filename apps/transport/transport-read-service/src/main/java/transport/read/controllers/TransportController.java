package transport.read.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import transport.read.entity.Transport;
import transport.read.services.TransportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class TransportController {
  private final TransportService transportService;

  public TransportController(TransportService transportService) {
    this.transportService = transportService;
  }

  @GetMapping("hello")
  public ResponseEntity<String> hello() {
    return ResponseEntity.ok("hello");
  }

  @GetMapping("transports")
  public ResponseEntity<List<Transport>> getTransports(
      @RequestParam("sourcePlace") Optional<String> sourcePlace,
      @RequestParam("destinationPlace") Optional<String> destinationPlace,
      @RequestParam("date") Optional<LocalDate> date) {

    return ResponseEntity.ok(transportService.findTransports(sourcePlace, destinationPlace, date));
  }
}
