package otus.ware;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WareRepositoryInterface extends JpaRepository<Ware, Long> {
    Optional<Ware> findById(long id);
}
