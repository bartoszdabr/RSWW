package reservation.reservationreadservice.controllers;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OfferController {

    private final Logger log = LogManager.getLogger(OfferController.class);

    private final OfferService offerService;

    @GetMapping("offers")
    public ResponseEntity<List<HotelOfferModel>> getOffers(
            @RequestParam Optional<String> startLocation,
            @RequestParam Optional<String> destinationLocation,
            @RequestParam Optional<LocalDate> startDate,
            @RequestParam Optional<LocalDate> endDate,
            @RequestParam Optional<Long> adults,
            @RequestParam Optional<Long> under3YearsOld,
            @RequestParam Optional<Long> under10YearsOld,
            @RequestParam Optional<Long> under18YearsOld
    ) {
        log.info("New find offers request");
        var offers = offerService.findOffers(startLocation, destinationLocation,
                startDate, endDate, adults, under3YearsOld, under10YearsOld, under18YearsOld);
        log.info("Finished processing find offers request. Found " + offers.size() + " offers.");

        return ResponseEntity.ok(offers);
    }
}
