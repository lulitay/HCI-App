package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class GetBlindsState {

    private BlindsState result;

    public GetBlindsState(BlindsState result) {
        this.result = result;
    }

    public void setResult(BlindsState result) {
        this.result = result;
    }

    public BlindsState getResult(){
        return result;
    }
}
