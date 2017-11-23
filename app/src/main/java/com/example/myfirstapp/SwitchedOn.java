package com.example.myfirstapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SwitchedOn extends AppCompatActivity {
    Context context;
    String actualIdDevice;
    Device actualDevice;
    LastState lastState = new LastState();
    public static final String PREFS_NAME = "MyPrefsFile";
    String color = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switched_on);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.SwitchedOnToolBar);
        setSupportActionBar(myToolbar);

        setTitle( getString(R.string.Switched));
        saveClass();
        context = this.getApplicationContext();
        final ArrayList<Device> devices = new ArrayList<Device>();
        final ArrayList<Device> devicesOn = new ArrayList<Device>();
        String url = "http://10.0.2.2:8080/api/devices";
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        GsonRequest<getDevicesResponse> request =
                new GsonRequest<getDevicesResponse>(url, getDevicesResponse.class, null, new com.android.volley.Response.Listener<getDevicesResponse>() {
                    @Override
                    public void onResponse(getDevicesResponse response) {
                        devices.clear();
                        devices.addAll(response.getDevices());
                        devicesOn.addAll(response.getDevices());
                        for(Device dev: devicesOn){
                            Log.d("name:",dev.getName());
                            Log.d("type",dev.getType());
                        }
                        for(int i = 0; i < devices.size();i++)
                            selectDevices(devices,devicesOn,i);


                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        request.setTag("asd");
        requestQueue.add(request);
    }

    public void saveClass(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("class", "SwitchedOn");
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.Settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void colorPicker(View view) {
        ColorPickerDialogBuilder

                .with(SwitchedOn.this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //Toast.makeText(MainActivity.this, "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this, "onColorSelected: 0x" + Integer.toHexString(selectedColor).substring(2), Toast.LENGTH_LONG).show();

                    }
                })
                .setPositiveButton(getString(R.string.Accept), new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        //Toast.makeText(MainActivity.this, "onClick 1: onColorSelected: 0x" + Integer.toHexString(selectedColor).substring(2), Toast.LENGTH_LONG).show();
                        TextView btn = findViewById(R.id.colorp);
                        btn.setBackgroundColor(selectedColor);
                        color = Integer.toHexString(selectedColor).substring(2);
                    }
                })
                .setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(this, SwitchedOn.class);
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

    public void openDevices(View view){
        Intent intent = new Intent(this,Devices.class);
        startActivity(intent);
    }

    private void notificationsDevices(Device device){
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            boolean silent = settings.getBoolean(device.getId(), false);
            device.setNotState(silent);
    }


    public void selectDevices(final ArrayList<Device> devices, final ArrayList<Device> devicesOn, final int index){
            final Device device = devices.get(index);
            if(device.getType().equals("lsf78ly0eqrjbz91") || device.getType().equals("eu0v2xgprrhhg41g") || device.getType().equals("rnizejqr2di0okho"))
                devicesOn.remove(device);
            else if(device.getType().equals("li6cbv5sdlatti0j")){
                String url = "http://10.0.2.2:8080/api/devices/" + device.getId() + "/getState";
                RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
                GsonRequest<GetAcState> request =
                        new GsonRequest<GetAcState>(true,url, GetAcState.class, null, new com.android.volley.Response.Listener<GetAcState>() {
                            @Override
                            public void onResponse(GetAcState response) {
                                if(response.getResult().getStatus().equals("off"))
                                    devicesOn.remove(device);
                                if(index == devices.size() - 1) {
                                    constructGrid(devicesOn);

                                }

                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("dsa", error.toString());
                                Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                            }
                        });
                request.setTag("asd");
                requestQueue.add(request);
            }

            else if(device.getType().equals("im77xxyulpegfmv8")){
                String url = "http://10.0.2.2:8080/api/devices/" + device.getId() + "/getState";
                RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
                GsonRequest<GetOvenState> request =
                        new GsonRequest<GetOvenState>(true,url, GetOvenState.class, null, new com.android.volley.Response.Listener<GetOvenState>() {
                            @Override
                            public void onResponse(GetOvenState response) {
                                //Toast.makeText(SwitchedOn.this, "viendo" + device.getName(), Toast.LENGTH_LONG).show();
                                if(response.getResult().getStatus().equals("off")) {
                                    devicesOn.remove(device);
                                    //Toast.makeText(SwitchedOn.this, "Apagado", Toast.LENGTH_LONG).show();
                                    //Toast.makeText(SwitchedOn.this, "size" + devicesOn.size(), Toast.LENGTH_LONG).show();
                                }
                                if(index == devices.size() - 1)
                                    constructGrid(devicesOn);
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("dsa", error.toString());
                                Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                            }
                        });
                request.setTag("asd");
                requestQueue.add(request);

            }


            else if(device.getType().equals("go46xmbqeomjrsjr")){

                String url = "http://10.0.2.2:8080/api/devices/" + device.getId() + "/getState";
                RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
                GsonRequest<GetLampState> request =
                        new GsonRequest<GetLampState>(true,url, GetLampState.class, null, new com.android.volley.Response.Listener<GetLampState>() {
                            @Override
                            public void onResponse(GetLampState response) {
                                //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                                if(response.getResult().getStatus().equals("off"))
                                    devicesOn.remove(device);
                                if(index == devices.size() - 1)
                                    constructGrid(devicesOn);
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("dsa", error.toString());
                                Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                            }
                        });
                request.setTag("asd");
                requestQueue.add(request);

            }


    }

    public void constructGrid(final ArrayList<Device> devices){
        GridView gridView;
        gridView = (GridView) findViewById(R.id.GridSwitchedOn);
        gridView.setAdapter(new ImageAdapter(this, devices));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                actualIdDevice = devices.get(position).getId();
                actualDevice = devices.get(position);
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
                                    Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                                }
                            });
                    request.setTag("asd");
                    requestQueue.add(request);

                }

            }
        });
    }



    public void UpdateDoor(Device door, GetDoorState response) {


        setContentView(R.layout.door);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(door.getName());
        TextView title = findViewById(R.id.DoorTitle);
        TextView desc = findViewById(R.id.DoorDescription);
        title.setText(door.getName());
        desc.setText(getString(R.string.Description) + ": "+ door.getMeta());

        ToggleButton StatusButton = (ToggleButton) findViewById(R.id.DoorStatus);
        final ToggleButton LockButton = (ToggleButton) findViewById(R.id.LockDoor);

        if (response.getResult().getStatus().equals("closed")) {
            StatusButton.setChecked(false);
            lastState.setStatus(false);
            if (response.getResult().getLock().equals("unlocked")) {
                LockButton.setChecked(false);
                lastState.setLock(false);
            }
            else {
                LockButton.setChecked(true);
                lastState.setLock(true);
            }
        } else {
            StatusButton.setChecked(true);
            lastState.setStatus(true);
            LockButton.setChecked(false);
            LockButton.setEnabled(false);
        }
        StatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    LockButton.setEnabled(false);
                    LockButton.setChecked(false);
                } else {
                    LockButton.setEnabled(true);
                }
            }
        });

    }

    public void UpdateBlinds(Device blinds, GetBlindsState response) {

        setContentView(R.layout.blinds);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(blinds.getName());
        TextView title = findViewById(R.id.BlindsTitle);
        TextView desc = findViewById(R.id.BlindsDescription);

        title.setText(blinds.getName());
        desc.setText(getString(R.string.Description) + ": "+ blinds.getMeta());
        // Toast.makeText(MainActivity.this, response.getResult().getStatus(), Toast.LENGTH_LONG).show();
        final ToggleButton StatusButton = (ToggleButton) findViewById(R.id.BlindsStatus);
        if (response.getResult().getStatus().equals("opened") || response.getResult().getStatus().equals("opening")) {
            StatusButton.setChecked(true);
            lastState.setStatus(true);
        } else {
            StatusButton.setChecked(false);
            lastState.setStatus(false);
        }


    }

    public void UpdateAc(Device ac, GetAcState response) {
        setContentView(R.layout.ac);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(ac.getName());
        TextView title = findViewById(R.id.AcTitle);
        TextView desc = findViewById(R.id.AcDescription);
        title.setText(ac.getName());
        desc.setText(getString(R.string.Description) + ": " + ac.getMeta());
        //Toast.makeText(MainActivity.this, response.getResult().getFanSpeed(), Toast.LENGTH_LONG).show();
        ToggleButton StatusButton = (ToggleButton) findViewById(R.id.AcStatus);


        if (response.getResult().getStatus().equals("on")) {
            StatusButton.setChecked(true);
            lastState.setStatus(true);
        } else {
            StatusButton.setChecked(false);
            lastState.setStatus(false);
        }

        final TextView temperature = (TextView) findViewById(R.id.TemperatureAc);
        temperature.setText(String.valueOf(response.getResult().getTemperature()));
        lastState.setTemperature(response.getResult().getTemperature());
        final int lastTemperatureInt = response.getResult().getTemperature();
        final String lastTemprature = String.valueOf(lastTemperatureInt);

        final Spinner modeAc = (Spinner) findViewById(R.id.ModeAc);
        lastState.setFirstSpinner(response.getResult().getMode());
        if (response.getResult().getMode().equals("Cool"))
            modeAc.setSelection(0);
        else if (response.getResult().getMode().equals("Heat"))
            modeAc.setSelection(1);
        else
            modeAc.setSelection(2);
        final int lastMode = modeAc.getSelectedItemPosition();
        lastState.setSecondSpinner(response.getResult().getVerticalSwing());
        final Spinner vertSwing = (Spinner) findViewById(R.id.VerticalSwing);
        if (response.getResult().getVerticalSwing().equals("Auto"))
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
        lastState.setThirdSpinner(response.getResult().getHorizontalSwing());
        if (response.getResult().getHorizontalSwing().equals("Auto"))
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
        lastState.setForthSpinner(response.getResult().getFanSpeed());
        if (response.getResult().getFanSpeed().equals("Auto"))
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

        if (!StatusButton.isChecked()) {
            temperature.setEnabled(false);
            modeAc.setEnabled(false);
            vertSwing.setEnabled(false);
            horSwing.setEnabled(false);
            fanSpeed.setEnabled(false);
        } else {
            temperature.setEnabled(true);
            modeAc.setEnabled(true);
            vertSwing.setEnabled(true);
            horSwing.setEnabled(true);
            fanSpeed.setEnabled(true);
        }


        StatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
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


                } else {
                    temperature.setEnabled(true);
                    modeAc.setEnabled(true);
                    vertSwing.setEnabled(true);
                    horSwing.setEnabled(true);
                    fanSpeed.setEnabled(true);
                }
            }
        });
    }

    public void UpdateOven(Device oven, GetOvenState response) {
        setContentView(R.layout.oven);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(oven.getName());
        TextView title = findViewById(R.id.OvenTitle);
        TextView desc = findViewById(R.id.OvenDescription);
        title.setText(oven.getName());
        desc.setText(getString(R.string.Description) + ": " + oven.getMeta());

        ToggleButton StatusButton = (ToggleButton) findViewById(R.id.StatusOven);
        if (response.getResult().getStatus().equals("on")) {
            StatusButton.setChecked(true);
            lastState.setStatus(true);
        } else {
            StatusButton.setChecked(false);
            lastState.setStatus(false);
        }

        final TextView temperature = (TextView) findViewById(R.id.TemperatureOven);
        temperature.setText(String.valueOf(response.getResult().getTemperature()));
        final int lastTemperatureInt = response.getResult().getTemperature();
        lastState.setTemperature(lastTemperatureInt);
        final String lastTemprature = String.valueOf(lastTemperatureInt);

        final Spinner heatMode = (Spinner) findViewById(R.id.HeatMode);
        lastState.setFirstSpinner(response.getResult().getHeat());
        if (response.getResult().getHeat().equals("Conventional"))
            heatMode.setSelection(0);
        else if (response.getResult().getHeat().equals("Bottom"))
            heatMode.setSelection(1);
        else
            heatMode.setSelection(2);
        final int lastHeatMode = heatMode.getSelectedItemPosition();

        final Spinner grillMode = (Spinner) findViewById(R.id.GrillMode);
        lastState.setSecondSpinner(response.getResult().getGrill());
        if (response.getResult().getGrill().equals("Large"))
            grillMode.setSelection(0);
        else if (response.getResult().getGrill().equals("Eco"))
            grillMode.setSelection(1);
        else
            grillMode.setSelection(2);
        final int lastGrillMode = grillMode.getSelectedItemPosition();

        final Spinner convMode = (Spinner) findViewById(R.id.ConvMode);
        lastState.setThirdSpinner(response.getResult().getConvection());
        if (response.getResult().getConvection().equals("Normal"))
            convMode.setSelection(0);
        else if (response.getResult().getConvection().equals("Eco"))
            convMode.setSelection(1);
        else
            convMode.setSelection(2);
        final int lastConvMode = convMode.getSelectedItemPosition();

        if (!StatusButton.isChecked()) {
            temperature.setEnabled(false);
            heatMode.setEnabled(false);
            grillMode.setEnabled(false);
            convMode.setEnabled(false);
        }

        StatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    temperature.setEnabled(false);
                    temperature.setText(lastTemprature);
                    heatMode.setEnabled(false);
                    heatMode.setSelection(lastHeatMode);
                    grillMode.setEnabled(false);
                    grillMode.setSelection(lastGrillMode);
                    convMode.setEnabled(false);
                    convMode.setSelection(lastConvMode);


                } else {
                    temperature.setEnabled(true);
                    heatMode.setEnabled(true);
                    grillMode.setEnabled(true);
                    convMode.setEnabled(true);
                }
            }
        });
    }

    public void UpdateLamp(Device lamp, GetLampState response) {

        setContentView(R.layout.lamp);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(lamp.getName());
        TextView title = findViewById(R.id.LampTitle);
        TextView desc = findViewById(R.id.LampDescription);
        title.setText(lamp.getName());
        desc.setText(getString(R.string.Description) + ": " + lamp.getMeta());
        final TextView btn = findViewById(R.id.colorp);
        String color = "ff" + response.getResult().getColor();
        btn.setBackgroundColor((int) Long.parseLong(color, 16));


        ToggleButton StatusButton = (ToggleButton) findViewById(R.id.StatusLamp);


        final TextView brightness = (TextView) findViewById(R.id.BrightnessLamp);
        final String lastBrightness = String.valueOf(response.getResult().getBrightness());
        brightness.setText(lastBrightness);
        lastState.setBrightness(response.getResult().getBrightness());
        lastState.setFirstSpinner(response.getResult().getColor());

        if (response.getResult().getStatus().equals("on")) {
            StatusButton.setChecked(true);
            brightness.setEnabled(true);
            btn.setEnabled(true);
            lastState.setStatus(true);
        } else {
            StatusButton.setChecked(false);
            brightness.setEnabled(false);
            btn.setEnabled(false);
            lastState.setStatus(false);

        }
        StatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    brightness.setEnabled(false);
                    btn.setEnabled(false);
                    brightness.setText(lastBrightness);
                } else {
                    brightness.setEnabled(true);
                    btn.setEnabled(true);
                }
            }
        });

    }

    public void UpdateRefrigerator(Device fridge, GetRefrigeratorState response) {
        setContentView(R.layout.fridge);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(fridge.getName());
        TextView title = findViewById(R.id.FridgeTitle);
        TextView desc = findViewById(R.id.FridgeDesc);
        title.setText(fridge.getName());
        desc.setText(getString(R.string.Description) + ": " + fridge.getMeta());

        final TextView temperature = (TextView) findViewById(R.id.FridgeTemperature);
        temperature.setText(String.valueOf(response.getResult().getTemperature()));
        final TextView freezerTemperature = (TextView) findViewById(R.id.FreezerTemeprature);
        freezerTemperature.setText(String.valueOf(response.getResult().getFreezerTemperature()));
        final Spinner fridgeMode = (Spinner) findViewById(R.id.FridgeMode);
        lastState.setTemperature(response.getResult().getTemperature());
        lastState.setFreezertemperatureTemperature(response.getResult().getFreezerTemperature());
        lastState.setFirstSpinner(response.getResult().getMode());
        if (response.getResult().getMode().equals("Default"))
            fridgeMode.setSelection(0);
        else if (response.getResult().getMode().equals("Vacation"))
            fridgeMode.setSelection(1);
        else
            fridgeMode.setSelection(2);

    }

    public void acceptBlinds(View view) {
        final boolean changed;
        boolean status;
        String url;

        final ToggleButton StatusButton = (ToggleButton) findViewById(R.id.BlindsStatus);
        if (StatusButton.isChecked()) {
            status = true;
            url = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/up";
        } else {
            status = false;
            url = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/down";
        }

        if( status != lastState.getStatus())
            changed = true;
        else
            changed = false;

        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        GsonRequest<ResultStateBoolean> request =
                new GsonRequest<ResultStateBoolean>(null, url, ResultStateBoolean.class, null, new com.android.volley.Response.Listener<ResultStateBoolean>() {
                    @Override
                    public void onResponse(ResultStateBoolean response) {
                        if(actualDevice.getNotState() && changed){
                            if(StatusButton.isChecked()) {
                                Notifications noti = new Notifications(getString(R.string.NotiBlindsOpen),actualDevice.getName() , SwitchedOn.this);
                                noti.build();
                                noti.addToLodge();
                            }else{
                                Notifications noti = new Notifications(getString(R.string.NotiBlindsClosed),actualDevice.getName() , SwitchedOn.this);
                                noti.build();
                                noti.addToLodge();
                            }
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        request.setTag("asd");
        requestQueue.add(request);
        Intent intent = new Intent(this, SwitchedOn.class);
        startActivity(intent);

    }

    public void acceptDoor(View view) {
        notificationsDevices(actualDevice);
        String changes = "";
        String urlStatus;
        boolean status;
        final ToggleButton StatusButton = (ToggleButton) findViewById(R.id.DoorStatus);

        if (StatusButton.isChecked()) {
            urlStatus = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/open";
            status = true;
            if(status != lastState.getStatus())
                changes += getString(R.string.NotiDoorOpen) +"\n";
        } else {
            urlStatus = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/close";
            status = false;
            if(status != lastState.getStatus())
                changes += getString(R.string.NotiDoorClosed) + "\n";
        }


        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        GsonRequest<ResultStateBoolean> requestStatus =
                new GsonRequest<ResultStateBoolean>(null, urlStatus, ResultStateBoolean.class, null, new com.android.volley.Response.Listener<ResultStateBoolean>() {
                    @Override
                    public void onResponse(ResultStateBoolean response) {
                        lockDoor(finalChanges);
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        requestStatus.setTag("status");
        requestQueue.add(requestStatus);


    }

    private void lockDoor(String changes) {
        String urlLock;
        boolean lock;
        final ToggleButton lockButton = (ToggleButton) findViewById(R.id.LockDoor);
        if (lockButton.isChecked()) {
            urlLock = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/lock";
            lock = true;
            if(lock != lastState.getLock())
                changes += getString(R.string.NotiDoorLock);
        } else {
            urlLock = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/unlock";
            lock = false;
            if(lock != lastState.getLock())
                changes += getString(R.string.NotiDoorUnlocked);
        }
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        GsonRequest<ResultStateBoolean> requestLock =
                new GsonRequest<ResultStateBoolean>(null, urlLock, ResultStateBoolean.class, null, new com.android.volley.Response.Listener<ResultStateBoolean>() {
                    @Override
                    public void onResponse(ResultStateBoolean response) {
                        if(actualDevice.getNotState() && finalChanges.length() > 0) {
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), SwitchedOn.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        requestLock.setTag("lock");
        requestQueue.add(requestLock);
        Intent intent = new Intent(this, SwitchedOn.class);
        startActivity(intent);
    }

    public void acceptFridge(View view){
        notificationsDevices(actualDevice);
        final TextView temperature = (TextView) findViewById(R.id.FridgeTemperature);
        int temp = Integer.parseInt(temperature.getText().toString());
        final TextView temperatureFreezer = (TextView) findViewById(R.id.FreezerTemeprature);
        int tempFreezer = Integer.parseInt(temperatureFreezer.getText().toString());
        if( (tempFreezer < -20 || tempFreezer > -8) && (temp > 8 || temp < 2)){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage(getString(R.string.BothErrorsFridge));
            dlgAlert.setTitle(getString(R.string.info));
            dlgAlert.setPositiveButton(getString(R.string.Accept),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
        else if(temp > 8 || temp < 2){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage(getString(R.string.errorTemperatureFridge));
            dlgAlert.setTitle(getString(R.string.info));
            dlgAlert.setPositiveButton(getString(R.string.Accept),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
        else if(tempFreezer < -20 || tempFreezer > -8){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage(getString(R.string.errorTemperatureFreezerFridge));
            dlgAlert.setTitle(getString(R.string.info));
            dlgAlert.setPositiveButton(getString(R.string.Accept),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

        } else{
            changeModeFridge();
            Intent intent = new Intent(this, SwitchedOn.class);
            startActivity(intent);
        }
    }


    public void changeModeFridge() {
        String changes = "";
        Spinner modeFridge = (Spinner) findViewById(R.id.FridgeMode);
        String newMode = modeFridge.getSelectedItem().toString();
        String urlMode = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setMode";
        if(! lastState.getFirstSpinner().equals(newMode))
            changes += getString(R.string.NotiFridgeMode) + "\n";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", newMode);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlMode, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeTemperatureFridge(finalChanges);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);

    }

    private void changeTemperatureFridge(String changes) {
        String urlTemperature = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setTemperature";
        final TextView temperature = (TextView) findViewById(R.id.FridgeTemperature);
        int temp = Integer.parseInt(temperature.getText().toString());

        if(lastState.getTemperature() != temp)
            changes += getString(R.string.NotiFridgeTemp) + "\n";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", String.valueOf(temp));
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlTemperature, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeFreezerTemperature(finalChanges);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(request);

    }

    private void changeFreezerTemperature(String changes) {
        String urlTemperature = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setFreezerTemperature";
        final TextView temperature = (TextView) findViewById(R.id.FreezerTemeprature);
        int temp = Integer.parseInt(temperature.getText().toString());

        if(lastState.getFreezertemperature() != temp)
            changes += getString(R.string.NotiFridgeFreezer);
        Map<String, Object> jsonParams1 = new HashMap<>();
        jsonParams1.put("0", String.valueOf(temp));
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlTemperature, new JSONObject(jsonParams1),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(actualDevice.getNotState() && finalChanges.length() > 0) {
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), SwitchedOn.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(request);

    }

    public void acceptOven(View view) {
        notificationsDevices(actualDevice);
        final TextView temperature = (TextView) findViewById(R.id.TemperatureOven);
        int temp = Integer.parseInt(temperature.getText().toString());
        if(temp > 230 || temp < 90){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage(getString(R.string.ErrorOven));
            dlgAlert.setTitle(getString(R.string.info));
            dlgAlert.setPositiveButton(getString(R.string.Accept),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        } else {
            changeOvenStatus();
            Intent intent = new Intent(this, SwitchedOn.class);
            startActivity(intent);
        }
    }

    private void changeOvenStatus() {
        String urlStatus;
        String changes = "";
        final ToggleButton statusButton = (ToggleButton) findViewById(R.id.StatusOven);
        final boolean decide;
        if (statusButton.isChecked()) {
            urlStatus = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/turnOn";
            decide = true;
            if(decide != lastState.getStatus()){
                changes += getString(R.string.NotiOvenOn) + "\n";
            }
        } else {
            urlStatus = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/turnOff";
            decide = false;
            if(decide != lastState.getStatus()){
                changes += getString(R.string.NotiOvenOff) + "\n";
            }
        }
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        GsonRequest<ResultStateBoolean> request =
                new GsonRequest<ResultStateBoolean>(null, urlStatus, ResultStateBoolean.class, null, new com.android.volley.Response.Listener<ResultStateBoolean>() {
                    @Override
                    public void onResponse(ResultStateBoolean response) {
                        if (decide) {
                            changeOvenTemperature(finalChanges);
                        }else if(actualDevice.getNotState() && finalChanges.length() > 0){
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), SwitchedOn.this);
                            noti.build();
                            noti.addToLodge();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        request.setTag("status");
        requestQueue.add(request);
    }

    private void changeOvenTemperature(String changes) {

        String urlTemperature = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setTemperature";
        final TextView temperature = (TextView) findViewById(R.id.TemperatureOven);
        int temp = Integer.parseInt(temperature.getText().toString());

        if(lastState.getTemperature() != temp)
            changes += getString(R.string.NotiOvenTemp) + "\n";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", String.valueOf(temp));
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlTemperature, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeHeatMode(finalChanges);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(request);

    }

    private void changeHeatMode(String changes) {
        Spinner modeFridge = (Spinner) findViewById(R.id.HeatMode);
        String newMode = modeFridge.getSelectedItem().toString();
        String urlMode = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setHeat";

        if(! lastState.getFirstSpinner().equals(newMode))
            changes += getString(R.string.NotiOvenHeat) + "\n";
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", newMode);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlMode, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeGrillMode(finalChanges);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    private void changeGrillMode(String changes) {
        Spinner modeFridge = (Spinner) findViewById(R.id.GrillMode);
        String newMode = modeFridge.getSelectedItem().toString();
        String urlMode = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setGrill";
        if(! lastState.getSecondSpinner().equals(newMode))
            changes += getString(R.string.NotiOvenGrill) + "\n";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", newMode);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlMode, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeConvectionMode(finalChanges);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    private void changeConvectionMode(String changes){

        Spinner modeFridge = (Spinner) findViewById(R.id.ConvMode);
        String newMode = modeFridge.getSelectedItem().toString();
        String urlMode = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setConvection";
        if(! lastState.getThirdSpinner().equals(newMode))
            changes += getString(R.string.NotiOvenConv);


        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", newMode);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlMode, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(actualDevice.getNotState() && finalChanges.length() > 0){
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), SwitchedOn.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);

    }

    public void acceptAc(View view) {
        notificationsDevices(actualDevice);
        final TextView temperature = (TextView) findViewById(R.id.TemperatureAc);
        int temp = Integer.parseInt(temperature.getText().toString());
        if(temp > 38 || temp < 18){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage(getString(R.string.ErrorAc));
            dlgAlert.setTitle(getString(R.string.info));
            dlgAlert.setPositiveButton(getString(R.string.Accept),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }else {
            changeAcStatus();
            Intent intent = new Intent(this, SwitchedOn.class);
            startActivity(intent);
        }
    }

    private void changeAcStatus() {
        String urlStatus;
        String changes = "";
        final ToggleButton statusButton = (ToggleButton) findViewById(R.id.AcStatus);
        final boolean decide;
        if (statusButton.isChecked()) {
            urlStatus = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/turnOn";
            decide = true;
            if(decide != lastState.getStatus())
                changes += getString(R.string.NotiAcStatusOn) + "\n";
        } else {
            urlStatus = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/turnOff";
            decide = false;
            if(decide != lastState.getStatus())
                changes += getString(R.string.NotiAcStatusOff) + "\n";
        }
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        GsonRequest<ResultStateBoolean> request =
                new GsonRequest<ResultStateBoolean>(null, urlStatus, ResultStateBoolean.class, null, new com.android.volley.Response.Listener<ResultStateBoolean>() {
                    @Override
                    public void onResponse(ResultStateBoolean response) {
                        if (decide) {
                            changeAcTemperature(finalChanges);
                        }else if(actualDevice.getNotState() && finalChanges.length() > 0){
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), SwitchedOn.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        request.setTag("status");
        requestQueue.add(request);
    }

    private void changeAcTemperature(String changes) {

        String urlTemperature = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setTemperature";
        final TextView temperature = (TextView) findViewById(R.id.TemperatureAc);
        int temp = Integer.parseInt(temperature.getText().toString());
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", String.valueOf(temp));
        if(lastState.getTemperature() != temp)
            changes += getString(R.string.NotiAcTemperature) + "\n";
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlTemperature, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeAcMode(finalChanges);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(request);

    }

    private void changeAcMode(String changes) {
        Spinner modeFridge = (Spinner) findViewById(R.id.ModeAc);
        String newMode = modeFridge.getSelectedItem().toString();
        String urlMode = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setMode";

        if(! lastState.getFirstSpinner().equals(newMode)){
            changes += getString(R.string.NotiAcMode) + "\n";
        }
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", newMode);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlMode, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeVerticalSwing(finalChanges);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    private void changeVerticalSwing(String changes) {
        Spinner modeFridge = (Spinner) findViewById(R.id.VerticalSwing);
        String newMode = modeFridge.getSelectedItem().toString();
        String urlMode = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setVerticalSwing";

        if(! lastState.getSecondSpinner().equals(newMode)){
            changes += getString(R.string.NotiAcVS) + "\n";
        }
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", newMode);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlMode, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeHorizontalSwing(finalChanges);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    private void changeHorizontalSwing(String changes){

        Spinner modeFridge = (Spinner) findViewById(R.id.HorizontalSwing);
        String newMode = modeFridge.getSelectedItem().toString();
        String urlMode = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setHorizontalSwing";
        if(! lastState.getThirdSpinner().equals(newMode))
            changes += getString(R.string.NotiAcHS) + "\n";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", newMode);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlMode, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeFanSpeed(finalChanges);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);

    }

    private void changeFanSpeed(String changes){

        Spinner modeFridge = (Spinner) findViewById(R.id.FanSpeed);
        String newMode = modeFridge.getSelectedItem().toString();
        String urlMode = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/setFanSpeed";
        if(! lastState.getForthSpinner().equals(newMode)){
            changes += getString(R.string.NotiAcFS);
        }

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", newMode);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlMode, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(actualDevice.getNotState() && finalChanges.length() > 0){
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), SwitchedOn.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);

    }

    public void acceptLamp(View view) {
        notificationsDevices(actualDevice);
        final TextView temperature = (TextView) findViewById(R.id.BrightnessLamp);
        int brightness = Integer.parseInt(temperature.getText().toString());
        if(brightness < 0 || brightness > 100){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage(getString(R.string.ErrorLamp));
            dlgAlert.setTitle(getString(R.string.info));
            dlgAlert.setPositiveButton(getString(R.string.Accept),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        } else {
            changeLampStatus();
            Intent intent = new Intent(this, SwitchedOn.class);
            startActivity(intent);
        }
    }

    private void changeLampStatus() {
        String urlStatus;
        String changes = "";
        final ToggleButton statusButton = (ToggleButton) findViewById(R.id.StatusLamp);
        final boolean decide;
        if (statusButton.isChecked()) {
            urlStatus = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/turnOn";
            decide = true;
            if(decide != lastState.getStatus())
                changes += getString(R.string.NotiLampOn) + "\n";
        } else {
            urlStatus = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/turnOff";
            decide = false;
            if(decide != lastState.getStatus())
                changes += getString(R.string.NotiLampOn) + "\n";
        }
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        GsonRequest<ResultStateBoolean> request =
                new GsonRequest<ResultStateBoolean>(null, urlStatus, ResultStateBoolean.class, null, new com.android.volley.Response.Listener<ResultStateBoolean>() {
                    @Override
                    public void onResponse(ResultStateBoolean response) {
                        if (decide) {
                            changeBrightness(finalChanges);
                        }else if(actualDevice.getNotState() && finalChanges.length() > 0){
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), SwitchedOn.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        request.setTag("status");
        requestQueue.add(request);
    }

    private void changeBrightness(String changes) {

        String urlTemperature = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/changeBrightness";
        final TextView temperature = (TextView) findViewById(R.id.BrightnessLamp);
        int temp = Integer.parseInt(temperature.getText().toString());
        if(temp != lastState.getBrightness())
            changes += getString(R.string.NotiLampBright) +"\n";
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", String.valueOf(temp));
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        final String finalChanges1 = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlTemperature, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeColor(finalChanges1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(request);

    }

    private void changeColor(String changes) {

        String urlTemperature = "http://10.0.2.2:8080/api/devices/" + actualIdDevice + "/changeColor";
        if(! lastState.getFirstSpinner().equals(color))
            changes += getString(R.string.NotiColor);
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", color);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlTemperature, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(actualDevice.getNotState() && finalChanges.length() > 0) {
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), SwitchedOn.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SwitchedOn.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(request);

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, SwitchedOn.class);
        startActivity(refresh);
        finish();
    }

}
