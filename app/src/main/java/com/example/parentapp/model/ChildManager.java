package com.example.parentapp.model;

import android.net.Uri;
import java.util.ArrayList;
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
        LocalStorage.getInstance().removeChild(children.get(childIndex));
        children.remove(childIndex);

        //LocalStorage.getInstance().saveChildren(children);
        // TODO: remove child from all existing queues, and selectedChildren
    }

    public void setChildPic(int index, String uri){

        children.get(index).setPicture(uri);

    }
    public String getChildPic(int index){

       return children.get(index).getPicture();

    }


    @Override
    public Iterator<Child> iterator() {
        return children.iterator();
    }

    public void saveChildren() {
        LocalStorage.getInstance().saveChildren(children);
    }

    public void updateChild(Child child,String name, Integer age, String gender, String imgURI) {
        //remove the child from Local Storage
        child.setName(name);
        child.setAge(age);
        child.setGender(gender);
        child.setPicture(imgURI);
        LocalStorage.getInstance().saveChildren(getChildren());
    }

}
