package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TasksAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    ArrayList<Integer> tImage;
    ArrayList<String> tName;
    ArrayList<String> tUser;

    public TasksAdapter(){

    }

    public TasksAdapter(Context c, ArrayList<Integer> i, ArrayList<String> t,ArrayList<String> n){
        tImage = i;
        tName = t;
        tUser =n;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tName.size();
    }

    @Override
    public Object getItem(int i) {
        return tName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = mInflator.inflate(R.layout.tasks_detail, null);
        ImageView imageView = v.findViewById(R.id. imageView);
        TextView opentasksTextView = v.findViewById(R.id. opentasksTextView);
        TextView userTextView= v.findViewById(R.id.userTextView);

        String name = tName.get(i);
        String user = tUser.get(i);
        int bild = tImage.get(i);

        imageView.setImageResource(bild);
        opentasksTextView.setText(name);
        userTextView.setText(user);



        return v;
    }

}

