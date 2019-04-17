package com.example.oyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class SettingsActivity extends AppCompatActivity {

    Button changepasswordBtn;
    Button changeemailBtn;
    Button changehouseholdnameBtn;
    Button logoutandchangehouseholdBtn;



    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        //Get the view from activity_settings.xml
        setContentView(R.layout.activity_settings);

        //Locate the button in activity_settings.xml
        changepasswordBtn = (Button) findViewById(R.id.changepassword);
        changeemailBtn = (Button) findViewById(R.id.changeemail);
        changehouseholdnameBtn = (Button) findViewById(R.id.changehouseholdname);
        logoutandchangehouseholdBtn = (Button) findViewById(R.id.logoutandchangehouseholdBtn);

        //Caputre click on button "change email"
        changeemailBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                //Start ChangeemailActivity.class
                Intent intent = new Intent(SettingsActivity.this,ChangeemailActivity.class);
                startActivity(intent);

            }
        });

        //Caputre click on button "change household name"
        changehouseholdnameBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                //Start ChangeHouseholdActivity.class
                Intent intent = new Intent(SettingsActivity.this, ChangeHouseholdActivity.class);
                startActivity(intent);

            }
        });


        //Capture click on button "change password"
        changepasswordBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                //Start ChangePasswordActivity.class
                Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        //Capture click on button "Logout"
        logoutandchangehouseholdBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                //Start xxxxx.class
                Intent intent = new Intent(SettingsActivity.this,StartActivity.class);
                startActivity(intent);

            }


        });




    }

}