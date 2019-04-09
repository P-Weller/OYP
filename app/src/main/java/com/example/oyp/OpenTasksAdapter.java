package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OpenTasksAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    int[] tImage;
    String[] tName;

    public OpenTasksAdapter(){

    }

    public OpenTasksAdapter(Context c, int[] i, String [] t){
        tImage = i;
        tName = t;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tName.length;
    }

    @Override
    public Object getItem(int i) {
        return tName[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = mInflator.inflate(R.layout.opentasks_detail, null);
        ImageView imageView = (ImageView) v.findViewById(R.id. imageView);
        TextView opentasksTextView = (TextView) v.findViewById(R.id. opentasksTextView);

        String name = tName[i];
        int bild = tImage[i];

        imageView.setImageResource(bild);
        opentasksTextView.setText(name);

        return v;
    }
}

