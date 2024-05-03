package main.java.tracker;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создание элементов TaskManager
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        task1.setId(taskManager.generateUniqueId());
        taskManager.addNewTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        task2.setId(taskManager.generateUniqueId());
        taskManager.addNewTask(task2);
        Epic epic1 = new Epic(taskManager.generateUniqueId(), "Эпик 1", "Описание эпика 1", Status.NEW);
        Subtask subtask1 = new Subtask(taskManager.generateUniqueId(), "Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1);
        Subtask subtask2 = new Subtask(taskManager.generateUniqueId(), "Подзадача 2", "Описание подзадачи 2", Status.NEW, epic1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(subtask1);
        taskManager.addNewTask(subtask2);
        Epic epic2 = new Epic(taskManager.generateUniqueId(), "Эпик 2", "Описание эпика 2", Status.NEW);
        Subtask subtask3 = new Subtask(taskManager.generateUniqueId(), "Подзадача 3", "Описание подзадачи 3", Status.NEW, epic2);
        taskManager.addNewTask(epic2);
        taskManager.addNewTask(subtask3);

        // Распечатка списков задач, задач по ID, подзадач эпика
        System.out.println("Задачи: " + taskManager.getAllTasks());
        System.out.println("Эпики: " + taskManager.getAllEpics());
        System.out.println("Подзадачи: " + taskManager.getAllSubtasks());
        System.out.println("Задача 1 по id: " + taskManager.getTaskOnId(task1.getId()));
        System.out.println("Задача 2 по id: " + taskManager.getTaskOnId(task2.getId()));
        System.out.println("Подзадачи эпика 1: " + taskManager.getSubtasksOnEpic(epic1.getId()));
        System.out.println("Подзадачи эпика 2: " + taskManager.getSubtasksOnEpic(epic2.getId()));

        // Изменение статусов и проверка
        task1.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.DONE);
        taskManager.updateTask(task1);
        taskManager.updateTask(subtask1);
        taskManager.updateTask(subtask2);
        taskManager.updateTask(subtask3);

        // Распечатка статусов после изменений статуса
        System.out.println("Измененные задачи: " + taskManager.getAllTasks());
        System.out.println("Измененные эпики: " + taskManager.getAllEpics());
        System.out.println("Измененные подзадачи: " + taskManager.getAllSubtasks());

        // Удаление задачи, эпика по ID
        taskManager.deleteTaskOnId(task2.getId());
        taskManager.deleteTaskOnId(epic2.getId());


        // Распечатка списков после удаления
        System.out.println("Списки после удаления:");
        System.out.println("Задачи: " + taskManager.getAllTasks());
        System.out.println("Эпики: " + taskManager.getAllEpics());
        System.out.println("Подзадачи: " + taskManager.getAllSubtasks());

        // Удаление всех списков
        taskManager.deleteAllTasks();
        System.out.println("Списки после удаления всех задач:");
        System.out.println("Задачи: " + taskManager.getAllTasks());
        System.out.println("Эпики: " + taskManager.getAllEpics());
        System.out.println("Подзадачи: " + taskManager.getAllSubtasks());
    }
}
