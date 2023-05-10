package reservation.reservationwriteservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import reservation.BaseEvent;

@Repository
public interface EventRepository extends MongoRepository<BaseEvent, String> {
}
