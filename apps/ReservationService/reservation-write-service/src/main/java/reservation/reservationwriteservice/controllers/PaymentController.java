package reservation.reservationwriteservice.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reservation.reservationwriteservice.models.PaymentPayload;
import reservation.reservationwriteservice.services.SagaService;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final Logger log = LogManager.getLogger(PaymentController.class);

    private final SagaService sagaService;

    @PostMapping("/payment")
    public ResponseEntity<Void> payForReservation(@RequestBody PaymentPayload paymentPayload) {
        log.info("Payment request for reservation: " + paymentPayload.reservationId() + " started.");
        sagaService.processPaymentForReservation(paymentPayload.reservationId());
        log.info("Finished processing payment request for reservation: "
                + paymentPayload.reservationId() + " finished.");

        return ResponseEntity
                .ok()
                .build();
    }
}
