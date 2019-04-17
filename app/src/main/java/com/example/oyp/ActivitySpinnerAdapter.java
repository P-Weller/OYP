package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivitySpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList<Integer> aIcons = new ArrayList<>();
    ArrayList<String> activityName = new ArrayList<>();
    LayoutInflater inflter;

    public ActivitySpinnerAdapter(Context context, ArrayList<Integer> icons, ArrayList<String> personName) {
        this.context = context;
        this.aIcons = icons;
        this.activityName = personName;
        inflter = (LayoutInflater.from(context));
    }

    public int getCount() {return activityName.size();    }


    public Object getItem(int i) {
        return activityName.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        int gIcon = aIcons.get(i);
        icon.setImageResource(gIcon);
        String personNames = activityName.get(i);
        names.setText(personNames);
        return view;
    }
}


