package reservation.reservationreadservice.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reservation.events.ReservationEvent;
import reservation.reservationreadservice.entities.ReservationEntity;
import reservation.reservationreadservice.repositories.ReservationRepository;

@Service
public class EventHandlerService {

    Logger log = LogManager.getLogger(EventHandlerService.class);

    private final ReservationRepository reservationRepository;

    public EventHandlerService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void handleAddReservationEvent(ReservationEvent reservationEvent) {
        log.info("New add reservation event.");
        var reservationEntity = ReservationEntity.builder()
                .id(reservationEvent.getEventId())
                .username(reservationEvent.getUsername())
                .transportId(reservationEvent.getTransportId())
                .roomReservationId(reservationEvent.getRoomReservationId())
                .ageGroupsSize(reservationEvent.getAgeGroupsSize())
                .cost(reservationEvent.getCost())
                .timestamp(reservationEvent.getTimestamp())
                .build();

        saveReservationToNoSql(reservationEntity);
        log.info("Finished handling new add reservation event.");

    }

    public void handleRemoveReservationEvent(ReservationEvent removeReservationEvent) {
        reservationRepository.findById(removeReservationEvent.getEventId()).ifPresent(reservationRepository::delete);
        log.info("Removed reservation id: " + removeReservationEvent.getEventId());

    }

    private void saveReservationToNoSql(ReservationEntity reservationEntity) {
        reservationRepository.insert(reservationEntity);
        log.info("Reservation id: " + reservationEntity.getId() + " inserted to db");
    }
}
