package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class GetAcState {
    private AcState result;

    public GetAcState(AcState result) {
        this.result = result;
    }

    public void setResult(AcState result) {
        this.result = result;
    }

    public AcState getResult(){
        return result;
    }
}
