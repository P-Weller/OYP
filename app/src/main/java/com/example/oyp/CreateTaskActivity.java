package com.example.oyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateTaskActivity extends AppCompatActivity {

    EditText personET;
    String contentpersonET;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        //Get the view from activity_createtask.xml
        setContentView(R.layout.activity_createtask);

        //Locate the button in activity_createtask.xml
        personET = (EditText) findViewById(R.id.createTaskEditText);


    }

}