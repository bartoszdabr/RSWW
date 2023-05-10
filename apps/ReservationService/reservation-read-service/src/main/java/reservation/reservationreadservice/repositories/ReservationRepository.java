package reservation.reservationreadservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import reservation.reservationreadservice.models.ReservationModel;

@Repository
public interface ReservationRepository extends MongoRepository<ReservationModel, String> {
}
