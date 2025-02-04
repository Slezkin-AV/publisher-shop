package otus.ware;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WareTypeRepositoryInterface extends JpaRepository<WareType, Long> {
    Optional<WareType> findById(long id);
}
