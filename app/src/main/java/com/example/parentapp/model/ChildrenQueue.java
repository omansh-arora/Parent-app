package com.example.parentapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Keep children flip order.
 * Select child to flip, if there is no child.
 * Put the child to the end after his/her flip.
 */
public class ChildrenQueue {

    private List<Child> children;
    private Child selectedChild;

    public ChildrenQueue() {
        children = LocalStorage.getInstance().getChildrenQueue();
        if (children.size() == 0) {
            children = new ArrayList<>(LocalStorage.getInstance().getChildren());
        }
        selectedChild = LocalStorage.getInstance().getSelectedChild();
    }

    public void setSelectedChild(int index) {
        selectedChild = children.get(index);
        LocalStorage.getInstance().saveSelectedChild(selectedChild);
    }

    public Child getSelectedChild() {
        return selectedChild;
    }

    public void cleanSelection() {
        selectedChild = ChildManager.DEFAULT_CHILD;
        LocalStorage.getInstance().saveSelectedChild(selectedChild);
    }

    private void moveToEnd() {
        if (selectedChild != ChildManager.DEFAULT_CHILD) {
            children.remove(selectedChild);
            children.add(selectedChild);
            LocalStorage.getInstance().saveChildrenQueue(children);
        }
    }

    public Child getNextChild() {
        moveToEnd();
        if (children.size() > 0) {
            setSelectedChild(0);
        }
        return selectedChild;
    }


    public List<Child> getChildren() {
        return children;
    }

}
