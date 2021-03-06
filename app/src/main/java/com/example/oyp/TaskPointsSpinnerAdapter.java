package com.example.oyp;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oyp.R;

import java.util.ArrayList;

public class TaskPointsSpinnerAdapter extends BaseAdapter {
    Context context;
    int icons[];
    ArrayList<String> pointsName = new ArrayList<>();
    LayoutInflater inflter;

    public TaskPointsSpinnerAdapter(Context applicationContext, int[] icons, ArrayList<String> pointsName) {
        this.context = applicationContext;
        this.icons = icons;
        this.pointsName = pointsName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public int getCount() {return pointsName.size();}


    public Object getItem(int i) {
        return pointsName.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.taskpoints_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(icons[i]);
        String pointsNames = pointsName.get(i);
        names.setText(pointsNames);
        return view;
    }

}





