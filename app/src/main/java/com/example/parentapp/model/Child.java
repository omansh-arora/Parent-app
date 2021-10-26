package com.example.parentapp.model;

public class Child {

    private static String name;

    public Child(String x) {
        name = x;
    }

    public String getName(){

        return name;

    }

    public void setName(String x){

        name = x;

    }
}
