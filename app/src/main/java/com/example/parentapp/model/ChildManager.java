package com.example.parentapp.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ChildManager implements Iterable<Child> {

    private static ChildManager instance;
    private List<Child> childrenList = new ArrayList<>();
    private int nextChildID = 0;

    public static ChildManager getInstance() {

        if (instance == null) {

            instance = new ChildManager();

        }
        return instance;

    }

    public List<Child> getChildrenList() {
        return childrenList;
    }

    public void addNewChild(Child child) {
        childrenList.add(child);
    }

    public void deleteChild(int childIndex) {
        childrenList.remove(childIndex);
    }


    /**
     * Return next child who has right to choose a side of a flip.
     * Should give each child a fair chance to play alternatively.
     * @return next child to choose a side.
     */
    public Child getNextChild() {
        if (childrenList.size() > 0) {
            return childrenList.get(nextChildID++ % childrenList.size());
        }
        return null;
    }

    @Override
    public Iterator<Child> iterator() {
        return childrenList.iterator();
    }


}
