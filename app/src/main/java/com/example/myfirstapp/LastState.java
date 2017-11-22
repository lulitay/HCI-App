package com.example.myfirstapp;

/**
 * Created by Ariel on 22/11/2017.
 */

public class LastState {
    private boolean status;
    private boolean lock;
    private int temperature;
    private int freezertemperature;
    private int brightness;
    private String firstSpinner;
    private String secondSpinner;
    private String thirdSpinner;
    private String forthSpinner;

    public LastState(){

    }

    public boolean getStatus(){
        return status;
    }
    public boolean getLock(){
        return lock;
    }
    public int getTemperature(){
        return temperature;
    }
    public int getFreezertemperature(){
        return freezertemperature;
    }
    public int getBrightness(){
        return brightness;
    }
    public String getFirstSpinner(){
        return firstSpinner;
    }
    public String getSecondSpinner(){
        return secondSpinner;
    }
    public String getThirdSpinner(){
        return thirdSpinner;
    }
    public String getForthSpinner(){
        return forthSpinner;
    }

    public void setStatus(boolean status){
        this.status = status;
    }
    public void setLock(boolean lock){
        this.lock = lock;
    }
    public void setTemperature(int temperature){
        this.temperature = temperature;
    }
    public void setFreezertemperatureTemperature(int freezertemperature){
        this.freezertemperature = freezertemperature;
    }
    public void setBrightness(int brightness){
        this.brightness = brightness;
    }
    public void setFirstSpinner(String firstSpinner){
        this.firstSpinner = firstSpinner;
    }
    public void setSecondSpinner(String secondSpinner){
        this.secondSpinner = secondSpinner;
    }
    public void setThirdSpinner(String thirdSpinner){
        this.thirdSpinner = thirdSpinner;
    }
    public void setForthSpinner(String forthSpinner){
        this.forthSpinner = forthSpinner;
    }

}
