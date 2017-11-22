package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class GetRefrigeratorState {
    private RefrigeratorState result;

    public GetRefrigeratorState(RefrigeratorState result) {
        this.result = result;
    }

    public void setResult(RefrigeratorState result) {
        this.result = result;
    }

    public RefrigeratorState getResult(){
        return result;
    }
}
