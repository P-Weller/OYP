package com.example.oyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class uNamesAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] uNames;
    String[] uPoints;

    public uNamesAdapter(Context c, String[] u, String[] p){
        uNames = u;
        uPoints = p;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return uNames.length;
    }

    @Override
    public Object getItem(int i) {
        return uNames[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.user_list_view_detail, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView pointTextView = (TextView) v.findViewById(R.id.pointTextView);

        String name = uNames[i];
        String points = uPoints[i];

        nameTextView.setText(name);
        pointTextView.setText(points);

        return v;
    }
}
