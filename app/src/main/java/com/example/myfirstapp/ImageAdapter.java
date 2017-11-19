package com.example.myfirstapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final Device[] mobileValues;

    public ImageAdapter(Context context, Device[] mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;
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
            textView.setText(mobileValues[position].getName());

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            String mobile = mobileValues[position].getName();

            switch(mobileValues[position].getType()){
                case "Fridge": imageView.setImageResource(R.drawable.heladera);break;
                case "Lamp" : imageView.setImageResource(R.drawable.lamp); break;
                case "Blinds" : imageView.setImageResource(R.drawable.blinds); break;
                case "Oven" : imageView.setImageResource(R.drawable.oven); break;
                case "Ac" : imageView.setImageResource(R.drawable.ac); break;
                case "Door" : imageView.setImageResource(R.drawable.door); break;
                default:break;
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return mobileValues.length;
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