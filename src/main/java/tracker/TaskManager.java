package main.java.tracker;

import java.util.List;

public interface TaskManager {

    void addNewTask(Task task);

    Task getTaskById(int id);

    List<Task> getAllTasks();

    void updateTask(Task task);

    void deleteTaskOnId(int id);

    List<Task> getHistory();

    List<Subtask> getSubtasksByEpic(int id);
}
