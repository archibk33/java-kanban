package main.java.tracker.managers;

import main.java.tracker.HistoryManager;
import main.java.tracker.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_SIZE_HISTORY = 10;
    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        Task taskCopy = new Task(task.getTitle(), task.getDescription(), task.getStatus());
        taskCopy.setId(task.getId());
        if (history.size() >= MAX_SIZE_HISTORY) {
            history.remove(0);
        }
        history.add(taskCopy);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
