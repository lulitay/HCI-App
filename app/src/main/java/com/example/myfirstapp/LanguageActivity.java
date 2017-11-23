package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Locale;

import static java.sql.DriverManager.println;

public class LanguageActivity extends AppCompatActivity {

    private FancyAdapter mFancyAdapter;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_configuration);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        setTitle(getString(R.string.Languaje));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList data = new ArrayList<String>();

        data.add(getString(R.string.English));
        data.add(getString(R.string.Español));

        ListView listView = (ListView) findViewById(R.id.ListView);

        listView.setAdapter(new FancyAdapter(this, data));
    }

    private class FancyAdapter extends ArrayAdapter<String>{
        private ArrayList data;
        private Context context;
        private ArrayList<RadioButton> buttons;

        public FancyAdapter(Context context, ArrayList<String> data) {
            super(context, R.layout.notifications_item, data);
            this.data = data;
            this.context = context;
            buttons = new ArrayList<>();
        }

        private class ViewHolder {
            TextView text;
            RadioButton s;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.language, null);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) convertView
                        .findViewById(R.id.text);
                viewHolder.s = (RadioButton) convertView
                        .findViewById(R.id.simpleSwitch);
                convertView.setTag(viewHolder);
                buttons.add(viewHolder.s);

                viewHolder.s.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        switch (position){
                            case 0:{
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
                                break;
                            }
                            case 1:{
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
                                break;

                            }
                            default:{
                                break;
                            }
                        }
                    }

                });




            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String temp = getItem(position);
            viewHolder.text.setText(temp);

            return convertView;
        }
    }


}
