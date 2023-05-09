package transport.read.filters;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import transport.read.entity.Transport;
import transport.read.entity.Transport_;
import java.util.Optional;

@AllArgsConstructor
public class TransportWithDestinationPlace implements Specification<Transport> {

  private final Optional<String> destinationPlace;

  @Override
  public Predicate toPredicate(
      Root<Transport> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return destinationPlace
        .map(dp -> criteriaBuilder.equal(root.get(Transport_.DESTINATION_PLACE), dp))
        .orElseGet(criteriaBuilder::and);
  }
}
