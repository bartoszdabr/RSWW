package reservation.reservationreadservice.services;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reservation.events.ReservationStatuses;
import reservation.reservationreadservice.controllers.NotificationController;
import reservation.reservationreadservice.repositories.ReservationRepository;

import java.time.Instant;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final ReservationRepository reservationRepository;

    public Instant checkIfBought(String hotelId, String transportId) {
        log.info("Checking db for reservation for hotelId: " + hotelId + " and transportId: " + transportId);
        var reservation =  reservationRepository.findFirstByRoomReservationIdAndTransportIdAndStatusOrderByTimestampDesc(hotelId, transportId,
                ReservationStatuses.RESERVED.getText());

        return reservation
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No buy events for provided ids"))
                .getTimestamp();
    }
}
