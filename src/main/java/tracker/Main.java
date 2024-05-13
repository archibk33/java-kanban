package main.java.tracker;

import main.java.tracker.managers.InMemoryTaskManager;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        /**
         * Создание и добавление нового эпика, а так же подзадач в этот эпик
         */
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        epic1.setId(taskManager.generateUniqueId());
        taskManager.addNewTask(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1);
        subtask1.setId(taskManager.generateUniqueId());
        taskManager.addNewTask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, epic1);
        subtask2.setId(taskManager.generateUniqueId());
        taskManager.addNewTask(subtask2);


        /**
         * Создание и добавление задач
         */
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        task1.setId(taskManager.generateUniqueId());
        taskManager.addNewTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        task2.setId(taskManager.generateUniqueId());
        taskManager.addNewTask(task2);

        /**
         * Получение задач по ID и история просмотров
         */
        Task retrievedTask = taskManager.getTaskById(task1.getId());
        System.out.println("Получена задача по ID: " + retrievedTask);
        System.out.println("История просмотров: " + taskManager.getHistory());

        /**
         * Обновление задачи и история просмотров
         */
        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);
        System.out.println("Обновлена задача: " + task1);
        System.out.println("История просмотров: " + taskManager.getHistory());

        /**
         * Удаление задачи по ID и история просмотров
         */
        taskManager.deleteTaskOnId(task2.getId());
        System.out.println("Удалена задача с ID: " + task2.getId());
        System.out.println("История просмотров: " + taskManager.getHistory());

        /**
         * Получение всех задач и история просмотров
         */
        List<Task> allTasks = taskManager.getAllTasks();
        System.out.println("Все задачи: " + allTasks);
        System.out.println("История просмотров: " + taskManager.getHistory());

        /**
         * Получение подзадач эпика по ID и история просмотров
         */
        List<Subtask> subtasksOfEpic = taskManager.getSubtasksByEpic(epic1.getId());
        System.out.println("Подзадачи эпика: " + subtasksOfEpic);
        System.out.println("История просмотров: " + taskManager.getHistory());
    }
}
