package reservation.reservationreadservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import reservation.reservationreadservice.entities.ReservationEntity;

import java.util.Optional;

@Repository
public interface ReservationRepository extends MongoRepository<ReservationEntity, String> {

    Optional<ReservationEntity> findFirstByRoomReservationIdAndTransportIdAndStatusOrderByTimestampDesc(String roomReservationId,
                                                                                                        String transportId,
                                                                                                        String status);

    Optional<ReservationEntity> findFirstByIdOrderByTimestampDesc(String reservationId);
}
