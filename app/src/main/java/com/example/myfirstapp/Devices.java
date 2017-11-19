package com.example.myfirstapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Devices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);

        setTitle("Devices");

        GridView gridView;

        gridView = (GridView) findViewById(R.id.GridView);
        Device hel1 = new Device("heladera","Fridge");
        Device hel2 = new Device("Puerta","Door");
        Device hel3 = new Device("Heladerita","Fridge");
        Device hel4 = new Device("Blinds","Blinds");
        Device hel5 = new Device("Lampara","Lamp");
        Device hel6 = new Device("Horni","Oven");
        Device hel7 = new Device("Airesito","Ac");


        final Device[] MOBILE_OS = new Device[] {
                hel1,hel2,hel3,hel4,hel5,hel7,hel6};

        gridView.setAdapter(new ImageAdapter(this, MOBILE_OS));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if(MOBILE_OS[position].getType() == "Blinds"){
                    setContentView(R.layout.blinds);
                    Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
                    setSupportActionBar(myToolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    setTitle(MOBILE_OS[position].getName());
                    TextView title = findViewById(R.id.BlindsTitle);
                    TextView desc = findViewById(R.id.BlindsDescription);
                    title.setText(MOBILE_OS[position].getName());
                    desc.setText("Description: ");

                }

                if(MOBILE_OS[position].getType() == "Door"){
                    setContentView(R.layout.door);
                    Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
                    setSupportActionBar(myToolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    setTitle(MOBILE_OS[position].getName());
                    TextView title = findViewById(R.id.DoorTitle);
                    TextView desc = findViewById(R.id.DoorDescription);
                    title.setText(MOBILE_OS[position].getName());
                    desc.setText("Description: ");

                    ToggleButton StatusButton = (ToggleButton) findViewById(R.id.DoorStatus);
                    final ToggleButton LockButton = (ToggleButton) findViewById(R.id.LockDoor);
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

                if(MOBILE_OS[position].getType() == "Ac"){
                    setContentView(R.layout.ac);
                    Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
                    setSupportActionBar(myToolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    setTitle(MOBILE_OS[position].getName());
                    TextView title = findViewById(R.id.AcTitle);
                    TextView desc = findViewById(R.id.AcDescription);
                    title.setText(MOBILE_OS[position].getName());
                    desc.setText("Description: ");

                    ToggleButton StatusButton = (ToggleButton) findViewById(R.id.AcStatus);



                    final TextView temperature = (TextView) findViewById(R.id.TemperatureAc);
                    final String lastTemprature = temperature.getText().toString();
                    final Spinner modeAc = (Spinner) findViewById(R.id.ModeAc);
                    final int lastMode = modeAc.getSelectedItemPosition();
                    final Spinner vertSwing = (Spinner) findViewById(R.id.VerticalSwing);
                    final int lastVS = vertSwing.getSelectedItemPosition();
                    final Spinner horSwing = (Spinner) findViewById(R.id.HorizontalSwing);
                    final int lastHS = horSwing.getSelectedItemPosition();
                    final Spinner fanSpeed = (Spinner) findViewById(R.id.FanSpeed);
                    final int lastFanSpeed = fanSpeed.getSelectedItemPosition();

                    if(! StatusButton.isChecked()){
                        temperature.setEnabled(false);
                        modeAc.setEnabled(false);
                        vertSwing.setEnabled(false);
                        horSwing.setEnabled(false);
                        fanSpeed.setEnabled(false);
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

                if(MOBILE_OS[position].getType() == "Oven"){
                    setContentView(R.layout.oven);
                    Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
                    setSupportActionBar(myToolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    setTitle(MOBILE_OS[position].getName());
                    TextView title = findViewById(R.id.OvenTitle);
                    TextView desc = findViewById(R.id.OvenDescription);
                    title.setText(MOBILE_OS[position].getName());
                    desc.setText("Description: ");

                    ToggleButton StatusButton = (ToggleButton) findViewById(R.id.StatusOven);
                    final TextView temperature = (TextView) findViewById(R.id.TemperatureOven);
                    final String lastTemprature = temperature.getText().toString();
                    final Spinner heatMode = (Spinner) findViewById(R.id.HeatMode);
                    final int lastHeatMode = heatMode.getSelectedItemPosition();
                    final Spinner grillMode = (Spinner) findViewById(R.id.GrillMode);
                    final int lastGrillMode = grillMode.getSelectedItemPosition();
                    final Spinner convMode = (Spinner) findViewById(R.id.ConvMode);
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


                if(MOBILE_OS[position].getType() == "Lamp"){
                    setContentView(R.layout.lamp);
                    Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
                    setSupportActionBar(myToolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    setTitle(MOBILE_OS[position].getName());
                    TextView title = findViewById(R.id.LampTitle);
                    TextView desc = findViewById(R.id.LampDescription);
                    title.setText(MOBILE_OS[position].getName());
                    desc.setText("Description: ");


                    ToggleButton StatusButton = (ToggleButton) findViewById(R.id.StatusLamp);
                    final TextView brightness = (TextView) findViewById(R.id.BrightnessLamp);
                    final String lastBrightness = brightness.getText().toString();
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

                if(MOBILE_OS[position].getType() == "Fridge"){
                    setContentView(R.layout.fridge);
                    Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
                    setSupportActionBar(myToolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    setTitle(MOBILE_OS[position].getName());
                    TextView title = findViewById(R.id.FridgeTitle);
                    TextView desc = findViewById(R.id.FridgeDesc);
                    title.setText(MOBILE_OS[position].getName());
                    desc.setText("Description: ");

                    final TextView temperature = (TextView) findViewById(R.id.FridgeTemperature);
                    final TextView freezerTemperature = (TextView) findViewById(R.id.FreezerTemeprature);
                    final Spinner fridgeMode = (Spinner) findViewById(R.id.FridgeMode);
                    final String lastTemperature = temperature.getText().toString();
                    final String lastFreezerTemperature = freezerTemperature.getText().toString();
                    final int modePosition = fridgeMode.getSelectedItemPosition();
                }

            }
        });


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

    public void createDevice(View view){

    }

    public void openHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }




}
