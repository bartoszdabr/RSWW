package reservation.reservationreadservice.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import reservation.reservationreadservice.entities.ReservationEntity;

import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<ReservationEntity, String> {

    @Query(value = "{$and: [{roomReservationId:  '?0'}, {transportId: '?1'}, {status:  '?2'}]}")
    List<ReservationEntity> findByHotelAndTransportIdsAndStatus(String hotelId, String transportId, String status, Sort sort);
}
