package reservation.reservationreadservice.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reservation.reservationreadservice.models.SagaStatusResponse;
import reservation.reservationreadservice.services.SagaStatusService;

@RestController
@RequiredArgsConstructor
public class SagaStatusController {

    private final Logger log = LogManager.getLogger(SagaStatusController.class);

    private final SagaStatusService sagaStatusService;

    @GetMapping("/saga/status")
    public ResponseEntity<SagaStatusResponse> getSagaStatus(
            @RequestParam("hotelId") String hotelId,
            @RequestParam("transportId") String transportId
    ) {
        log.info("Get status for saga with hotelId: " + hotelId + " transportId: " + transportId);
        var status = sagaStatusService.getSagaStatus(hotelId, transportId);
        log.info("Request finished for saga status with hotelId: " + hotelId + " transportId: " + transportId);
        return ResponseEntity
                .ok(SagaStatusResponse
                        .builder()
                        .status(status)
                        .build());
    }

}
