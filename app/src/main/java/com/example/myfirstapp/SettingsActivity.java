package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private FancyAdapter mFancyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.DeviceToolbar);
        setSupportActionBar(myToolbar);
        setTitle( getString(R.string.Settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String data [] = {getString(R.string.Languaje), getString(R.string.Notifications)};

        ListView listView;

        listView = (ListView) findViewById(R.id.ListView);

        mFancyAdapter = new FancyAdapter(data);
        listView.setAdapter(mFancyAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FancyAdapter extends BaseAdapter {

        private String[] mData;

        public FancyAdapter(String[] data) {
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public String getItem(int position) {
            return mData[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView result;

            if (convertView == null) {
                result = (TextView) getLayoutInflater().inflate(R.layout.settings_item, parent, false);
            } else {
                result = (TextView) convertView;
            }

            final String cheese = getItem(position);
            result.setText(cheese);
            result.setId(position);
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (view.getId()) {
                        case 0:
                            intent = new Intent(SettingsActivity.this, LanguageActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(SettingsActivity.this, NotificationsActivity.class);
                            startActivity(intent);
                        default:
                            break;
                    }
                }
            });

            return result;
        }

    }

}
