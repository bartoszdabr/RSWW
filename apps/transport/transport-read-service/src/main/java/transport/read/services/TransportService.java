package transport.read.services;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import transport.common.AddReservationEvent;
import transport.common.CancelReservationEvent;
import transport.common.CreateTransportEvent;
import transport.models.TransportResponseModel;
import transport.read.entity.Transport;
import transport.read.filters.TransportWithDate;
import transport.read.filters.TransportWithDestinationPlace;
import transport.read.filters.TransportWithMinSeats;
import transport.read.filters.TransportWithSourcePlace;
import transport.read.repositories.TransportsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransportService {
  private final Logger log = LogManager.getLogger(TransportService.class);
  private final TransportsRepository transportsRepository;

  TransportService(TransportsRepository transportsRepository) {
    this.transportsRepository = transportsRepository;
  }

  @Transactional
  public void addNewReservation(AddReservationEvent event) {
    transportsRepository
        .findById(event.getTransportId())
        .ifPresentOrElse(
            transport -> transport.handle(event),
            () -> log.warn("Transport with given id was not found"));
  }

  @Transactional
  public void cancelReservation(CancelReservationEvent event) {
    transportsRepository
        .findById(event.getTransportId())
        .ifPresentOrElse(
            transport -> transport.handle(event),
            () -> log.warn("Transport with given id was not found"));
  }

  public TransportResponseModel findTransports(
      Optional<String> sourcePlace,
      Optional<String> destinationPlace,
      Optional<LocalDate> date,
      Optional<Integer> numOfPeople) {

    Specification<Transport> specification =
        Specification.where(new TransportWithDestinationPlace(destinationPlace))
            .and(new TransportWithSourcePlace(sourcePlace))
            .and(new TransportWithDate(date))
            .and(new TransportWithMinSeats(numOfPeople));

    return TransportResponseModel.builder()
        .matchingTransports(transportsRepository.findAll(specification))
        .build();
  }

  public Optional<Transport> findTransport(String id) {
    return transportsRepository.findById(id);
  }

  public void addNewTransport(CreateTransportEvent event) {
    Transport transport =
        Transport.builder()
            .id(event.getTransportId())
            .destinationPlace(event.getDestinationPlace())
            .sourcePlace(event.getSourcePlace())
            .date(event.getDate())
            .availableSeats(event.getAvailableSeats())
            .build();
    transportsRepository.save(transport);
  }

  public List<String> findAllIds() {
    return transportsRepository.findAll().stream()
        .map(Transport::getId)
        .collect(Collectors.toList());
  }
}
