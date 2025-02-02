package otus.note;

public class NoteMapper {

    // Convert Note JPA Entity into NoteDto
    public static NoteDto mapToNoteDto(Note note) {
        NoteDto noteDto = new NoteDto(
                note.getId()
        );
        //случайное замедление
//        Random random = new Random();
//        int randomNumber = random.nextInt(1000);  // вернёт случайное число от 0 до 999
//        try{
//            Thread.sleep(100 + randomNumber);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //конец случайного замедления

        return noteDto;
    }

    // Convert NoteDto into Note JPA Entity
    public static Note mapToNote(NoteDto noteDto) {
        Note note = new Note(
                noteDto.getId()
        );
        return note;
    }
}

