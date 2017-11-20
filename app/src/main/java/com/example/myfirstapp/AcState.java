package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class AcState {

    private String status;
    private int temperature;
    private String mode;
    private String verticalSwing;
    private String horizontalSwing;
    private String fanSpeed;

    public AcState(String status, int temperature, String mode, String verticalSwing, String horizontalSwing, String fanSpeed) {

        this.status = status;
        this.temperature = temperature;
        this.mode = mode;
        this.verticalSwing = verticalSwing;
        this.horizontalSwing = horizontalSwing;
        this.fanSpeed = fanSpeed;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public void setVerticalSwing(String verticalSwing) {
        this.verticalSwing = verticalSwing;
    }
    public void setHorizontalSwing(String horizontalSwing) {
        this.horizontalSwing = horizontalSwing;
    }
    public void setFanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public String getStatus(){
        return status;
    }
    public int getTemperature(){
        return temperature;
    }
    public String getMode(){
        return mode;
    }
    public String getVerticalSwing(){
        return verticalSwing;
    }
    public String getHorizontalSwing(){
        return horizontalSwing;
    }
    public String getFanSpeed(){
        return fanSpeed;
    }

}
