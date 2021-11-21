package com.example.parentapp.model;

import java.util.Iterator;
import java.util.List;

public class ChildManager implements Iterable<Child> {

    public static final Child DEFAULT_CHILD = new Child("DEFAULT", 0, "");
    private List<Child> children;

    public ChildManager() {
        children = LocalStorage.getInstance().getChildren();
    }

    public List<Child> getChildren() {
        return children;
    }

    public void addNewChild(Child child) {
        children.add(child);
        LocalStorage.getInstance().addNewChild(child);
    }

    public void deleteChild(int childIndex) {
        children.remove(childIndex);
        LocalStorage.getInstance().saveChildren(children);
        // TODO: remove child from all existing queues, and selectedChildren
    }

    @Override
    public Iterator<Child> iterator() {
        return children.iterator();
    }

    public void saveChildren() {
        LocalStorage.getInstance().saveChildren(children);
    }
}
