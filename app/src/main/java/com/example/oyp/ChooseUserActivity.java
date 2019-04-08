package com.example.oyp;

import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ChooseUserActivity extends AppCompatActivity{

    Button createuserBtn;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Get the view from activity_chooseuser.xml
        setContentView(R.layout.activity_chooseuser);

        createuserBtn = (Button) findViewById(R.id.createuserBtn);

        createuserBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                    Intent intent = new Intent(ChooseUserActivity.this, CreateUserActivity.class);
                    startActivity(intent);


                }

        });


    }

}
