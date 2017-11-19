package com.example.myfirstapp;

/**
 * Created by Ariel on 16/11/2017.
 */

public class Device {
    private String name;
    private String type;

    public Device(String name, String type){
        this.name = name;
        this.type = type;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }
}
