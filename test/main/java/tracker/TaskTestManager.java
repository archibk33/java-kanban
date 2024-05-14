package main.java.tracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import main.java.tracker.util.Managers;

import java.lang.reflect.Executable;
import java.util.List;

class TaskTestManager {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    @DisplayName("Подготовка объектов перед каждым тестом")
    void setUp() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    @DisplayName("Проверка равенства задач с одинаковыми ID")
    void tasksShouldBeEqualIfIdIsEqual() {
        Task task1 = new Task("Заголовок задачи 1", "Описание задачи 1", Status.NEW);
        task1.setId(1);
        Task task2 = new Task("Заголовок задачи 2", "Описание задачи 2", Status.NEW);
        task2.setId(1);
        assertEquals(task1, task2);
    }

    @Test
    @DisplayName("Эпик не может быть своей подзадачей")
    void epicCannotBeItsOwnSubtask() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика", Status.NEW);
        try {
            epic.addSubtask(new Subtask("Заголовок подзадачи", "Описание подзадачи", Status.NEW, epic));
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    @DisplayName("Подзадача не может быть добавлена в список подзадач эпика, если она уже связана с этим эпиком")
    void subtaskCannotHaveItselfAsEpic() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика", Status.NEW);
        Subtask subtask = new Subtask("Заголовок подзадачи", "Описание подзадачи", Status.NEW, epic);
        try {
            epic.addSubtask(subtask);
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    @DisplayName("Managers возвращает инициализированные экземпляры менеджеров")
    void utilityClassShouldReturnInitializedManagers() {
        assertNotNull(historyManager);
        assertNotNull(taskManager);
    }

    @Test
    @DisplayName("TaskManager может добавлять задачи и находить их по ID")
    void taskManagerShouldAddTasksAndFindThemById() {
        Task task = new Task("Заголовок", "Описание", Status.NEW);
        taskManager.addNewTask(task);
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    @DisplayName("Задачи с заданным и сгенерированным ID не конфликтуют внутри TaskManager")
    void tasksWithGivenAndGeneratedIdShouldNotConflict() {
        Task task1 = new Task("Заголовок", "Описание", Status.NEW);
        task1.setId(10);
        taskManager.addNewTask(task1);
        Task task2 = new Task("Заголовок", "Описание", Status.NEW);
        taskManager.addNewTask(task2);
        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    @DisplayName("Задача остается неизменной после добавления в TaskManager")
    void taskShouldRemainUnchangedWhenAddedToManager() {
        Task originalTask = new Task("Заголовок", "Описание", Status.NEW);
        Task taskToAdd = new Task("Заголовок", "Описание", Status.NEW);
        taskManager.addNewTask(taskToAdd);
        assertEquals(originalTask.getTitle(), taskToAdd.getTitle());
        assertEquals(originalTask.getDescription(), taskToAdd.getDescription());
        assertEquals(originalTask.getStatus(), taskToAdd.getStatus());
    }

    @Test
    @DisplayName("HistoryManager сохраняет версию задачи в момент её добавления")
    void historyManagerShouldPreserveTaskVersion() {
        Task task = new Task("Заголовок", "Описание", Status.NEW);
        historyManager.add(task);
        task.setStatus(Status.DONE);
        List<Task> history = historyManager.getHistory();
        assertNotEquals(history.get(0).getStatus(), task.getStatus());
    }

    @Test
    @DisplayName("При попытке добавить задачу с уже существующим ID выбрасывается исключение")
    void shouldThrowExceptionWhenAddingTaskWithDuplicateId() {
        Task task1 = new Task("Задача 1", "Описание", Status.NEW);
        taskManager.addNewTask(task1);
        Task task2 = new Task("Задача 2", "Описание", Status.NEW);
        task2.setId(task1.getId());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           taskManager.addNewTask(task2);
        });
        String expectedMessage = "Задача с таким ID уже существует";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }



    @Test
    @DisplayName("У эпика нет подзадач")
    void shouldReturnEmptyListWhenNoSubtasksForEpic() {
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        taskManager.addNewTask(epic);
        List<Subtask> subtasks = taskManager.getSubtasksByEpic(epic.getId());
        assertTrue(subtasks.isEmpty());
    }

    @Test
    @DisplayName("В истории сохраняется порядок задач")
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

    @Test
    @DisplayName("Две подзадачи считаются равными, если у них одинаковые ID")
    void subtasksShouldBeEqualIfIdIsEqual() {
        Epic epic = new Epic("Заголовок эпика", "Описание эпика", Status.NEW);
        Subtask subtask1 = new Subtask("Заголовок подзадачи", "Описание подзадачи", Status.NEW, epic);
        Subtask subtask2 = new Subtask("Другой заголовок подзадачи", "Другое описание", Status.DONE, epic);
        assertEquals(subtask1, subtask2);
    }

    @Test
    @DisplayName("Два эпика считаются равными, если у них одинаковые ID")
    void epicsShouldBeEqualIfIdIsEqual() {
        Epic epic1 = new Epic("Заголовок эпика", "Описание эпика", Status.NEW);
        Epic epic2 = new Epic("Другой заголовок эпика", "Другое описание", Status.IN_PROGRESS);
        assertEquals(epic1, epic2);
    }
}
