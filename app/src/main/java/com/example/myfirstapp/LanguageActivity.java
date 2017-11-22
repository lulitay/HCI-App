package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Locale;

import static java.sql.DriverManager.println;

public class LanguageActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        setTitle(getString(R.string.Languaje));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setTitle(getString(R.string.Languaje));

        TextView first = findViewById(R.id.first);
        first.setText(getString(R.string.English));
        TextView second = findViewById(R.id.Second);
        second.setText(getString(R.string.Español));

        final RadioButton rd1 = findViewById(R.id.radioButton1);
        RadioButton rd2 = findViewById(R.id.radioButton2);
        rd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("silentMode", true);
                editor.commit();
                Intent intent = new Intent(LanguageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        rd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale myLocale = new Locale("es");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("silentMode", false);
                editor.commit();
                Intent intent = new Intent(LanguageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}