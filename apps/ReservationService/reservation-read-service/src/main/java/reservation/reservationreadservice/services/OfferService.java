package reservation.reservationreadservice.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reservation.AgeRange;
import reservation.costcalculator.CostCalculator;
import reservation.costcalculator.CostCalculatorImpl;
import reservation.reservationreadservice.models.HotelOfferModel;
import reservation.reservationreadservice.models.TransportModel;
import reservation.reservationreadservice.repositories.HotelRepository;
import reservation.reservationreadservice.repositories.TransportRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;
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
                                            Optional<Long> adults,
                                            Optional<Long> under3YearsOld,
                                            Optional<Long> under10YearsOld,
                                            Optional<Long> under18YearsOld
                                            ) {
        var numOfPeople = Stream.of(adults, under3YearsOld, under10YearsOld, under18YearsOld)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(0L, Long::sum);

        var availableHotels = getHotels(startDate, endDate, numOfPeople, destinationLocation);
        availableHotels.forEach(hotelOffer -> {
                    hotelOffer.setTransports(findMatchingTransport(startLocation, hotelOffer));
                    hotelOffer.setCost(costCalculator.calculateOfferCost(Map.of(
                            AgeRange.LESS_THAN_3_YEARS_OLD, under3YearsOld.orElse(0L),
                            AgeRange.LESS_THAN_10_YEARS_OLD, under10YearsOld.orElse(0L),
                            AgeRange.LESS_THAN_18_YEARS_OLD, under18YearsOld.orElse(0L),
                            AgeRange.ADULT, adults.orElse(0L)
                    ), DAYS.between(hotelOffer.getStartDate(), hotelOffer.getEndDate())));
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
                                            Long numOfPeople, Optional<String> destinationLocation) {
        var hotelsOptional = hotelRepository.findHotels(startDate, endDate, numOfPeople, destinationLocation);

        return hotelsOptional.orElseThrow(() -> {
            log.info("Couldn't find matching hotels.");
            throw new ResponseStatusException(NOT_FOUND, "Unable to find matching hotels.");
        }).getAvailableHotels();
    }
}
