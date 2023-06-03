package transport.write.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import transport.write.model.TransportEventResponseModel;
import transport.write.services.EventService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class EventController {
  private final EventService eventService;

  @GetMapping("events")
  public ResponseEntity<TransportEventResponseModel> getEvents() {
    return ResponseEntity.ok(eventService.findAll());
  }

  @GetMapping("events/{id}")
  public ResponseEntity<TransportEventResponseModel> getEvents(@PathVariable String id) {
    return ResponseEntity.ok(eventService.find(id));
  }
}
