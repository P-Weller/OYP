package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChooseUserAdapter extends BaseAdapter {
    LayoutInflater mInflator;
    ArrayList<String> uNames;
    ArrayList<Integer> uImage;
//create Constructor
    public ChooseUserAdapter(Context c, ArrayList<Integer> i, ArrayList<String> u){
        uNames = u;
        uImage = i;
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

        imageView.setImageResource(gIcon);

        usersTextView.setText(userNames);
        return v;
    }
}


