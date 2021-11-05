package com.example.parentapp.model;

public class Child {

    private String name;
    private Integer age;

    public Child(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {

        String output = "";
        output += "Child Name: " + getName();
        return output;
    }
}
