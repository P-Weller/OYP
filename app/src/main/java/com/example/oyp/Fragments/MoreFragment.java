package com.example.oyp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.oyp.HelpActivity;
import com.example.oyp.R;
import com.example.oyp.SettingsActivity;
import com.example.oyp.StartActivity;

public class MoreFragment extends Fragment {



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        View view = inflater.inflate(R.layout.fragment_more, container, false);


        //Locate the button in fragment_more.xml
        Button mlogoutBtn = (Button) view.findViewById(R.id.logoutBtn);
        Button helpBtn =(Button) view.findViewById(R.id.helpBtn);
        Button settingsBtn = (Button) view.findViewById(R.id.settingsBtn);

        //Capture click on button "Logout"
        mlogoutBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                //Start StartActivity.class
                clearSharedPref();
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);

            }


        });

        //Capture click on button "Help"
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
            }
        });


        //Capture click on button "Settings"
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void clearSharedPref() {

        SharedPreferences sp = this.getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.clear().apply();
    }

}