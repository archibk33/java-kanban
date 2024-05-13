package main.java.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    //TODO: 1) Я погуглил + поспрашивал у знакомых про атомарный счетчик. Его можно использовать на проектах? 2) ById - я поменял и вдругих местах. Спасибо большое за ревью :)
    //TODO: Check link on git
    private AtomicInteger nextId = new AtomicInteger(1);

    public void addNewTask(Task task) {
        int id = task.getId();
        if (id == 0) {
            id = generateUniqueId();
            task.setId(id);
        } else if (!isIdUnique(id)) {
            throw new IllegalArgumentException("Задача с таким ID уже существует! -  " + id);
        }
        if (task instanceof Epic) {
            epics.put(id, (Epic) task);
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            subtasks.put(id, subtask);
            Epic parentEpic = subtask.getParentEpic();
            if (parentEpic != null) {
                parentEpic.addSubtask(subtask);
                updateEpicStatus(parentEpic);
            }
        } else {
            tasks.put(id, task);
        }
    }

    private boolean isIdUnique(int id) {
        return !tasks.containsKey(id) && !epics.containsKey(id) && !subtasks.containsKey(id);
    }

    public int generateUniqueId() {
        return nextId.getAndIncrement();
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        }
        return null;
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public List<Subtask> getSubtasksByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        return epic != null ? new ArrayList<>(epic.getSubtasks()) : new ArrayList<>();
    }

    public void updateTask(Task task) {
        int id = task.getId();
        if (task instanceof Epic) {
            epics.put(id, (Epic) task);
            updateEpicStatus((Epic) task);
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            subtasks.put(id, subtask);
            updateEpicStatus(subtask.getParentEpic());
        } else {
            tasks.put(id, task);
        }
    }

    private void updateEpicStatus(Epic epic) {
        boolean allNew = true;
        boolean allDone = true;
        for (Subtask subtask : epic.getSubtasks()) {
            Status status = subtask.getStatus();
            if (status != Status.NEW) {
                allNew = false;
            }
            if (status != Status.DONE) {
                allDone = false;
            }
            if (!allNew && !allDone) {
                break;
            }
        }
        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteTaskOnId(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            Epic parentEpic = subtask.getParentEpic();
            if (parentEpic != null) {
                parentEpic.getSubtasks().remove(subtask);
                updateEpicStatus(parentEpic);
            }
        } else if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
            epic.getSubtasks().clear();
        }
    }
}
