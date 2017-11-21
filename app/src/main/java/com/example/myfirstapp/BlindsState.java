package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class BlindsState {

    private String status;

    public BlindsState(String status) {

        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

}
