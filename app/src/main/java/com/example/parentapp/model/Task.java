package com.example.parentapp.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}