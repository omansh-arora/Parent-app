package com.example.parentapp.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalStorage {

    private static LocalStorage instance;
    private final Context appContext;

    private static final String PREFS_TASKS = "tasks";
    private static final String PREFS_HISTORIES = "histories";
    private static final String PREFS_QUEUES = "queues";
    private static final String PREFS_SELECTED_CHILDREN = "selected_children";
    private static final String PREFS_CHILDREN = "children";
    private static final String PREFS_BREATHS_NUM = "breathsNum";

    private List<String> taskNames;
    private Map<String, Child> selected_children;
    private Map<String, List<Child>> queues;
    private Map<String, List<CoinFlip>> histories;

    private LocalStorage(Context applicationContext) {
        this.appContext = applicationContext;
        loadTasks();
        loadHistories();
        loadQueues();
        loadSelectedChildren();
    }

    private void loadSelectedChildren() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_SELECTED_CHILDREN, MODE_PRIVATE);
        String json = prefs.getString(PREFS_SELECTED_CHILDREN, "");
        selected_children = new Gson().fromJson(json, new TypeToken<Map<String, Child>>() {
        }.getType());
        if (selected_children == null) selected_children = new HashMap<String, Child>();
    }

    private void loadQueues() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_QUEUES, MODE_PRIVATE);
        String json = prefs.getString(PREFS_QUEUES, "");
        queues = new Gson().fromJson(json, new TypeToken<Map<String, List<Child>>>() {
        }.getType());
        if (queues == null) queues = new HashMap<String, List<Child>>();
    }

    private void loadHistories() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_HISTORIES, MODE_PRIVATE);
        String json = prefs.getString(PREFS_HISTORIES, "");
        histories = new Gson().fromJson(json, new TypeToken<Map<String, List<CoinFlip>>>() {
        }.getType());
        if (histories == null) histories = new HashMap<String, List<CoinFlip>>();
    }

    private void loadTasks() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_TASKS, MODE_PRIVATE);
        String json = prefs.getString(PREFS_TASKS, "");
        taskNames = new Gson().fromJson(json, new TypeToken<List<String>>() {
        }.getType());
        if (taskNames == null) taskNames = new ArrayList<>();
    }

    public static LocalStorage getInstance(Context applicationContext) {
        if (instance == null) {
            instance = new LocalStorage(applicationContext);
        }
        return instance;
    }

    public static LocalStorage getInstance() {
        return instance;
    }

    public List<CoinFlip> getHistory(String taskName) {
        if (!histories.containsKey(taskName)) {
            saveHistory(taskName, new ArrayList<>());
        }
        return histories.get(taskName);
    }

    private void saveHistories() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_HISTORIES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(histories);
        editor.putString(PREFS_HISTORIES, json);
        editor.commit();
    }

    public void saveHistory(String taskName, List<CoinFlip> history) {
        histories.put(taskName, history);
        saveHistories();
    }

    public List<Child> getChildren() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_CHILDREN, MODE_PRIVATE);
        String json = prefs.getString(PREFS_CHILDREN, "");
        List<Child> loaded = new Gson().fromJson(json, new TypeToken<List<Child>>() {
        }.getType());
        if (loaded == null) return new ArrayList<>();
        return loaded;
    }

    public void saveChildren(List<Child> children) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_CHILDREN, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(children);
        editor.putString(PREFS_CHILDREN, json);
        editor.commit();
    }

    public List<Child> getChildrenQueue(String taskName) {
        if (!queues.containsKey(taskName)) {
            saveChildrenQueue(taskName, getChildren());
        }
        return queues.get(taskName);
    }

    public void saveQueues() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_QUEUES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(queues);
        editor.putString(PREFS_QUEUES, json);
        editor.commit();
    }

    public void saveChildrenQueue(String taskName, List<Child> children) {
        queues.put(taskName, children);
        saveQueues();
    }

    public Child getSelectedChild(String taskName) {
        if (!selected_children.containsKey(taskName)) {
            saveSelectedChild(taskName, ChildManager.DEFAULT_CHILD);
        }

        //hotfix
        if (selected_children.get(taskName).getName().equals("DEFAULT")){
            return getChildren().get(0);
        }

        return selected_children.get(taskName);
    }

    private void saveSelectedChildren() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_SELECTED_CHILDREN, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(selected_children);
        editor.putString(PREFS_SELECTED_CHILDREN, json);
        editor.commit();
    }

    public void saveSelectedChild(String taskName, Child child) {
        selected_children.put(taskName, child);
        saveSelectedChildren();
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (String taskName : taskNames) {
            Task task = new Task(taskName);
            tasks.add(task);
        }
        return tasks;
    }

    private void saveTaskNames() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_TASKS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskNames);
        editor.putString(PREFS_TASKS, json);
        editor.commit();
    }

    public void saveTasks(List<Task> tasks) {
        taskNames = new ArrayList<>();
        for (Task task : tasks){
            taskNames.add(task.getName());
        }
        saveTaskNames();
    }

    public void deleteTask(String taskName){
        histories.remove(taskName);
        saveHistories();
        queues.remove(taskName);
        saveQueues();
        selected_children.remove(taskName);
        saveSelectedChildren();
        taskNames.remove(taskName);
        saveTaskNames();
    }

    public void addNewChild(Child child) {
        for (String taskName : queues.keySet()) {
            queues.get(taskName).add(child);
        }
        saveQueues();
        List<Child> children = getChildren();
        children.add(child);
        saveChildren(children);
    }

    public void removeChild(Child child) {
        for (String taskName : queues.keySet()) {
            queues.get(taskName).remove(child);
        }
        saveQueues();
        List<Child> children = getChildren();
        children.remove(child);
        saveChildren(children);
    }


    public void saveBreaths(String breathsNum) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_BREATHS_NUM, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_BREATHS_NUM,breathsNum);
        editor.apply();
    }

    public String getBreaths() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_BREATHS_NUM, MODE_PRIVATE);
        String breaths = prefs.getString(PREFS_BREATHS_NUM, "1");
        return breaths;
    }
}
