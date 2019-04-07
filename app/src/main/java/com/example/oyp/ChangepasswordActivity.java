package com.example.oyp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.ResultSet;
import java.sql.Statement;


public class ChangepasswordActivity extends AppCompatActivity {

    Button changeBtn;
    EditText oldpassword;
    EditText newpassword;
    EditText new2password;

    String oldpass;
    String newpass;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        //Get the view from activity_changepassword.xml
        setContentView(R.layout.activity_changepassword);

        changeBtn = (Button) findViewById(R.id.changepasswordBtn);
        oldpassword = (EditText) findViewById(R.id.oldpasswordEditText);
        newpassword = (EditText) findViewById(R.id.newpasswordEditText);
        new2password = (EditText) findViewById(R.id.confirmpasswordEditText);

        changeBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent ioldpass = getIntent();
                oldpass = ioldpass.getStringExtra("oldpassw");
                if (oldpass == (oldpassword.getText().toString()) && newpassword == new2password){
                    newpass = newpassword.getText().toString();
                    Intent intentnew = new Intent(ChangepasswordActivity.this, MainActivity.class);
                    intentnew.putExtra("passnew", newpass);
                    startActivity(intentnew);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });







    }




    }

