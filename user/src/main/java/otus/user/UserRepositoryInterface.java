package otus.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryInterface extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);
    List<User> findByLogin(String login);
}
