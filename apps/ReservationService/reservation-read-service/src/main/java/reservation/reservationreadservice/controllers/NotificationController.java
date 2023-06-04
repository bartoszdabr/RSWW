package reservation.reservationreadservice.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reservation.reservationreadservice.models.NotificationResponseModel;
import reservation.reservationreadservice.services.NotificationService;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final Logger log = LogManager.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<NotificationResponseModel> checkIfSomeoneBoughtOffer(
            @RequestParam("hotelId") String hotelId,
            @RequestParam("transportId") String transportId
    ) {
        log.info("Notification request for transportid: " + transportId + " hotelId: " + hotelId);
        var lastBuyTimestamp = notificationService.checkIfBought(hotelId, transportId);

        return ResponseEntity.ok(NotificationResponseModel.builder().buyTimestamp(lastBuyTimestamp).build());
    }

}
