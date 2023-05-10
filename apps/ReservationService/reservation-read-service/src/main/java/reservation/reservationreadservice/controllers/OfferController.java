package reservation.reservationreadservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reservation.reservationreadservice.models.HotelOfferModel;
import reservation.reservationreadservice.services.OfferService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class OfferController {

    private final Logger log = LogManager.getLogger(OfferController.class);

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("offers")
    public ResponseEntity<List<HotelOfferModel>> getOffers(
            @RequestParam Optional<String> startLocation,
            @RequestParam Optional<String> destinationLocation,
            @RequestParam Optional<LocalDate> startDate,
            @RequestParam Optional<LocalDate> endDate,
            @RequestParam Optional<Integer> numOfPeople
    ) {
        log.info("New find reservations request");
        var offers = offerService.findOffers(startLocation, destinationLocation, startDate, endDate, numOfPeople);


        return ResponseEntity.ok(offers);
    }
}
