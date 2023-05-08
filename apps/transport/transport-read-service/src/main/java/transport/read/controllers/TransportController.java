package transport.read.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import transport.common.Event;

@RestController
public class TransportController {
  @GetMapping("hello")
  public ResponseEntity<String> hello() {
    Event e = new Event();
    return ResponseEntity.ok("hello");
  }
}
