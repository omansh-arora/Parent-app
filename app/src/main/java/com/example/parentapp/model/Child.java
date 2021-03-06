package com.example.parentapp.model;

import android.net.Uri;

import java.util.Objects;

public class Child {

    private String name;
    private Integer age;
    private String gender;
    private String picture;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Child(String name, Integer age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
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

    public void setPicture(String pic) {
        picture = pic;
    }

    public String getPicture() {
        return picture;
    }

    @Override
    public String toString() {

        String output = "";
        output += "Child Name: " + getName() + "  Gender: " + getGender() + "  Age: " + getAge();
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Child)) return false;
        Child child = (Child) o;
        return name.equals(child.name) && age.equals(child.age) && gender.equals(child.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, gender);
    }
}
