package reservation.reservationwriteservice.saga;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reservation.events.ReservationStatuses;
import reservation.reservationwriteservice.repositories.EventRepository;
import reservation.reservationwriteservice.services.EventService;
import reservation.reservationwriteservice.services.SagaRollbackService;

@Component
@AllArgsConstructor
public class TimeoutTask {

    private final Logger log = LogManager.getLogger(EventService.class);

    private final EventRepository eventRepository;

    private final SagaRollbackService sagaRollbackService;
    @Async
    public void checkIfReservationTimeouted(String reservationId) {
        log.info("Checking if reservation: " + reservationId + " finished");
        var reservation = eventRepository.findFinishedReservationById(reservationId,
                ReservationStatuses.RESERVED.getText());

        if (reservation.isEmpty()) {
            revertTransaction(reservationId);
        }
        log.info("Timeout check for reservation: " + reservationId + " completed");
    }

    private void revertTransaction(String reservationId) {
        log.info("Timeout exceeded for reservation: " + reservationId);
        sagaRollbackService.rollbackSagaById(reservationId);
        log.info("Revert events sent successfully for reservation: " + reservationId);
    }
}
