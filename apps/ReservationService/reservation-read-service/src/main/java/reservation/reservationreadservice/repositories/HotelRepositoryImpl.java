package reservation.reservationreadservice.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reservation.reservationreadservice.models.HotelResponseModel;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Repository
@Profile("default")
public class HotelRepositoryImpl implements HotelRepository {

    private final Logger log = LogManager.getLogger(HotelRepositoryImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.hotel.apiUrl}")
    private String hotelServiceBaseApiUrl;

    @Value("${services.hotel.hotelsEndpoint}")
    private String hotelServiceHotelsEndpoint;


    @Override
    public Optional<HotelResponseModel> findHotels(Optional<LocalDate> startDate, Optional<LocalDate> endDate,
                                                   Long numOfPeople, Optional<String> destinationLocation) {
        var uri = UriComponentsBuilder.
                fromHttpUrl(hotelServiceBaseApiUrl)
                .path(hotelServiceHotelsEndpoint)
                .queryParam("fromDate", startDate)
                .queryParam("toDate", endDate)
                .queryParam("numberOfGuestsInAllHotel", numOfPeople)
                .queryParam("destination", destinationLocation)
                .build().toUri();

        log.info("Calling url " + uri);
        var response = restTemplate.getForEntity(uri, HotelResponseModel.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error(uri + " returned non-ok status code.");
            throw new ResponseStatusException(NOT_FOUND, "Unable to find matching hotels.");
        }
        log.info("Finished calling " + uri);

        return Optional.ofNullable(response.getBody());
    }
}
