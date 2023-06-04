package reservation.reservationreadservice.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reservation.costcalculator.CostCalculator;
import reservation.costcalculator.CostCalculatorImpl;
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
@RequiredArgsConstructor
public class OfferService {

    private final Logger log = LogManager.getLogger(OfferService.class);

    private final CostCalculator costCalculator = new CostCalculatorImpl();

    private final HotelRepository hotelRepository;

    private final TransportRepository transportRepository;

    public List<HotelOfferModel> findOffers(Optional<String> startLocation,
                                            Optional<String> destinationLocation,
                                            Optional<LocalDate> startDate,
                                            Optional<LocalDate> endDate,
                                            Optional<Integer> numOfPeople) {
        var availableHotels = getHotels(startDate, endDate, numOfPeople, destinationLocation);
        availableHotels.forEach(hotelOffer -> {
                    hotelOffer.setTransports(findMatchingTransport(startLocation, hotelOffer));
                    hotelOffer.setCost(costCalculator.calculateOfferCost(null, null, null)); // TODO
                }
        );

        return availableHotels;
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
