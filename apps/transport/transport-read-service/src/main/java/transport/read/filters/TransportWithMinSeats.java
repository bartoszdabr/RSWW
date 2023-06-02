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
public class TransportWithMinSeats implements Specification<Transport> {

    private final Optional<Integer> numOfPeople;

    @Override
    public Predicate toPredicate(Root<Transport> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return numOfPeople
                .map(ppl -> criteriaBuilder.greaterThanOrEqualTo(root.get(Transport_.AVAILABLE_SEATS), ppl))
                .orElseGet(criteriaBuilder::and);
    }
}
