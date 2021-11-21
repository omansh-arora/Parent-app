package com.example.parentapp.model;

public class Task {

    private String name;
    private ChildrenQueue childrenQueue;

    public Task(String name) {
        this.name = name;
        ChildrenQueue queue = new ChildrenQueue(name);
        CoinFlipHistory history = new CoinFlipHistory(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}