package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UsercolorSpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList<Integer> icons = new ArrayList<>();
    ArrayList<String> colorName = new ArrayList<>();
    LayoutInflater inflter;

    public UsercolorSpinnerAdapter(Context context, ArrayList<Integer> icons, ArrayList<String> personName) {
        this.context = context;
        this.icons = icons;
        this.colorName = personName;
        inflter = (LayoutInflater.from(context));
    }

    public int getCount() {return colorName.size();    }


    public Object getItem(int i) {
        return colorName.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.usercolor_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        int gIcon = icons.get(i);
        icon.setImageResource(gIcon);
        String personNames = colorName.get(i);
        names.setText(personNames);
        return view;
    }
}
