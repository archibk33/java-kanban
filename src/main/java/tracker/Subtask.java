package main.java.tracker;

public class Subtask extends Task {
    private Epic parentEpic;

    public Subtask(int i, String title, String description, Status status, Epic parentEpic) {
        super(title, description, status);
        this.parentEpic = parentEpic;
    }

    public Epic getParentEpic() {
        return parentEpic;
    }
}
