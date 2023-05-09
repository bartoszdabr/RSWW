package transport.read.filters;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import transport.read.entity.Transport;
import transport.read.entity.Transport_;

import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
public class TransportWithDate implements Specification<Transport> {
  private Optional<LocalDate> date;

  @Override
  public Predicate toPredicate(
      Root<Transport> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return date.map(date -> criteriaBuilder.equal(root.get(Transport_.DATE), date))
        .orElseGet(criteriaBuilder::and);
  }
}
