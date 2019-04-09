package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    int[] aImage;
    String[] aName;

    public ActivityAdapter(){

    }

    public ActivityAdapter(Context c, int[] i, String [] a){
        aImage = i;
        aName = a;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return aName.length;
    }

    @Override
    public Object getItem(int i) {
        return aName[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = mInflator.inflate(R.layout.activities_detail, null);
        ImageView imageView = (ImageView) v.findViewById(R.id. imageView);
        TextView activityTextView = (TextView) v.findViewById(R.id. activityTextView);

        String name = aName[i];
        int bild = aImage[i];

        imageView.setImageResource(bild);
        activityTextView.setText(name);

        return v;
    }
}