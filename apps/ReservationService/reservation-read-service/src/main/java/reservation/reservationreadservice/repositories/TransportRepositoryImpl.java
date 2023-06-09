package reservation.reservationreadservice.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reservation.reservationreadservice.models.HotelOfferModel;
import reservation.reservationreadservice.models.TransportResponseModel;

import java.net.URI;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Repository
public class TransportRepositoryImpl implements TransportRepository {

    private final Logger log = LogManager.getLogger(TransportRepositoryImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.transport.apiUrl}")
    private String transportServiceBaseApiUrl;

    @Value("${services.transport.transportsEndpoint}")
    private String transportServiceTransportsEndpoint;

    @Override
    public Optional<TransportResponseModel> findAvailableTransports(Optional<String> startLocation,
                                                                    HotelOfferModel hotelOfferModel) {
        var uri = buildUri(startLocation, hotelOfferModel);

        log.info("Calling url " + uri);
        var response = restTemplate.getForEntity(uri, TransportResponseModel.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error(uri + " returned non-ok status code.");
            throw new ResponseStatusException(NOT_FOUND, "Unable to find matching hotels.");
        }
        log.info("Finished calling " + uri);

        return Optional.ofNullable(response.getBody());
    }

    private URI buildUri(Optional<String> startLocation, HotelOfferModel hotelOfferModel) {
        var uriBuilder = UriComponentsBuilder
                .fromHttpUrl(transportServiceBaseApiUrl)
                .path(transportServiceTransportsEndpoint)
                .queryParam("date", hotelOfferModel.getStartDate())
                .queryParam("numOfPeople", hotelOfferModel.getNumOfPeople())
                .queryParam("destinationPlace", hotelOfferModel.getLocation());

        startLocation.ifPresent(startLocationValue -> uriBuilder.queryParam("sourcePlace", startLocationValue));

        return uriBuilder.build().toUri();
    }
}
