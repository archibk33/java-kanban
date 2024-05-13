package main.java.tracker.util;

import main.java.tracker.HistoryManager;
import main.java.tracker.TaskManager;
import main.java.tracker.managers.InMemoryHistoryManager;
import main.java.tracker.managers.InMemoryTaskManager;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

}
