package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class GetStatus {
    private Status result;

    public GetStatus(Status result){
        this.result = result;
    }

    public void setResult(Status result){
        this.result = result;
    }

    public Status getResult(){
        return this.result;
    }
}
