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


public class ChooseUserListViewAdapter extends BaseAdapter {
    LayoutInflater mInflator;
    ArrayList<String> uNames;
    ArrayList<Integer> uImage;
    ArrayList<Integer> uColorID;
//create Constructor
    public ChooseUserListViewAdapter(Context c, ArrayList<Integer> i, ArrayList<String> u, ArrayList<Integer> a){
        uNames = u;
        uImage = i;
        uColorID=a;
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
        View v = mInflator.inflate(R.layout.chooseuser_detail, null);
        ImageView imageView = (ImageView) v.findViewById(R.id.profileImageView);
        TextView usersTextView = (TextView) v.findViewById(R.id.usersTextView);

        String userNames = uNames.get(i);
        int gIcon = uImage.get(i);
        int color = uColorID.get(i);

        imageView.setImageResource(gIcon);
        usersTextView.setText(userNames);
        if(color == 1){
            usersTextView.setTextColor(Color.RED);
        } else if(color == 2){
            usersTextView.setTextColor(Color.GREEN);
        } else if(color == 3){
            usersTextView.setTextColor(Color.YELLOW);
        } else if(color == 4){
            usersTextView.setTextColor(Color.rgb(238,0,238));
        } else if(color == 5){
            usersTextView.setTextColor(Color.BLACK);
        } else if(color == 6){
            usersTextView.setTextColor(Color.rgb(122,55,139));
        } else if(color == 7){
            usersTextView.setTextColor(Color.BLUE);
        } else if(color == 8){
            usersTextView.setTextColor(Color.rgb(255,165,0));
        } else if(color == 9){
            usersTextView.setTextColor(Color.GRAY);
        } else if(color == 10){
            usersTextView.setTextColor(Color.rgb(139,69,19));
        }
        return v;
    }
}


