package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class LampState {
    private String status;
    private String color;
    private int brightness;

    public LampState(String status, String color, int brightness) {

        this.status = status;
        this.color = color;
        this.brightness = brightness;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public String getStatus(){
        return status;
    }

    public String getColor(){
        return color;
    }

    public int getBrightness(){
        return brightness;
    }

}
