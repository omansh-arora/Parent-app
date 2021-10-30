package com.example.parentapp.model;

import java.util.ArrayList;
import java.util.List;

public class ChildManager {

    private static ChildManager instance;
    private List<Child> children = new ArrayList<>();

    public static ChildManager getInstance() {

        if (instance == null) {

            instance = new ChildManager();

        }
        return instance;

    }

    /**
     * Return next child who has right to choose a side of a flip.
     * Should give each child a fair chance to play alternatively.
     * @return next child to choose a side.
     */
    public Child getNextChild() {
        return null;
    }
}
