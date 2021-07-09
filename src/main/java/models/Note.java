package models;

public class Note {
    private int noteId;
    private int traineeId;
    private int batchId;
    private int weekId;
    private String noteContent;

    public Note() {
        super();
        noteId = 0;
        traineeId = 0;
        batchId = 0;
        weekId = 0;
        noteContent = "";
    }

    public Note(int noteId, int traineeId, int batchId, int weekId, String noteContent) {
        super();
        this.noteId = noteId;
        this.traineeId = traineeId;
        this.batchId = batchId;
        this.weekId = weekId;
        this.noteContent = noteContent;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(int traineeId) {
        this.traineeId = traineeId;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getWeekId() {
        return weekId;
    }

    public void setWeekId(int weekId) {
        this.weekId = weekId;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    @Override
    public String toString() {
        return "Note [batchId=" + batchId + ", noteContent=" + noteContent + ", noteId=" + noteId + ", traineeId="
                + traineeId + ", weekId=" + weekId + "]";
    }

}
