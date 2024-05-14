package main.java.tracker.managers;

import main.java.tracker.Subtask;
import main.java.tracker.Task;
import main.java.tracker.Epic;
import main.java.tracker.TaskManager;
import main.java.tracker.HistoryManager;
import main.java.tracker.Status;
import main.java.tracker.util.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;

    @Override
    public void addNewTask(Task task) {
        int id = task.getId();
        if (id == 0) {
            id = generateUniqueId();
            task.setId(id);
        } else if (!isIdUnique(id)) {
            throw new IllegalArgumentException("Задача с таким ID уже существует! -  " + id);
        }
        addTaskByType(task);
    }

    private void addTaskByType(Task task) {
        if (task instanceof Epic) {
            addEpic((Epic) task);
        } else if (task instanceof Subtask) {
            addSubtask((Subtask) task);
        } else {
            addSimpleTask(task);
        }
    }

    private void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    private void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic parentEpic = subtask.getParentEpic();
        if (parentEpic != null) {
            parentEpic.addSubtask(subtask);
            updateEpicStatus(parentEpic);
        }
    }

    private void addSimpleTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public boolean isIdUnique(int id) {
        return !tasks.containsKey(id) && !epics.containsKey(id) && !subtasks.containsKey(id);
    }

    public int generateUniqueId() {
        return nextId++;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = null;
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            task = subtasks.get(id);
        } else if (epics.containsKey(id)) {
            task = epics.get(id);
        }
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
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

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        if (task instanceof Epic) {
            updateEpic((Epic) task);
        } else if (task instanceof Subtask) {
            updateSubtask((Subtask) task);
        } else {
            updateSimpleTask(task);
        }
    }

    private void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    private void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getParentEpic());
    }

    private void updateSimpleTask(Task task) {
        tasks.put(task.getId(), task);
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

    @Override
    public void deleteTaskOnId(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            deleteSubtask(id);
        } else if (epics.containsKey(id)) {
            deleteEpic(id);
        }
    }

    private void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        Epic parentEpic = subtask.getParentEpic();
        if (parentEpic != null) {
            parentEpic.getSubtasks().remove(subtask);
            updateEpicStatus(parentEpic);
        }
    }

    private void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        for (Subtask subtask : epic.getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epic.getSubtasks().clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
