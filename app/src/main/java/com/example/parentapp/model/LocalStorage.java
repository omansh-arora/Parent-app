package com.example.parentapp.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class LocalStorage {

    private static LocalStorage instance;
    private final Context appContext;

    private static final String PREFS_HISTORY = "history";
    private static final String PREFS_CHILDREN = "children";
    private static final String PREFS_CHILDREN_QUEUE = "queue";
    private static final String PREFS_SELECTED_CHILD = "selected_child";

    private LocalStorage(Context applicationContext) {
        this.appContext = applicationContext;
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

    public List<CoinFlip> getHistory() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_HISTORY, MODE_PRIVATE);
        String json = prefs.getString(PREFS_HISTORY, "");
        List<CoinFlip> loaded = new Gson().fromJson(json, new TypeToken<List<CoinFlip>>(){}.getType());
        if (loaded == null) return new ArrayList<>();
        return loaded;
    }

    public void saveHistory(List<CoinFlip> history) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_HISTORY, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(history);
        editor.putString(PREFS_HISTORY, json);
        editor.commit();
    }

    public List<Child> getChildren() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_CHILDREN, MODE_PRIVATE);
        String json = prefs.getString(PREFS_CHILDREN, "");
        List<Child> loaded = new Gson().fromJson(json, new TypeToken<List<Child>>(){}.getType());
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

    public List<Child> getChildrenQueue() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_CHILDREN_QUEUE, MODE_PRIVATE);
        String json = prefs.getString(PREFS_CHILDREN_QUEUE, "");
        List<Child> loaded = new Gson().fromJson(json, new TypeToken<List<Child>>(){}.getType());
        if (loaded == null) return new ArrayList<>();
        return loaded;
    }

    public void saveChildrenQueue(List<Child> children) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_CHILDREN_QUEUE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(children);
        editor.putString(PREFS_CHILDREN_QUEUE, json);
        editor.commit();
    }

    public Child getSelectedChild() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_SELECTED_CHILD, MODE_PRIVATE);
        String json = prefs.getString(PREFS_SELECTED_CHILD, "");
        Child loaded = new Gson().fromJson(json, Child.class);
        if (loaded == null) return ChildManager.DEFAULT_CHILD;
        return loaded;
    }

    public void saveSelectedChild(Child child) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_SELECTED_CHILD, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(child);
        editor.putString(PREFS_SELECTED_CHILD, json);
        editor.commit();
    }
}
