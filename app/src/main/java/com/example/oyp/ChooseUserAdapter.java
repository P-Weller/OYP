package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ChooseUserAdapter extends BaseAdapter {
    LayoutInflater mInflator;
    ArrayList<String> users;
//create Constructor
    public ChooseUserAdapter(Context c, ArrayList<String> u){
        users = u;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = mInflator.inflate(R.layout.chooseuser_detail, null);
        TextView usersTextView = (TextView) v.findViewById(R.id.usersTextView);
        String user = users.get(i);
        usersTextView.setText(user);
        return v;
    }
}


