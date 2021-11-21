package com.example.parentapp.model;

import java.util.List;

public class TaskManager {

    public static final Task DEFAULT_TASK = new Task("DEFAULT");
    List<Task> tasks;

    public TaskManager() {
        this.tasks = LocalStorage.getInstance().getTasks();
    }

    public void addNewTask(Task task) {
        tasks.add(task);
        LocalStorage.getInstance().saveTasks(tasks);
    }

    public void deleteTask(int taskIndex) {
        tasks.remove(taskIndex);
        //LocalStorage.getInstance().saveChildren(children);
    }

    public List<Task> getTasks() {
        this.tasks = LocalStorage.getInstance().getTasks();
        return tasks;
    }

}
