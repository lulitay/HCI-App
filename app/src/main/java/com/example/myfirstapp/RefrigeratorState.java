package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class RefrigeratorState {
    private int freezerTemperature;
    private int temperature;
    private String mode;

    public RefrigeratorState(int freezerTemperature, int temperature, String mode) {

        this.freezerTemperature = freezerTemperature;
        this.temperature = temperature;
        this.mode = mode;
    }

    public void setFreezerTemperature(int freezerTemperature) {
        this.freezerTemperature = freezerTemperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setMode(String mode){
        this.mode = mode;
    }

    public int getFreezerTemperature(){
        return freezerTemperature;
    }

    public int getTemperature(){
        return temperature;
    }

    public String getMode(){
        return mode;
    }
}
