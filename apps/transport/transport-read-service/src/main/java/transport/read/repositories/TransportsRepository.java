package transport.read.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import transport.read.entity.Transport;

@Repository
public interface TransportsRepository
    extends JpaRepository<Transport, String>, JpaSpecificationExecutor<Transport> {}
