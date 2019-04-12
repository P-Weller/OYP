package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ChooseUserAdapter extends BaseAdapter {
    LayoutInflater mInflator;
    String[] users;

    public ChooseUserAdapter(Context c, String[] u){
        users = u;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return users.length;
    }

    @Override
    public Object getItem(int i) {
        return users[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = mInflator.inflate(R.layout.chooseuser_detail, null);
        TextView usersTextView = (TextView) v.findViewById(R.id.usersTextView);
        String user = users[i];
        usersTextView.setText(user);
        return v;
    }
}


