package reservation.reservationreadservice.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reservation.AddReservationEvent;
import reservation.RemoveReservationEvent;
import reservation.reservationreadservice.models.ReservationModel;
import reservation.reservationreadservice.repositories.ReservationRepository;

@Service
public class EventHandlerService {

    Logger log = LogManager.getLogger(EventHandlerService.class);

    private final ReservationRepository reservationRepository;

    public EventHandlerService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void handleAddReservationEvent(AddReservationEvent addReservationEvent) {
        var reservationEntity = ReservationModel.builder()
                .id(addReservationEvent.getEventId())
                .username(addReservationEvent.getUsername())
                .transportId(addReservationEvent.getTransportId())
                .roomReservationId(addReservationEvent.getRoomReservationId())
                .ageGroupsSize(addReservationEvent.getAgeGroupsSize())
                .cost(addReservationEvent.getCost())
                .timestamp(addReservationEvent.getTimestamp())
                .build();

        saveReservationToNoSql(reservationEntity);

    }

    public void handleRemoveReservationEvent(RemoveReservationEvent removeReservationEvent) {
        reservationRepository.findById(removeReservationEvent.getRemovedReservationId()).ifPresent(reservationRepository::delete);
        log.info("Removed reservation id: " + removeReservationEvent.getEventId());

    }

    private void saveReservationToNoSql(ReservationModel reservationModel) {
        reservationRepository.insert(reservationModel);
        log.info("Reservation id: " + reservationModel.getId() + " inserted to db");
    }
}
