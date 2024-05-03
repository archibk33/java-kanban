package main.java.tracker;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(int i, String title, String description, Status status) {
        super(title, description, status);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        this.subtasks.add(subtask);
    }
}

