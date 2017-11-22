package com.example.myfirstapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;

import static java.sql.DriverManager.println;

public class LanguageActivity extends AppCompatActivity {

    private FancyAdapter mFancyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_configuration);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        setTitle("Language");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList data = new ArrayList<String>();

        data.add("a");
        data.add("b");

        ListView listView = (ListView) findViewById(R.id.ListView);

        listView.setAdapter(new FancyAdapter(this, data));
    }

    private class FancyAdapter extends ArrayAdapter<String>{
        private ArrayList data;
        private Context context;

        public FancyAdapter(Context context, ArrayList<String> data) {
            super(context, R.layout.notifications_item, data);
            this.data = data;
            this.context = context;
        }

        private class ViewHolder {
            TextView text;
            RadioButton s;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.language, null);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) convertView
                        .findViewById(R.id.text);
                viewHolder.s = (RadioButton) convertView
                        .findViewById(R.id.simpleSwitch);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String temp = getItem(position);
            viewHolder.text.setText(temp);

            return convertView;
        }
    }

}