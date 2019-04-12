package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    ArrayList<Integer> aImage;
    ArrayList<String> aName;

    public ActivityAdapter(){

    }

    public ActivityAdapter(Context c, ArrayList<Integer> i, ArrayList<String> a){
        aImage = i;
        aName = a;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return aName.size();
    }

    @Override
    public Object getItem(int i) {
        return aName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = mInflator.inflate(R.layout.activities_detail, null);
        ImageView imageView = v.findViewById(R.id. imageView);
        TextView activityTextView = v.findViewById(R.id. activityTextView);

        String name = aName.get(i);
        int image = aImage.get(i);

        imageView.setImageResource(image);
        activityTextView.setText(name);

        return v;
    }
}