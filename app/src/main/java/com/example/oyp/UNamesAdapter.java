package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UNamesAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    ArrayList<String> uNames;
    ArrayList<String> uPoints;
    ArrayList<String> count;

    public UNamesAdapter(){

    }

    public UNamesAdapter(Context c, ArrayList<String> u, ArrayList<String> p, ArrayList<String> zahl){
        uNames = u;
        uPoints = p;
        count = zahl;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return uNames.size();
    }

    @Override
    public Object getItem(int i) {
        return uNames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = mInflator.inflate(R.layout.my_listview_detail, null);
        TextView uNamesTextView = v.findViewById(R.id. unamesTextView);
        TextView uPointsTextView = v.findViewById(R.id. upointsTextView);
        TextView countTextView = v.findViewById(R.id.countTextView);

        String uname = uNames.get(i);
        String upoint = uPoints.get(i);
        String number = count.get(i);


        uNamesTextView.setText(uname);
        uPointsTextView.setText(upoint);
        countTextView.setText(number);

        return v;
    }
}

