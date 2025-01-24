package otus.billing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepositoryInterface extends JpaRepository<Account, Long> {
    List<Account> findByUserId(long userId);
}
