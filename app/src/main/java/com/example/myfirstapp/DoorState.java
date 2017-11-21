package com.example.myfirstapp;

import java.util.ArrayList;

public class DoorState {
    private String status;
    private String lock;

    public DoorState(String status, String lock) {

        this.status = status;
        this.lock = lock;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getStatus(){
        return status;
    }

    public String getLock(){
        return lock;
    }
}
