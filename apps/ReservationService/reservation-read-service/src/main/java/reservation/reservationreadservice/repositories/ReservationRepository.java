package reservation.reservationreadservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import reservation.reservationreadservice.entities.ReservationEntity;

@Repository
public interface ReservationRepository extends MongoRepository<ReservationEntity, String> {
}
