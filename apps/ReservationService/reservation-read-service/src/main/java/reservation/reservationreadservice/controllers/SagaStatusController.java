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
            @RequestParam("reservationId") String reservationId
    ) {
        log.info("Get status for saga with reservation id: " + reservationId);
        var status = sagaStatusService.getSagaStatus(reservationId);
        log.info("Request finished for saga status check with reservation id : " + reservationId);
        return ResponseEntity
                .ok(SagaStatusResponse
                        .builder()
                        .status(status)
                        .build());
    }

}
