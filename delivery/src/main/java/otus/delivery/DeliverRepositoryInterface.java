package otus.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DeliverRepositoryInterface extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findById(long id);
}
