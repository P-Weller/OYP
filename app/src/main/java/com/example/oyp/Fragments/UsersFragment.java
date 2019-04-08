package com.example.oyp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.oyp.R;
import com.example.oyp.UnamesAdapter;

public class UsersFragment extends Fragment {

    public UsersFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        String[] uNames = {"Patricia", "Paul", "Lenio"};
        String[] uPoints = {"43", "44", "45"};

        int j = uNames.length;
        String[] count = new String[j];
        for (int k = 0; k<j; k++){
            count[k] = k+1 + ".";
        }

        ListView myListView = (ListView) view.findViewById(R.id.myListView);

        UnamesAdapter unamesAdapter = new UnamesAdapter(this.getContext(), uNames, uPoints, count);
        myListView.setAdapter(unamesAdapter);
        return view;

    }
}


