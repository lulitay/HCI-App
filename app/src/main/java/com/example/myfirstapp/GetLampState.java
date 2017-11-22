package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class GetLampState {

    private LampState result;

    public GetLampState(LampState result) {
        this.result = result;
    }

    public void setResult(LampState result) {
        this.result = result;
    }

    public LampState getResult(){
        return result;
    }
}
