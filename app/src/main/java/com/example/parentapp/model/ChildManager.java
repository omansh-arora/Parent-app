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


}
