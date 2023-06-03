package reservation.reservationwriteservice.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import reservation.events.ReservationEvent;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<ReservationEvent, String> {

    @Query("{'_id': ?0, 'status': ?1}")
    Optional<ReservationEvent> findFinishedReservationById(String eventId, String status);

    @Query("{reservationId: '?0'}")
    List<ReservationEvent> findReservationEventsByReservationIdOrderByTimestamp(String reservationId, Sort sort);
}
