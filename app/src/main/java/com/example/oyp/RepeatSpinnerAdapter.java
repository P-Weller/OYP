package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RepeatSpinnerAdapter extends BaseAdapter{
    Context context;
    int icons[];
    String[] repeatName;
    LayoutInflater inflter;

    public RepeatSpinnerAdapter(Context applicationContext, int[] icons, String[] repeatName) {
        this.context = applicationContext;
        this.icons = icons;
        this.repeatName = repeatName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public int getCount() {
        return icons.length;
    }


    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.repeat_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(icons[i]);
        names.setText(repeatName[i]);
        return view;
    }
}

