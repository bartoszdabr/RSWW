package reservation.reservationreadservice.repositories;

import reservation.reservationreadservice.models.HotelModel;
import reservation.reservationreadservice.models.TransportResponseModel;

import java.util.Optional;

public interface TransportRepository {

    Optional<TransportResponseModel> findAvailableTransports(Optional<String> startLocation,
                                                             Optional<String> destinationLocation,
                                                             HotelModel hotelModel);
}
