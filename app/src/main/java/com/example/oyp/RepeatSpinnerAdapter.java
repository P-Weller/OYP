package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RepeatSpinnerAdapter extends BaseAdapter{
    Context context;
    int icons[];
    ArrayList<String> repeatName = new ArrayList<>();

   //String[] repeatName;
   LayoutInflater inflter;

    public RepeatSpinnerAdapter(Context applicationContext, int[] icons, ArrayList<String> repeatName) {
        this.context = applicationContext;
        this.icons = icons;
        this.repeatName = repeatName;
        inflter = (LayoutInflater.from(applicationContext));
    }


    public int getCount() {
        return repeatName.size();
    }


    public Object getItem(int i) {
        return repeatName.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.repeat_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(icons[i]);
        String repeatNames = repeatName.get(i);
        names.setText(repeatNames);
        return view;
    }
}

