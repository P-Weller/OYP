package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UnamesAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    String[] uNames;
    String[] uPoints;
    String[] count;

    public UnamesAdapter(){

    }

    public UnamesAdapter(Context c, String[] u, String [] p, String[] zahl){
        uNames = u;
        uPoints = p;
        count = zahl;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return uNames.length;
    }

    @Override
    public Object getItem(int i) {
        return uNames[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = mInflator.inflate(R.layout.my_listview_detail, null);
        TextView unamesTextView = (TextView) v.findViewById(R.id. unamesTextView);
        TextView upointsTextView = (TextView) v.findViewById(R.id. upointsTextView);
        TextView countTextView = (TextView) v.findViewById(R.id.countTextView);

        String uname = uNames[i];
        String upoint = uPoints[i];
        String zahl = count[i];


        unamesTextView.setText(uname);
        upointsTextView.setText(upoint);
        countTextView.setText(zahl);

        return v;
    }
}

