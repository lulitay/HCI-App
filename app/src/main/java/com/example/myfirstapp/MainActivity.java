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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String actualIdDevice;
    Device actualDevice;
    LastState lastState = new LastState();
    boolean asd = true;
    public static final String PREFS_NAME = "MyPrefsFile";
    View actualLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent = settings.getBoolean("silentMode", false);
        String language;
        if(silent)
            language = "en";
        else
            language = "es";
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout); //con esto el decimos cual es el layout grafico, osea donde estan las ID's
        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        //createTabHost(savedInstanceState);
        setTitle("Smart Home");
        saveClass();
        Log.d("asd", "whut");
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
                        for (Device dev : devices) {
                            Log.d("name:", dev.getName());
                            Log.d("type", dev.getType());
                        }
                        updateDevices(devices);
                        UpdateOnDevices(devices);
                        notificationsDevices(devices);
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        request.setTag("asd");
        requestQueue.add(request);

        //Toast.makeText(MainActivity.this, "Finalizado", Toast.LENGTH_LONG).show();

    }

    public void saveClass(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("class", "MainActivity");
        editor.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    public void setLanguaje(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent = settings.getBoolean("silentMode", false);
        String language;
        if(silent)
            language = "en";
        else
            language = "es";
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    private void notificationsDevices(ArrayList<Device> devices){
        for(Device dev: devices){
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            boolean silent = settings.getBoolean(dev.getId(), false);
            dev.setNotState(silent);
        }
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

    public void updateDevices(final ArrayList<Device> devices) {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        final LinearLayout[] individual = new LinearLayout[devices.size()];
        for (int i = 0; i < devices.size(); i++)
            individual[i] = new LinearLayout(this);

        for (int i = 0; i < devices.size(); i++) {
            LinearLayout lin = individual[i];
            lin.addView(createView(devices.get(i)));
            lin.setPadding(0, 0, 30, 0);
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

    public void UpdateOnDevices(final ArrayList<Device> devices) {
        LinearLayout linearLayout = findViewById(R.id.linearLayout2);
        for (int i = 0; i < devices.size(); i++) {
            deviceIsOn(devices.get(i), i, linearLayout, devices);
        }
    }

    public void createSwitchedOnDevice(Device device, int i, LinearLayout linearLayout, final ArrayList<Device> devices) {
        final LinearLayout[] individual = new LinearLayout[devices.size()];
        individual[i] = new LinearLayout(this);
        LinearLayout lin = individual[i];
        lin.addView(createView(devices.get(i)));
        lin.setPadding(0, 0, 30, 0);
        lin.setClickable(true);
        lin.setOnClickListener(new CustomOnClickListener(i) {
            @Override
            public void onClick(View v) {
                startLayout(devices, this.index);
            }
        });
        linearLayout.addView(lin);
    }

    public void deviceIsOn(final Device device, final int i, final LinearLayout linearLayout, final ArrayList<Device> devices) {
        if (device.getType().equals("lsf78ly0eqrjbz91") || device.getType().equals("eu0v2xgprrhhg41g") || device.getType().equals("rnizejqr2di0okho"))
            return;

        final Boolean[] result = {null};
        if (device.getType().equals("li6cbv5sdlatti0j")) {
            String url = "http://10.0.2.2:8080/api/devices/" + device.getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetAcState> request =
                    new GsonRequest<GetAcState>(true, url, GetAcState.class, null, new com.android.volley.Response.Listener<GetAcState>() {
                        @Override
                        public void onResponse(GetAcState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            if (response.getResult().getStatus().equals("on"))
                                createSwitchedOnDevice(device, i, linearLayout, devices);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);
        }

        if (device.getType().equals("im77xxyulpegfmv8")) {
            String url = "http://10.0.2.2:8080/api/devices/" + device.getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetOvenState> request =
                    new GsonRequest<GetOvenState>(true, url, GetOvenState.class, null, new com.android.volley.Response.Listener<GetOvenState>() {
                        @Override
                        public void onResponse(GetOvenState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            if (response.getResult().getStatus().equals("on"))
                                createSwitchedOnDevice(device, i, linearLayout, devices);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);
        }


        if (device.getType().equals("go46xmbqeomjrsjr")) {

            String url = "http://10.0.2.2:8080/api/devices/" + device.getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetLampState> request =
                    new GsonRequest<GetLampState>(true, url, GetLampState.class, null, new com.android.volley.Response.Listener<GetLampState>() {
                        @Override
                        public void onResponse(GetLampState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            if (response.getResult().getStatus().equals("on"))
                                createSwitchedOnDevice(device, i, linearLayout, devices);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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

    public void colorPicker(View view){
        ColorPickerDialogBuilder
                .with(MainActivity.this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Toast.makeText(MainActivity.this, "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_LONG).show();
                        Toast.makeText(MainActivity.this, "onColorSelected: 0x" + Integer.toHexString(selectedColor).substring(2), Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        Toast.makeText(MainActivity.this, "onClick 1: onColorSelected: 0x" + Integer.toHexString(selectedColor).substring(2), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "onClick 2", Toast.LENGTH_LONG).show();
                    }
                })
                .build()
                .show();
    }

    private void startLayout(final ArrayList<Device> devices, final int position) {
        actualIdDevice = devices.get(position).getId();
        actualDevice = devices.get(position);
        if (devices.get(position).getType().equals("eu0v2xgprrhhg41g")) {
            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
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
                            Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);

        }

        if (devices.get(position).getType().equals("lsf78ly0eqrjbz91")) {
            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            GsonRequest<GetDoorState> request =
                    new GsonRequest<GetDoorState>(true, url, GetDoorState.class, null, new com.android.volley.Response.Listener<GetDoorState>() {
                        @Override
                        public void onResponse(GetDoorState response) {
                            //Toast.makeText(MainActivity.this, response.getResult().getLock(), Toast.LENGTH_LONG).show();
                            UpdateDoor(devices.get(position), response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("dsa", error.toString());
                            Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);

        }

        if (devices.get(position).getType().equals("li6cbv5sdlatti0j")) {
            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
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
                            Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);
        }

        if (devices.get(position).getType().equals("im77xxyulpegfmv8")) {
            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
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
                            Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);
        }


        if (devices.get(position).getType().equals("go46xmbqeomjrsjr")) {

            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
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
                            Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);

        }

        if (devices.get(position).getType().equals("rnizejqr2di0okho")) {
            String url = "http://10.0.2.2:8080/api/devices/" + devices.get(position).getId() + "/getState";
            RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
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
                            Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                        }
                    });
            request.setTag("asd");
            requestQueue.add(request);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
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


        ToggleButton StatusButton = (ToggleButton) findViewById(R.id.StatusLamp);


        final TextView brightness = (TextView) findViewById(R.id.BrightnessLamp);
        final String lastBrightness = String.valueOf(response.getResult().getBrightness());
        brightness.setText(lastBrightness);
        lastState.setBrightness(response.getResult().getBrightness());

        if (response.getResult().getStatus().equals("on")) {
            StatusButton.setChecked(true);
            brightness.setEnabled(true);
            lastState.setStatus(true);
        } else {
            StatusButton.setChecked(false);
            brightness.setEnabled(false);
            lastState.setStatus(false);

        }
        StatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    brightness.setEnabled(false);
                    brightness.setText(lastBrightness);
                } else {
                    brightness.setEnabled(true);
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
                                Notifications noti = new Notifications(getString(R.string.NotiBlindsOpen),actualDevice.getName() , MainActivity.this);
                                noti.build();
                                noti.addToLodge();
                            }else{
                                Notifications noti = new Notifications(getString(R.string.NotiBlindsClosed),actualDevice.getName() , MainActivity.this);
                                noti.build();
                                noti.addToLodge();
                            }
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        request.setTag("asd");
        requestQueue.add(request);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void acceptDoor(View view) {
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
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), MainActivity.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        requestLock.setTag("lock");
        requestQueue.add(requestLock);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void acceptFridge(View view){
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
            Intent intent = new Intent(this, MainActivity.class);
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
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), MainActivity.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(request);

    }

    public void acceptOven(View view) {
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
            Intent intent = new Intent(this, MainActivity.class);
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
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), MainActivity.this);
                            noti.build();
                            noti.addToLodge();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                        Notifications noti = new Notifications(finalChanges, actualDevice.getName(), MainActivity.this);
                        noti.build();
                        noti.addToLodge();
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);

    }

    public void acceptAc(View view) {
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
            Intent intent = new Intent(this, MainActivity.class);
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
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), MainActivity.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
                        Notifications noti = new Notifications(finalChanges, actualDevice.getName(), MainActivity.this);
                        noti.build();
                        noti.addToLodge();
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);

    }

    public void acceptLamp(View view) {
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
            Intent intent = new Intent(this, MainActivity.class);
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
                            Notifications noti = new Notifications(finalChanges, actualDevice.getName(), MainActivity.this);
                            noti.build();
                            noti.addToLodge();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
            changes += getString(R.string.NotiLampBright);
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("0", String.valueOf(temp));
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String finalChanges = changes;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, urlTemperature, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    if(actualDevice.getNotState() && finalChanges.length() > 0) {
                        Notifications noti = new Notifications(finalChanges, actualDevice.getName(), MainActivity.this);
                        noti.build();
                        noti.addToLodge();
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, getString(R.string.errorConnection), Toast.LENGTH_LONG).show();
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
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }
}

