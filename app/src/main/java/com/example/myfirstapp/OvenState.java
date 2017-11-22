package com.example.myfirstapp;

/**
 * Created by Ariel on 20/11/2017.
 */

public class OvenState {

    private String status;
    private int temperature;
    private String heat;
    private String grill;
    private String convection;

    public OvenState(String status, int temperature, String heat, String grill, String convection) {

        this.status = status;
        this.temperature = temperature;
        this.heat = heat;
        this.grill = grill;
        this.convection = convection;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public void setHeat(String heat) {
        this.heat = heat;
    }
    public void setGrill(String grill) {
        this.grill = grill;
    }
    public void setConvection(String convection) {
        this.convection = convection;
    }

    public String getStatus(){
        return status;
    }
    public int getTemperature(){
        return temperature;
    }
    public String getHeat(){
        return heat;
    }
    public String getGrill(){
        return grill;
    }
    public String getConvection(){
        return convection;
    }
}
