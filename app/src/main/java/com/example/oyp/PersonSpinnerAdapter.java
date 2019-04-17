package com.example.oyp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oyp.R;

import java.util.ArrayList;

public class PersonSpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList<Integer> icons = new ArrayList<>();
    ArrayList<String> personName = new ArrayList<>();
    LayoutInflater inflter;


    public PersonSpinnerAdapter(Context applicationContext, ArrayList<Integer> icons, ArrayList<String> personName) {
        this.context = applicationContext;
        this.icons = icons;
        this.personName = personName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public int getCount() {return personName.size();    }


    public Object getItem(int i) {
        return personName.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.person_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        int gIcon = icons.get(i);
        icon.setImageResource(gIcon);
        String personNames = personName.get(i);
        names.setText(personNames);
        return view;
    }
}





