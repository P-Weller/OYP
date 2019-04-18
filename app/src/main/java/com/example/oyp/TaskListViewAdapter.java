package com.example.oyp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskListViewAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    ArrayList<Integer> tImage;
    ArrayList<String> tName;
    ArrayList<String> tUser;
    ArrayList<Integer> tColor;

    public TaskListViewAdapter(){

    }

    public TaskListViewAdapter(Context c, ArrayList<Integer> i, ArrayList<String> t, ArrayList<String> n,ArrayList<Integer> a){
        tImage = i;
        tName = t;
        tUser =n;
        tColor=a;
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
        int icon = tImage.get(i);
        int color = tColor.get(i);

        imageView.setImageResource(icon);
        opentasksTextView.setText(name);
        userTextView.setText(user);
        if(color == 1){
            opentasksTextView.setTextColor(Color.RED);
        } else if(color == 2){
            opentasksTextView.setTextColor(Color.GREEN);
        } else if(color == 3){
            opentasksTextView.setTextColor(Color.YELLOW);
        } else if(color == 4){
            opentasksTextView.setTextColor(Color.rgb(238,0,238));
        } else if(color == 5){
            opentasksTextView.setTextColor(Color.BLACK);
        } else if(color == 6){
            opentasksTextView.setTextColor(Color.rgb(122,55,139));
        } else if(color == 7){
            opentasksTextView.setTextColor(Color.BLUE);
        } else if(color == 8){
            opentasksTextView.setTextColor(Color.rgb(255,165,0));
        } else if(color == 9){
            opentasksTextView.setTextColor(Color.GRAY);
        } else if(color == 10){
            opentasksTextView.setTextColor(Color.rgb(139,69,19));
        }

        return v;
    }



}

