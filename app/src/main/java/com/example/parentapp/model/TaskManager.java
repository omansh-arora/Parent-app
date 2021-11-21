package com.example.parentapp.model;

import java.util.List;

public class TaskManager {

    public static final Task DEFAULT_TASK = new Task("DEFAULT");
    List<Task> tasks;

    public TaskManager() {
        this.tasks = LocalStorage.getInstance().getTasks();
    }

    public void addNewTask(String taskName) {
        tasks.add(new Task(taskName));
        LocalStorage.getInstance().saveTasks(tasks);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
        LocalStorage.getInstance().deleteTask(task.getName());
    }

    public void deleteTask(String taskName) {
        tasks.remove(new Task(taskName));
        LocalStorage.getInstance().deleteTask(taskName);
    }

    public List<Task> getTasks() {
        this.tasks = LocalStorage.getInstance().getTasks();
        return tasks;
    }

    public void renameTask(String oldName, String newName) {
        List<Child> oldChildrenQueue = LocalStorage.getInstance().getChildrenQueue(oldName);
        List<CoinFlip> history = LocalStorage.getInstance().getHistory(oldName);
        Child selectedChild = LocalStorage.getInstance().getSelectedChild(oldName);
        LocalStorage.getInstance().saveChildrenQueue(newName,oldChildrenQueue);
        LocalStorage.getInstance().saveHistory(newName,history);
        LocalStorage.getInstance().saveSelectedChild(newName, selectedChild);
        deleteTask(oldName);
        addNewTask(newName);
    }
}
