package com.example.oyp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class CreateTaskActivity extends AppCompatActivity {

    EditText personET;
    String contentpersonET;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        //Get the view from fragment_createtask.xml
        setContentView(R.layout.fragment_createtask);

        //Locate the button in fragment_createtask.xml
        personET = (EditText) findViewById(R.id.createTaskEditText);


    }

}