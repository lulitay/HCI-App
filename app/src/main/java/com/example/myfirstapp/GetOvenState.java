package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class GetOvenState {
    private OvenState result;

    public GetOvenState(OvenState result) {
        this.result = result;
    }

    public void setResult(OvenState result) {
        this.result = result;
    }

    public OvenState getResult(){
        return result;
    }
}
