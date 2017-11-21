package com.example.myfirstapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<Device> devices;

    public ImageAdapter(Context context, ArrayList<Device> mobileValues) {
        this.context = context;
        this.devices = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.mobile, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(devices.get(position).getName());

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            String mobile = devices.get(position).getName();

            switch(devices.get(position).getType()){
                case "rnizejqr2di0okho": imageView.setImageResource(R.drawable.heladera);break;
                case "go46xmbqeomjrsjr" : imageView.setImageResource(R.drawable.lamp); break;
                case "eu0v2xgprrhhg41g" : imageView.setImageResource(R.drawable.blinds); break;
                case "im77xxyulpegfmv8" : imageView.setImageResource(R.drawable.oven); break;
                case "li6cbv5sdlatti0j" : imageView.setImageResource(R.drawable.ac); break;
                case "lsf78ly0eqrjbz91" : imageView.setImageResource(R.drawable.door); break;
                default:break;
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
