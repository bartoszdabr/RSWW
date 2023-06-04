package reservation.reservationreadservice.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reservation.events.ReservationEvent;
import reservation.reservationreadservice.entities.ReservationEntity;
import reservation.reservationreadservice.repositories.ReservationRepository;

@Service
@RequiredArgsConstructor
public class EventHandlerService {

    private final Logger log = LogManager.getLogger(EventHandlerService.class);

    private final ReservationRepository reservationRepository;

    public void handleAddReservationEvent(ReservationEvent reservationEvent) {
        var reservationEntity = ReservationEntity.builder()
                .id(reservationEvent.getReservationId())
                .username(reservationEvent.getUsername())
                .transportId(reservationEvent.getTransportId())
                .roomReservationId(reservationEvent.getRoomReservationId())
                .ageGroupsSize(reservationEvent.getAgeGroupsSize())
                .cost(reservationEvent.getCost())
                .timestamp(reservationEvent.getTimestamp())
                .status(reservationEvent.getStatus())
                .build();

        saveReservationToNoSql(reservationEntity);

    }

    private void saveReservationToNoSql(ReservationEntity reservationEntity) {
        log.info("Saving reservation with id: " + reservationEntity.getId());
        reservationRepository.save(reservationEntity);
        log.info("Reservation id: " + reservationEntity.getId() + " inserted to db");
    }
}
