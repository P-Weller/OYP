package com.example.oyp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {

    TextView addActivityTextView;
    TextView addDescriptionTextView;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Get the view from fragment_addactivity
        setContentView(R.layout.fragment_addactivity);

        addActivityTextView = findViewById(R.id.addActivityTextView);
        addDescriptionTextView = findViewById(R.id.addDescriptionTextView);


    }
}
