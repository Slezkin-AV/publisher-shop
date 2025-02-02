package otus.note;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.lib.event.*;
import otus.lib.exception.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService implements NoteServiceInterface {

    private final NoteRepositoryInterface noteRepository;


    @Override
    public NoteDto addNote(Note note){
        Note note1 = null;
        try {
            note1 = noteRepository.save(note);
        }catch (RuntimeException ex){
            log.info(String.valueOf(ex));
        }
        assert note1 != null;
        return NoteMapper.mapToNoteDto(note1);
    }

    public void cleanAll(){
        noteRepository.deleteAll();
    }

}
