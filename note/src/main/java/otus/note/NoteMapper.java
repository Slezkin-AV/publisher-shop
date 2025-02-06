package otus.note;

public class NoteMapper {

    // Convert Note JPA Entity into NoteDto
    public static NoteDto mapToNoteDto(Note note) {
        //случайное замедление
//        Random random = new Random();
//        int randomNumber = random.nextInt(1000);  // вернёт случайное число от 0 до 999
//        try{
//            Thread.sleep(100 + randomNumber);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //конец случайного замедления

        return new NoteDto(
                note.getId(),
                note.getStatusDescription()
        );
    }

    // Convert NoteDto into Note JPA Entity
    public static Note mapToNote(NoteDto noteDto) {
        return new Note(
                noteDto.getId(),
                noteDto.getStatusDescription()
        );
    }
}

