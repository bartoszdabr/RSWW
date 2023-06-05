package reservation.reservationreadservice.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reservation.reservationreadservice.repositories.ReservationRepository;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SagaStatusService {

    private final Logger log = LogManager.getLogger(SagaStatusService.class);

    private final ReservationRepository reservationRepository;

    public String getSagaStatus(String hotelId, String transportId) {
        var status = reservationRepository.findFirstByRoomReservationIdAndTransportIdOrderByTimestampDesc(
                hotelId, transportId);

        return status.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No reservation for provided ids")).getStatus();
    }
}
