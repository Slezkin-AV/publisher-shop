package otus.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryInterface extends JpaRepository<UserPub, Long> {
    List<UserPub> findByEmail(String email);
    List<UserPub> findByLogin(String login);
}
