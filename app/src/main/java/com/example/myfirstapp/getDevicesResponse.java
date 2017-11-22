package com.example.myfirstapp;

import java.util.ArrayList;

public class getDevicesResponse {
    private ArrayList<Device> devices;

    public getDevicesResponse(ArrayList<Device> devices) {

        this.devices = devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public ArrayList<Device> getDevices() { return this.devices; }
}
