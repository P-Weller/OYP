package com.example.oyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MoreActivity extends AppCompatActivity {

    Button settingsBtn;
    Button notificationsBtn;
    Button helpBtn;
    Button mlogoutBtn;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        //Get the view from activity_createtask.xml
        setContentView(R.layout.activity_more);

        //Locate the button in activity_createtask.xml
        mlogoutBtn = (Button) findViewById(R.id.logoutBtn);
        helpBtn =(Button) findViewById(R.id.helpBtn);
        notificationsBtn = (Button) findViewById(R.id.notificationsBtn);
        settingsBtn = (Button) findViewById(R.id.settingsBtn);

        //Capture edittext clicks
        mlogoutBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                //Start xxxxx.class
                Intent intent = new Intent(MoreActivity.this,StartActivity.class);
                startActivity(intent);

            }


        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreActivity.this,HelpActivity.class);
                startActivity(intent);
            }
        });

        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreActivity.this,NotificationsActivity.class);
                startActivity(intent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

}