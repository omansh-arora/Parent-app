package com.example.parentapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ChildManager implements Iterable<Child> {

    private static ChildManager instance;
    private List<Child> childrenList = new ArrayList<>();
    private List<Child> sortedChildrenList = new ArrayList<>();
    private List<Child> newChildrenList = new ArrayList<>();
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


    // sort children list by default. default would be first (top), pre played would be last
    public List<Child> getSortedChildrenList() {

        sortedChildrenList.clear();
        if (nextChildID == 0) {
            sortedChildrenList = childrenList;
            return sortedChildrenList;
        }

        int i = nextChildID;
        while (i < childrenList.size()) {
            sortedChildrenList.add(childrenList.get(i));
            i++;
        }

        i = 0;
        while (i < nextChildID) {
            sortedChildrenList.add(childrenList.get(i));
            i++;
        }

        return sortedChildrenList;
    }


    public void setOverrideChildrenList(int index) {
        newChildrenList.clear();
        newChildrenList.addAll(getSortedChildrenList());
        //Log.i("newChildrenList size", String.valueOf(newChildrenList.size()));

        Child selectedChildTem = newChildrenList.get(index);
        newChildrenList.remove(index);
        newChildrenList.add(0,selectedChildTem);

        //Log.i("setOverrideChildrenList newChildrenList", "index 0: " + newChildrenList.get(0).getName() + "  index 1: " + newChildrenList.get(1).getName() + "  index 2: " + newChildrenList.get(2).getName());
    }

    public List<Child> getOverrideChildrenList() {
        return newChildrenList;
    }

    public void setNewChildrenList(List<Child> newList) {
        newChildrenList = newList;
    }

    public void addNewChild(Child child) {
        childrenList.add(child);
    }

    public void editChild(int index, Child child) {
        childrenList.set(index, child);
    }

    public void deleteChild(int childIndex) {
        childrenList.remove(childIndex);
    }

    public void setChildrenList(List<Child> childrenBB) {
        childrenList = childrenBB;
    }

    /**
     * Return next child who has right to choose a side of a flip.
     * Should give each child a fair chance to play alternatively.
     *
     * @return next child to choose a side.
     */
    public Child getNextChild() {

        if (newChildrenList.size() == 0) {
            Log.i("newChildrenList hasn't assigned:  ", "True");
            newChildrenList.addAll(getSortedChildrenList());
        }

        Log.i("nextChildID (index): ", newChildrenList.get(nextChildID).getName() + " Index: " + String.valueOf(nextChildID));

        if (newChildrenList.size() > 0) {
            return newChildrenList.get(nextChildID);
        }
        return null;
    }

    public void setChildID() {

        if (nextChildID < childrenList.size() - 1) {
            nextChildID++;
            return;
        }
        nextChildID = 0;
    }

    @Override
    public Iterator<Child> iterator() {
        return childrenList.iterator();
    }


}
