package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout); //con esto el decimos cual es el layout grafico, osea donde estan las ID's

        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);

        //createTabHost(savedInstanceState);

        setTitle("Home");



        Log.d("asd","whut");
        final ArrayList<Device> devices = new ArrayList<Device>();
        String url = "http://10.0.2.2:8080/api/devices";
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        GsonRequest<getDevicesResponse> request =
                new GsonRequest<getDevicesResponse>(url, getDevicesResponse.class, null, new com.android.volley.Response.Listener<getDevicesResponse>() {
                    @Override
                    public void onResponse(getDevicesResponse response) {
                        //Toast.makeText(MainActivity.this, "Bien", Toast.LENGTH_LONG).show();
                        devices.clear();
                        devices.addAll(response.getDevices());
                        for(Device dev: devices){
                            Log.d("name:",dev.getName());
                            Log.d("type",dev.getType());
                        }
                        updateDevices(devices);
                        UpdateOnDevices(devices);
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(MainActivity.this, "error desconocido", Toast.LENGTH_LONG).show();
                    }
                });
        request.setTag("asd");
        requestQueue.add(request);

        //Toast.makeText(MainActivity.this, "Finalizado", Toast.LENGTH_LONG).show();


    }

    public void updateDevices(final ArrayList<Device> devices){
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

            final LinearLayout[] individual = new LinearLayout[devices.size()];
            for (int i = 0; i < devices.size(); i++)
                individual[i] = new LinearLayout(this);

            for (int i = 0; i < devices.size(); i++) {
                LinearLayout lin = individual[i];
                lin.addView(createView(devices.get(i)));
                lin.setPadding(0, 0, 20, 0);
                lin.setClickable(true);
                lin.setOnClickListener(new CustomOnClickListener(i) {
                    @Override
                    public void onClick(View v) {
                        startLayout(devices, this.index);
                        //Toast.makeText(MainActivity.this, "jeje" + String.valueOf(index), Toast.LENGTH_LONG).show();

                    }
                });
                linearLayout.addView(lin);
            }
    }

    public void UpdateOnDevices(final ArrayList<Device> devices){
        LinearLayout linearLayout = findViewById(R.id.linearLayout2);
        for (int i = 0; i < devices.size(); i++) {
            deviceIsOn(devices.get(i),i, linearLayout, devices);
        }
    }

    public void createSwitchedOnDevice(Device device, int i, LinearLayout linearLayout, final ArrayList<Device> devices){
        final LinearLayout[] individual = new LinearLayout[devices.size()];
        individual[i] = new LinearLayout(this);
        LinearLayout lin = individual[i];
        lin.addView(createView(devices.get(i)));
        lin.setPadding(0, 0, 20, 0);
        lin.setClickable(true);
        lin.setOnClickListener(new CustomOnClickListener(i) {
            @Override
            public void onClick(View v) {
                startLayout(devices, this.index);
            }
        });
        linearLayout.addView(lin);
    }

    public void deviceIsOn(final Device device, final int i, final LinearLayout linearLayout, final ArrayList<Device> devices){
        if(device.getType().equals("lsf78ly0eqrjbz91") || device.getType().equals("eu0v2xgprrhhg41g") || device.getType().equals("rnizejqr2di0okho"))
            return;

        final Boolean[] result = {null};
        if(device.getType().equals("li6cbv5sdlatti0j")){
            String url = "http://10.0.2.2:8080/api/devices/" + device.getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetAcState> request =
                    new GsonRequest<GetAcState>(true,url, GetAcState.class, null, new com.android.volley.Response.Listener<GetAcState>() {
                        @Override
                        public void onResponse(GetAcState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            if(response.getResult().getStatus().equals("on"))
                                createSwitchedOnDevice(device,i,linearLayout,devices);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, "Error al cargar", Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);
        }

        if(device.getType().equals("im77xxyulpegfmv8")){
            String url = "http://10.0.2.2:8080/api/devices/" + device.getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetOvenState> request =
                    new GsonRequest<GetOvenState>(true,url, GetOvenState.class, null, new com.android.volley.Response.Listener<GetOvenState>() {
                        @Override
                        public void onResponse(GetOvenState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            if(response.getResult().getStatus().equals("on"))
                                createSwitchedOnDevice(device,i,linearLayout,devices);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, "Error al cargar", Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);
        }


        if(device.getType().equals("go46xmbqeomjrsjr")){

            String url = "http://10.0.2.2:8080/api/devices/" + device.getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetLampState> request =
                    new GsonRequest<GetLampState>(true,url, GetLampState.class, null, new com.android.volley.Response.Listener<GetLampState>() {
                        @Override
                        public void onResponse(GetLampState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            if(response.getResult().getStatus().equals("on"))
                                createSwitchedOnDevice(device,i,linearLayout,devices);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, "Error al cargar", Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);

        }

        //while(result[0] == null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    public void openDevices(View view) {
        Intent intent = new Intent(this, Devices.class);
        startActivity(intent);
    }

    public void openSwitchedOn(View view) {
        Intent intent = new Intent(this, SwitchedOn.class);
        startActivity(intent);
    }


    private View createView(Device obj) {

            Context context = this;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View gridView;
            gridView = inflater.inflate(R.layout.mobile, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(obj.getName());

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            String mobile = obj.getName();

            switch (obj.getType()) {
                case "rnizejqr2di0okho":
                    imageView.setImageResource(R.drawable.heladera);
                    break;
                case "go46xmbqeomjrsjr":
                    imageView.setImageResource(R.drawable.lamp);
                    break;
                case "eu0v2xgprrhhg41g":
                    imageView.setImageResource(R.drawable.blinds);
                    break;
                case "im77xxyulpegfmv8":
                    imageView.setImageResource(R.drawable.oven);
                    break;
                case "li6cbv5sdlatti0j":
                    imageView.setImageResource(R.drawable.ac);
                    break;
                case "lsf78ly0eqrjbz91":
                    imageView.setImageResource(R.drawable.door);
                    break;
                default:
                    break;
            }

            return gridView;

    }

    private void startLayout(final ArrayList<Device> devices, final int position){

        if(devices.get(position).getType().equals("eu0v2xgprrhhg41g")){
            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetBlindsState> request =
                    new GsonRequest<GetBlindsState>(true,url, GetBlindsState.class, null, new com.android.volley.Response.Listener<GetBlindsState>() {
                        @Override
                        public void onResponse(GetBlindsState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            UpdateBlinds(devices.get(position), response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, "Error al cargar", Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);

        }

        if(devices.get(position).getType().equals("lsf78ly0eqrjbz91")){
            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetDoorState> request =
                    new GsonRequest<GetDoorState>(true,url, GetDoorState.class, null, new com.android.volley.Response.Listener<GetDoorState>() {
                        @Override
                        public void onResponse(GetDoorState response) {
                            Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            UpdateDoor(devices.get(position), response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, "Error al cargar", Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);

        }

        if(devices.get(position).getType().equals("li6cbv5sdlatti0j")){
            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetAcState> request =
                    new GsonRequest<GetAcState>(true,url, GetAcState.class, null, new com.android.volley.Response.Listener<GetAcState>() {
                        @Override
                        public void onResponse(GetAcState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            UpdateAc(devices.get(position), response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, "Error al cargar", Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);
        }

        if(devices.get(position).getType().equals("im77xxyulpegfmv8")){
            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetOvenState> request =
                    new GsonRequest<GetOvenState>(true,url, GetOvenState.class, null, new com.android.volley.Response.Listener<GetOvenState>() {
                        @Override
                        public void onResponse(GetOvenState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            UpdateOven(devices.get(position), response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, "Error al cargar", Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);
        }


        if(devices.get(position).getType().equals("go46xmbqeomjrsjr")){

            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetLampState> request =
                    new GsonRequest<GetLampState>(true,url, GetLampState.class, null, new com.android.volley.Response.Listener<GetLampState>() {
                        @Override
                        public void onResponse(GetLampState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            UpdateLamp(devices.get(position), response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, "Error al cargar", Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);

        }

        if(devices.get(position).getType().equals("rnizejqr2di0okho")){
            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetRefrigeratorState> request =
                    new GsonRequest<GetRefrigeratorState>(true,url, GetRefrigeratorState.class, null, new com.android.volley.Response.Listener<GetRefrigeratorState>() {
                        @Override
                        public void onResponse(GetRefrigeratorState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            UpdateRefrigerator(devices.get(position), response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, "Error al cargar", Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);
        }

    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
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
        Toast.makeText(MainActivity.this, response.getResult().getFanSpeed(), Toast.LENGTH_LONG).show();
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
