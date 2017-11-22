package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class Parametre {
    private ResultString result;

    public Parametre(ResultString result){
        this.result = result;
    }

    public void setResult(ResultString result){
        this.result = result;
    }

    public ResultString getResult(){
        return this.result;
    }
}
