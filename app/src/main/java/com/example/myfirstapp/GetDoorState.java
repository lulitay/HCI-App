package com.example.myfirstapp;

import java.util.ArrayList;

public class GetDoorState {
    private DoorState result;

    public GetDoorState(DoorState result) {
        this.result = result;
    }

    public void setResult(DoorState result) {
        this.result = result;
    }

    public DoorState getResult(){
        return result;
    }
}
