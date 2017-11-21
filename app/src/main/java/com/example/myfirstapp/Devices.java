package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class Devices extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);

        setTitle("Devices");
        context = this.getApplicationContext();
        final ArrayList<Device> devices = new ArrayList<Device>();
        String url = "http://10.0.2.2:8080/api/devices";
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        GsonRequest<getDevicesResponse> request =
                new GsonRequest<getDevicesResponse>(url, getDevicesResponse.class, null, new com.android.volley.Response.Listener<getDevicesResponse>() {
                    @Override
                    public void onResponse(getDevicesResponse response) {
                        Toast.makeText(Devices.this, "Bien", Toast.LENGTH_LONG).show();
                        devices.clear();
                        devices.addAll(response.getDevices());
                        for(Device dev: devices){
                            Log.d("name:",dev.getName());
                            Log.d("type",dev.getType());
                        }
                        constructGrid(devices);
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(Devices.this, "error desconocido", Toast.LENGTH_LONG).show();
                    }
                });
        request.setTag("asd");
        requestQueue.add(request);


    }



    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(this, Devices.class);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return true;
    }


    public void openHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void openSwitchedOn(View view) {
        Intent intent = new Intent(this, SwitchedOn.class);
        startActivity(intent);
    }

    public void constructGrid(final ArrayList<Device> devices){
        GridView gridView;
        gridView = (GridView) findViewById(R.id.GridView);
        gridView.setAdapter(new ImageAdapter(this, devices));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

                if (devices.get(position).getType().equals("eu0v2xgprrhhg41g")) {
                    String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
                    RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                    GsonRequest<GetBlindsState> request =
                            new GsonRequest<GetBlindsState>(true, url, GetBlindsState.class, null, new com.android.volley.Response.Listener<GetBlindsState>() {
                                @Override
                                public void onResponse(GetBlindsState response) {
                                    //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                                    UpdateBlinds(devices.get(position), response);
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("dsa", error.toString());
                                    Toast.makeText(Devices.this, "Error al cargar", Toast.LENGTH_LONG).show();
                                }
                            });
                    request.setTag("asd");
                    requestQueue.add(request);

                }

                if (devices.get(position).getType().equals("lsf78ly0eqrjbz91")) {
                    String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
                    RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                    GsonRequest<GetDoorState> request =
                            new GsonRequest<GetDoorState>(true, url, GetDoorState.class, null, new com.android.volley.Response.Listener<GetDoorState>() {
                                @Override
                                public void onResponse(GetDoorState response) {
                                    Toast.makeText(Devices.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                                    UpdateDoor(devices.get(position), response);
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("dsa", error.toString());
                                    Toast.makeText(Devices.this, "Error al cargar", Toast.LENGTH_LONG).show();
                                }
                            });
                    request.setTag("asd");
                    requestQueue.add(request);

                }

                if (devices.get(position).getType().equals("li6cbv5sdlatti0j")) {
                    String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
                    RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                    GsonRequest<GetAcState> request =
                            new GsonRequest<GetAcState>(true, url, GetAcState.class, null, new com.android.volley.Response.Listener<GetAcState>() {
                                @Override
                                public void onResponse(GetAcState response) {
                                    //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                                    UpdateAc(devices.get(position), response);
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("dsa", error.toString());
                                    Toast.makeText(Devices.this, "Error al cargar", Toast.LENGTH_LONG).show();
                                }
                            });
                    request.setTag("asd");
                    requestQueue.add(request);
                }

                if (devices.get(position).getType().equals("im77xxyulpegfmv8")) {
                    String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
                    RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                    GsonRequest<GetOvenState> request =
                            new GsonRequest<GetOvenState>(true, url, GetOvenState.class, null, new com.android.volley.Response.Listener<GetOvenState>() {
                                @Override
                                public void onResponse(GetOvenState response) {
                                    //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                                    UpdateOven(devices.get(position), response);
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("dsa", error.toString());
                                    Toast.makeText(Devices.this, "Error al cargar", Toast.LENGTH_LONG).show();
                                }
                            });
                    request.setTag("asd");
                    requestQueue.add(request);
                }


                if (devices.get(position).getType().equals("go46xmbqeomjrsjr")) {

                    String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
                    RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                    GsonRequest<GetLampState> request =
                            new GsonRequest<GetLampState>(true, url, GetLampState.class, null, new com.android.volley.Response.Listener<GetLampState>() {
                                @Override
                                public void onResponse(GetLampState response) {
                                    //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                                    UpdateLamp(devices.get(position), response);
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("dsa", error.toString());
                                    Toast.makeText(Devices.this, "Error al cargar", Toast.LENGTH_LONG).show();
                                }
                            });
                    request.setTag("asd");
                    requestQueue.add(request);

                }

                if (devices.get(position).getType().equals("rnizejqr2di0okho")) {
                    String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
                    RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                    GsonRequest<GetRefrigeratorState> request =
                            new GsonRequest<GetRefrigeratorState>(true, url, GetRefrigeratorState.class, null, new com.android.volley.Response.Listener<GetRefrigeratorState>() {
                                @Override
                                public void onResponse(GetRefrigeratorState response) {
                                    //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                                    UpdateRefrigerator(devices.get(position), response);
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("dsa", error.toString());
                                    Toast.makeText(Devices.this, "Error al cargar", Toast.LENGTH_LONG).show();
                                }
                            });
                    request.setTag("asd");
                    requestQueue.add(request);
                }
            }
        });
    }

    public void UpdateDoor(Device door, GetDoorState response){


        setContentView(R.layout.door);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(door.getName());
        TextView title = findViewById(R.id.DoorTitle);
        TextView desc = findViewById(R.id.DoorDescription);
        title.setText(door.getName());
        desc.setText("Description: " + door.getMeta());

        ToggleButton StatusButton = (ToggleButton) findViewById(R.id.DoorStatus);
        final ToggleButton LockButton = (ToggleButton) findViewById(R.id.LockDoor);

        if(response.getResult().getStatus().equals("closed")){
            StatusButton.setChecked(false);
            if(response.getResult().getLock().equals("unlocked"))
                LockButton.setChecked(false);
            else
                LockButton.setChecked(true);
        }else{
            StatusButton.setChecked(true);
            LockButton.setChecked(false);
            LockButton.setEnabled(false);
        }
        StatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    LockButton.setEnabled(false);
                    LockButton.setChecked(false);
                }
                else
                {
                    LockButton.setEnabled(true);
                }
            }
        });

    }

    public void UpdateBlinds(Device blinds, GetBlindsState response){

        setContentView(R.layout.blinds);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(blinds.getName());
        TextView title = findViewById(R.id.BlindsTitle);
        TextView desc = findViewById(R.id.BlindsDescription);

        title.setText(blinds.getName());
        desc.setText("Description: " + blinds.getMeta());
        final ToggleButton StatusButton = (ToggleButton) findViewById(R.id.BlindsStatus);
        if(response.getResult().getStatus() == "opened" || response.getResult().getStatus() == "opening"){
            StatusButton.setChecked(true);
        }
        else {
            StatusButton.setChecked(false);
        }


    }

    public void UpdateAc(Device ac, GetAcState response){
        setContentView(R.layout.ac);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(ac.getName());
        TextView title = findViewById(R.id.AcTitle);
        TextView desc = findViewById(R.id.AcDescription);
        title.setText(ac.getName());
        desc.setText("Description: " + ac.getMeta());
        Toast.makeText(Devices.this, response.getResult().getFanSpeed(), Toast.LENGTH_LONG).show();
        ToggleButton StatusButton = (ToggleButton) findViewById(R.id.AcStatus);


        if(response.getResult().getStatus().equals("on")){
            StatusButton.setChecked(true);
        }else
            StatusButton.setChecked(false);

        final TextView temperature = (TextView) findViewById(R.id.TemperatureAc);
        temperature.setText(String.valueOf(response.getResult().getTemperature()));
        final int lastTemperatureInt = response.getResult().getTemperature();
        final String lastTemprature = String.valueOf(lastTemperatureInt);

        final Spinner modeAc = (Spinner) findViewById(R.id.ModeAc);
        if(response.getResult().getMode().equals("Cool"))
            modeAc.setSelection(0);
        else if (response.getResult().getMode().equals("Heat"))
            modeAc.setSelection(1);
        else
            modeAc.setSelection(2);
        final int lastMode = modeAc.getSelectedItemPosition();

        final Spinner vertSwing = (Spinner) findViewById(R.id.VerticalSwing);
        if(response.getResult().getVerticalSwing().equals("Auto"))
            vertSwing.setSelection(0);
        else if (response.getResult().getVerticalSwing().equals("22°"))
            vertSwing.setSelection(1);
        else if (response.getResult().getVerticalSwing().equals("45°"))
            vertSwing.setSelection(2);
        else if (response.getResult().getVerticalSwing().equals("67°"))
            vertSwing.setSelection(3);
        else
            vertSwing.setSelection(4);
        final int lastVS = vertSwing.getSelectedItemPosition();

        final Spinner horSwing = (Spinner) findViewById(R.id.HorizontalSwing);
        if(response.getResult().getHorizontalSwing().equals("Auto"))
            horSwing.setSelection(0);
        else if (response.getResult().getHorizontalSwing().equals("-90°"))
            horSwing.setSelection(1);
        else if (response.getResult().getHorizontalSwing().equals("-45°"))
            horSwing.setSelection(2);
        else if (response.getResult().getHorizontalSwing().equals("0°"))
            horSwing.setSelection(3);
        else if (response.getResult().getHorizontalSwing().equals("45°"))
            horSwing.setSelection(4);
        else
            horSwing.setSelection(5);
        final int lastHS = horSwing.getSelectedItemPosition();

        final Spinner fanSpeed = (Spinner) findViewById(R.id.FanSpeed);
        if(response.getResult().getFanSpeed().equals("Auto"))
            fanSpeed.setSelection(0);
        else if (response.getResult().getFanSpeed().equals("25"))
            fanSpeed.setSelection(1);
        else if (response.getResult().getFanSpeed().equals("50"))
            fanSpeed.setSelection(2);
        else if (response.getResult().getFanSpeed().equals("75"))
            fanSpeed.setSelection(3);
        else
            fanSpeed.setSelection(4);
        final int lastFanSpeed = fanSpeed.getSelectedItemPosition();

        if(! StatusButton.isChecked()){
            temperature.setEnabled(false);
            modeAc.setEnabled(false);
            vertSwing.setEnabled(false);
            horSwing.setEnabled(false);
            fanSpeed.setEnabled(false);
        }else{
            temperature.setEnabled(true);
            modeAc.setEnabled(true);
            vertSwing.setEnabled(true);
            horSwing.setEnabled(true);
            fanSpeed.setEnabled(true);
        }





        StatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(! isChecked)
                {
                    temperature.setEnabled(false);
                    temperature.setText(lastTemprature);
                    modeAc.setEnabled(false);
                    modeAc.setSelection(lastMode);
                    vertSwing.setEnabled(false);
                    vertSwing.setSelection(lastVS);
                    horSwing.setEnabled(false);
                    horSwing.setSelection(lastHS);
                    fanSpeed.setEnabled(false);
                    fanSpeed.setSelection(lastFanSpeed);


                }
                else
                {
                    temperature.setEnabled(true);
                    modeAc.setEnabled(true);
                    vertSwing.setEnabled(true);
                    horSwing.setEnabled(true);
                    fanSpeed.setEnabled(true);
                }
            }
        });
    }




    public void UpdateOven(Device oven, GetOvenState response){
        setContentView(R.layout.oven);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(oven.getName());
        TextView title = findViewById(R.id.OvenTitle);
        TextView desc = findViewById(R.id.OvenDescription);
        title.setText(oven.getName());
        desc.setText("Description: " + oven.getMeta());

        ToggleButton StatusButton = (ToggleButton) findViewById(R.id.StatusOven);
        if(response.getResult().getStatus().equals("on")){
            StatusButton.setChecked(true);
        }else
            StatusButton.setChecked(false);

        final TextView temperature = (TextView) findViewById(R.id.TemperatureOven);
        temperature.setText(String.valueOf(response.getResult().getTemperature()));
        final int lastTemperatureInt = response.getResult().getTemperature();
        final String lastTemprature = String.valueOf(lastTemperatureInt);

        final Spinner heatMode = (Spinner) findViewById(R.id.HeatMode);
        if(response.getResult().getHeat().equals("Conventional"))
            heatMode.setSelection(0);
        else if(response.getResult().getHeat().equals("Bottom"))
            heatMode.setSelection(1);
        else
            heatMode.setSelection(2);
        final int lastHeatMode = heatMode.getSelectedItemPosition();

        final Spinner grillMode = (Spinner) findViewById(R.id.GrillMode);
        if(response.getResult().getGrill().equals("Large"))
            grillMode.setSelection(0);
        else if(response.getResult().getGrill().equals("Eco"))
            grillMode.setSelection(1);
        else
            grillMode.setSelection(2);
        final int lastGrillMode = grillMode.getSelectedItemPosition();

        final Spinner convMode = (Spinner) findViewById(R.id.ConvMode);
        if(response.getResult().getConvection().equals("Normal"))
            convMode.setSelection(0);
        else if(response.getResult().getConvection().equals("Eco"))
            convMode.setSelection(1);
        else
            convMode.setSelection(2);
        final int lastConvMode = convMode.getSelectedItemPosition();

        if(! StatusButton.isChecked()){
            temperature.setEnabled(false);
            heatMode.setEnabled(false);
            grillMode.setEnabled(false);
            convMode.setEnabled(false);
        }

        StatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(! isChecked)
                {
                    temperature.setEnabled(false);
                    temperature.setText(lastTemprature);
                    heatMode.setEnabled(false);
                    heatMode.setSelection(lastHeatMode);
                    grillMode.setEnabled(false);
                    grillMode.setSelection(lastGrillMode);
                    convMode.setEnabled(false);
                    convMode.setSelection(lastConvMode);


                }
                else
                {
                    temperature.setEnabled(true);
                    heatMode.setEnabled(true);
                    grillMode.setEnabled(true);
                    convMode.setEnabled(true);
                }
            }
        });
    }

    public void UpdateLamp(Device lamp, GetLampState response){

        setContentView(R.layout.lamp);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(lamp.getName());
        TextView title = findViewById(R.id.LampTitle);
        TextView desc = findViewById(R.id.LampDescription);
        title.setText(lamp.getName());
        desc.setText("Description: " + lamp.getMeta());


        ToggleButton StatusButton = (ToggleButton) findViewById(R.id.StatusLamp);



        final TextView brightness = (TextView) findViewById(R.id.BrightnessLamp);
        final String lastBrightness = String.valueOf(response.getResult().getBrightness());
        brightness.setText(lastBrightness);


        if(response.getResult().getStatus().equals("on")) {
            StatusButton.setChecked(true);
            brightness.setEnabled(true);
        }
        else {
            StatusButton.setChecked(false);
            brightness.setEnabled(false);

        }
        StatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(! isChecked)
                {
                    brightness.setEnabled(false);
                    brightness.setText(lastBrightness);
                }
                else
                {
                    brightness.setEnabled(true);
                }
            }
        });

    }

    public void UpdateRefrigerator(Device fridge, GetRefrigeratorState response){
        setContentView(R.layout.fridge);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(fridge.getName());
        TextView title = findViewById(R.id.FridgeTitle);
        TextView desc = findViewById(R.id.FridgeDesc);
        title.setText(fridge.getName());
        desc.setText("Description: " + fridge.getMeta());

        final TextView temperature = (TextView) findViewById(R.id.FridgeTemperature);
        temperature.setText(String.valueOf(response.getResult().getTemperature()));
        final TextView freezerTemperature = (TextView) findViewById(R.id.FreezerTemeprature);
        freezerTemperature.setText(String.valueOf(response.getResult().getFreezerTemperature()));
        final Spinner fridgeMode = (Spinner) findViewById(R.id.FridgeMode);
        if(response.getResult().getMode().equals("Default"))
            fridgeMode.setSelection(0);
        else if(response.getResult().getMode().equals("Vacation"))
            fridgeMode.setSelection(1);
        else
            fridgeMode.setSelection(2);

    }

}



