package com.example.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.android.volley.*;

public class NotificationsActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_configuration);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Context context = this.getApplicationContext();
        final ArrayList<Device> devices = new ArrayList<Device>();
        String url = "http://10.0.2.2:8080/api/devices";
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        GsonRequest<getDevicesResponse> request =
                new GsonRequest<getDevicesResponse>(url, getDevicesResponse.class, null, new com.android.volley.Response.Listener<getDevicesResponse>() {
                    @Override
                    public void onResponse(getDevicesResponse response) {
                        //Toast.makeText(Devices.this, "Bien", Toast.LENGTH_LONG).show();
                        devices.clear();
                        devices.addAll(response.getDevices());
                        for(Device dev: devices){
                            Log.d("name:",dev.getName());
                            Log.d("type",dev.getType());
                        }

                        ListView listView = (ListView) findViewById(R.id.ListView);

                        ArrayList<String> datas = new ArrayList<String>();
                        for(Device d : devices){
                            datas.add(d.getName());
                        }

                        listView.setAdapter(new FancyAdapter(NotificationsActivity.this, datas, devices));
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("dsa", error.toString());
                        Toast.makeText(NotificationsActivity.this, "error desconocido", Toast.LENGTH_LONG).show();
                    }
                });
        request.setTag("asd");
        requestQueue.add(request);
    }

    private class FancyAdapter extends ArrayAdapter<String>{
        private ArrayList<String> data;
        private Context context;
        private ArrayList<Device> devices;

        public FancyAdapter(Context context, ArrayList<String> data, ArrayList<Device> devices) {
            super(context, R.layout.notifications_item, data);
            this.data = data;
            this.context = context;
            this.devices = devices;
        }

        private class ViewHolder {
            TextView text;
            Switch s;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.notifications_item, null);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) convertView
                        .findViewById(R.id.text);
                viewHolder.s = (Switch) convertView
                        .findViewById(R.id.simpleSwitch);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String temp = getItem(position);
            viewHolder.text.setText(temp);
            viewHolder.s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        devices.get(position).setNotState(true);
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean(devices.get(position).getId(), true);
                        editor.commit();

                    }
                    else {
                        devices.get(position).setNotState(false);
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean(devices.get(position).getId(), false);
                        editor.commit();
                    }
                }
            });

            setPreference(viewHolder.s,position,devices);
            return convertView;
        }

    }

    public void setPreference(Switch s, int position, ArrayList<Device> devices){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent = settings.getBoolean(devices.get(position).getId(), false);
        s.setChecked(silent);
        //if(silent)
        //Toast.makeText(NotificationsActivity.this, devices.get(position).getName(), Toast.LENGTH_LONG).show();


    }

}