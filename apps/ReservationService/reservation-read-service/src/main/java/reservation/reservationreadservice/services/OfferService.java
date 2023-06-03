package reservation.reservationreadservice.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reservation.reservationreadservice.models.HotelOfferModel;
import reservation.reservationreadservice.models.TransportModel;
import reservation.reservationreadservice.repositories.HotelRepository;
import reservation.reservationreadservice.repositories.TransportRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class OfferService {

    private final Logger log = LogManager.getLogger(OfferService.class);

    private final HotelRepository hotelRepository;

    private final TransportRepository transportRepository;

    public OfferService(HotelRepository hotelRepository, TransportRepository transportRepository) {
        this.hotelRepository = hotelRepository;
        this.transportRepository = transportRepository;
    }

    public List<HotelOfferModel> findOffers(Optional<String> startLocation,
                                            Optional<String> destinationLocation,
                                            Optional<LocalDate> startDate,
                                            Optional<LocalDate> endDate,
                                            Optional<Integer> numOfPeople) {
        var availableHotels = getHotels(startDate, endDate, numOfPeople, destinationLocation);
        availableHotels.forEach(hotelOffer -> {
                    hotelOffer.setTransports(findMatchingTransport(startLocation, hotelOffer));
                    hotelOffer.setCost(calculateOfferCost(hotelOffer));
                }
        );

        return availableHotels;
    }

    private Double calculateOfferCost(HotelOfferModel hotelOffer) {
        // TODO: Dodaj algorytm liczenia kosztu wycieczki
        return null;
    }

    private List<TransportModel> findMatchingTransport(Optional<String> startLocation, HotelOfferModel hotel) {
        var matchingTransportsFromApi = transportRepository.findAvailableTransports(
                startLocation, hotel);

        return matchingTransportsFromApi.isPresent()
                ? matchingTransportsFromApi.get().getMatchingTransports()
                : Collections.emptyList();
    }

    private List<HotelOfferModel> getHotels(Optional<LocalDate> startDate, Optional<LocalDate> endDate,
                                            Optional<Integer> numOfPeople, Optional<String> destinationLocation) {
        var hotelsOptional = hotelRepository.findHotels(startDate, endDate, numOfPeople, destinationLocation);

        return hotelsOptional.orElseThrow(() -> {
            log.info("Couldn't find matching hotels.");
            throw new ResponseStatusException(NOT_FOUND, "Unable to find matching hotels.");
        }).getAvailableHotels();
    }
}
