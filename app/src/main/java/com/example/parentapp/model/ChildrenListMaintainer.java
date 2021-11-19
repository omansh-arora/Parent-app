package com.example.parentapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ChildrenListMaintainer {

    private static ChildrenListMaintainer instance;
    private List<Child> childrenList = new ArrayList<>();
    private List<Child> sortedChildrenList = new ArrayList<>();
    private List<Child> newChildrenList = new ArrayList<>();
    private int nextChildID = 0;
    private Child defaultChild;

    public ChildrenListMaintainer(List<Child> list) {
        this.childrenList.clear();
        this.childrenList.addAll(list);

        if (childrenList.size() > 0) {
            defaultChild = this.childrenList.get(0);
        }
    }

    public Child getDefaultChild() {
        return defaultChild;
    }


    public Child getNextChild() {
        if (childrenList.size() > 0) {
            return childrenList.get(nextChildID++ % childrenList.size());
        }
        return null;
    }

    // sort children list from default(first/top) to previous/last played
    //This list is used to display in the popup window
    public void sortChildrenListByDefaultTop() {

        sortedChildrenList.clear();
        //if nextChildID is at index 0, the list stays it's order
        if (nextChildID == 0) {
            sortedChildrenList.addAll(childrenList);
            return;
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

        //update childrenlist with sorted display children order
        setChildrenList(sortedChildrenList);
    }

    // move the selected override child to top of the list
    public void setOverrideChildrenList(int index) {
        newChildrenList.clear();
        newChildrenList.addAll(childrenList);
        //Log.i("newChildrenList size", String.valueOf(newChildrenList.size()));

        Child selectedChildTem = newChildrenList.get(index);
        newChildrenList.remove(index);
        newChildrenList.add(0, selectedChildTem);

        //update childrenlist with override children order
        setChildrenList(newChildrenList);

        Log.i("setOverrideChildrenList newChildrenList", "index 0: " + newChildrenList.get(0).getName() + "  index 1: " + newChildrenList.get(1).getName() + "  index 2: " + newChildrenList.get(2).getName());
    }

    public List<Child> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<Child> newList) {
        this.childrenList.clear();
        this.childrenList.addAll(newList);
    }



}
