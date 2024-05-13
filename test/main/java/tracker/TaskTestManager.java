package main.java.tracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import main.java.tracker.util.Managers;

import java.util.List;


class TaskTestManager {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    /**
     * Подготовка объектов перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    /**
     * Тест на проверку равенства задач с одинаковыми ID.
     */
    @Test
    void tasksShouldBeEqualIfIdIsEqual() {
        Task task1 = new Task("Заголовок задачи 1", "Описание задачи 1", Status.NEW);
        task1.setId(1);
        Task task2 = new Task("Заголовок задачи 2", "Описание задачи 2", Status.NEW);
        task2.setId(1);
        assertEquals(task1, task2);
    }

    /**
     * Тест на проверку, что эпик не может быть своей подзадачей.
     */
    @Test
    void epicCannotBeItsOwnSubtask() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика", Status.NEW);
        try {
            epic.addSubtask(new Subtask("Заголовок подзадачи", "Описание подзадачи", Status.NEW, epic));
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    /**
     * Тест на проверку, что подзадача не может быть добавлена в список подзадач эпика, если она уже связана с этим эпиком.
     */
    @Test
    void subtaskCannotHaveItselfAsEpic() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика", Status.NEW);
        Subtask subtask = new Subtask("Заголовок подзадачи", "Описание подзадачи", Status.NEW, epic);
        try {
            epic.addSubtask(subtask);
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    /**
     * Тест на проверку, что класс Managers возвращает инициализированные экземпляры менеджеров.
     */
    @Test
    void utilityClassShouldReturnInitializedManagers() {
        assertNotNull(historyManager);
        assertNotNull(taskManager);
    }

    /**
     * Тест на проверку, что TaskManager может добавлять задачи и находить их по ID.
     */
    @Test
    void taskManagerShouldAddTasksAndFindThemById() {
        Task task = new Task("Заголовок", "Описание", Status.NEW);
        taskManager.addNewTask(task);
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    /**
     * Тест на проверку, что задачи с заданным и сгенерированным ID не конфликтуют внутри TaskManager.
     */
    @Test
    void tasksWithGivenAndGeneratedIdShouldNotConflict() {
        Task task1 = new Task("Заголовок", "Описание", Status.NEW);
        task1.setId(10);
        taskManager.addNewTask(task1);
        Task task2 = new Task("Заголовок", "Описание", Status.NEW);
        taskManager.addNewTask(task2);
        assertNotEquals(task1.getId(), task2.getId());
    }

    /**
     * Тест на проверку, что задача остается неизменной после добавления в TaskManager.
     */
    @Test
    void taskShouldRemainUnchangedWhenAddedToManager() {
        Task originalTask = new Task("Заголовок", "Описание", Status.NEW);
        Task taskToAdd = new Task("Заголовок", "Описание", Status.NEW);
        taskManager.addNewTask(taskToAdd);
        assertEquals(originalTask.getTitle(), taskToAdd.getTitle());
        assertEquals(originalTask.getDescription(), taskToAdd.getDescription());
        assertEquals(originalTask.getStatus(), taskToAdd.getStatus());
    }

    /**
     * Тест на проверку, что HistoryManager сохраняет версию задачи в момент её добавления.
     */
    @Test
    void historyManagerShouldPreserveTaskVersion() {
        Task task = new Task("Заголовок", "Описание", Status.NEW);
        historyManager.add(task);
        task.setStatus(Status.DONE);
        List<Task> history = historyManager.getHistory();
        assertNotEquals(history.get(0).getStatus(), task.getStatus());
    }

    /**
     * Тест на проверку, что при попытке добавить задачу с уже существующим ID будет выброшено исключение.
     */
    @Test
    void shouldThrowExceptionWhenAddingTaskWithDuplicateId() {
        Task task1 = new Task("Задача 1", "Описание", Status.NEW);
        taskManager.addNewTask(task1);

        Task task2 = new Task("Задача 2", "Описание", Status.NEW);
        task2.setId(task1.getId());

        try {
            taskManager.addNewTask(task2);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Тест на проверку, что у эпика нет подзадач.
     */
    @Test
    void shouldReturnEmptyListWhenNoSubtasksForEpic() {
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        taskManager.addNewTask(epic);
        List<Subtask> subtasks = taskManager.getSubtasksByEpic(epic.getId());
        assertTrue(subtasks.isEmpty());
    }

    /**
     * Тест на проверку, что в истории сохраняется порядок задач.
     */
    @Test
    void shouldPreserveTaskOrderInHistory() {
        Task task1 = new Task("Задача 1", "Описание", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание", Status.NEW);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        List<Task> history = taskManager.getHistory();
        assertEquals(task1.getId(), history.get(0).getId());
        assertEquals(task2.getId(), history.get(1).getId());
    }

    /**
     * Тест на проверку, что две подзадачи считаются равными, если у них одинаковые ID.
     */
    @Test
    void subtasksShouldBeEqualIfIdIsEqual() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика", Status.NEW);
        Subtask subtask1 = new Subtask("Заголовок подзадачи", "Описание подзадачи", Status.NEW, epic);
        Subtask subtask2 = new Subtask("Другой заголовок подзадачи", "Другое описание", Status.DONE, epic);
        assertEquals(subtask1, subtask2);
    }

    /**
     * Тест на проверку, что два эпика считаются равными, если у них одинаковые ID.
     */
    @Test
    void epicsShouldBeEqualIfIdIsEqual() {
        Epic epic1 = new Epic("Заголовок эпика", "Описание эпика", Status.NEW);
        Epic epic2 = new Epic("Другой заголовок эпика", "Другое описание", Status.IN_PROGRESS);
        assertEquals(epic1, epic2);
    }
}
