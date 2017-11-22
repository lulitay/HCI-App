package com.example.myfirstapp;

/**
 * Created by Ariel on 16/11/2017.
 */

public class Device {
    private String id;
    private String name;
    private String typeId;
    private String meta;
    private boolean notification = false;

    public Device(String id, String name, String typeId, String meta){
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.meta = meta;
    }

    public String getName(){
        return name;
    }

    public boolean getNotState(){
        return notification;
    }

    public void setNotState(boolean b){
        notification = b;
    }

    public String getType(){
        return typeId;
    }

    public String getId() {
        return id;
    }

    public String getMeta(){
        return meta;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setType(String typeId) {
        this.typeId = typeId;
    }
    public void setMeta(String meta) {
        this.meta = meta;
    }

}
