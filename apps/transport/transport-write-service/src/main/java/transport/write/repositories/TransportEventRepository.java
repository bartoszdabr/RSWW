package transport.write.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import transport.write.events.TransportEvent;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransportEventRepository extends JpaRepository<TransportEvent, UUID> {
  List<TransportEvent> findByTransportId(String transportId);
}
