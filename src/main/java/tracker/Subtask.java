package main.java.tracker;

public class Subtask extends Task {
    private Epic parentEpic;

    public Subtask(int i, String title, String description, Status status, Epic parentEpic) {
        super(title, description, status);
        setParentEpic(parentEpic);
        parentEpic.addSubtask(this);
    }

    public Epic getParentEpic() {
        return parentEpic;
    }

    public void setParentEpic(Epic parentEpic) {
        this.parentEpic = parentEpic;
    }
}
