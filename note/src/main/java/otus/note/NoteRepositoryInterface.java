package otus.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteRepositoryInterface extends JpaRepository<Note, Long> {
    List<Note> findById(long id);
}
