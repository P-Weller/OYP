package com.example.oyp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {


    Button signupbtn;
    Button loginbtn;



    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        //Get the view from activity_start.xml
        setContentView(R.layout.activity_start);

        //Locate the button in activity_start.xml
        loginbtn = (Button) findViewById(R.id.StartloginBtn);
        signupbtn = (Button) findViewById(R.id.StartsignupBtn);

        //Capture login button clicks
        loginbtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                //Start LoginActivity.class
                Intent intent = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(intent);

            }

        });

        //Capture signup button clicks
        signupbtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                //Start LoginActivity.class
                Intent intent = new Intent(StartActivity.this,RegistrationActivity.class);
                startActivity(intent);

            }

        });

    }
    @Override
    public void onBackPressed() {
        Log.e("######","You can't get out!");
    }






}
