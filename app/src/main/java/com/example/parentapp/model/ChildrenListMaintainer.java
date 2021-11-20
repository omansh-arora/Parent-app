package com.example.parentapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ChildrenListMaintainer {

    private static ChildrenListMaintainer instance;
    private List<Child> childrenList;
    private int nextChildID = 0;
    private Child selectedChild;
    public static final Child DEFAULT_CHILD = ChildManager.DEFAULT_CHILD;

    public ChildrenListMaintainer(List<Child> list) {
        this.childrenList = new ArrayList<>(list);

        if (childrenList.size() > 0) {
            selectedChild = this.childrenList.get(0);
        } else {
            selectedChild = DEFAULT_CHILD;
        }
    }

    public void setSelectedChild(int index) {
        this.selectedChild = childrenList.get(index);
    }


    public Child getSelectedChild() {

        return selectedChild;
    }



    public Child getNextChild() {
        if (childrenList.size() > 0) {
            return childrenList.get(nextChildID++ % childrenList.size());
        }
        return DEFAULT_CHILD;
    }


    public List<Child> getChildrenList() {
        return childrenList;
    }



}
