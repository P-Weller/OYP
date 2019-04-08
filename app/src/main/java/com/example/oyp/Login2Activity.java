package com.example.oyp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Login2Activity extends AppCompatActivity {



    TextView nametext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.oyp.R.layout.activity_login2);

        String name=getIntent().getStringExtra("name");

        nametext= (TextView) findViewById(com.example.oyp.R.id.name);

        nametext.setText(name);



    }
}

