package com.example.oyp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class ChooseUserActivity extends AppCompatActivity{

    //List View for users
    ListView usersListView;
    Button createuserBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the view from activity_chooseuser.xml
        setContentView(R.layout.activity_chooseuser);

        Resources res = getResources();
        usersListView = (ListView) findViewById(R.id.usersListView);
        String[] users = {"Pascal", "Jojo","Julia"};

        ChooseUserAdapter usersAdapter = new ChooseUserAdapter(this, users);
        usersListView.setAdapter(usersAdapter);


        createuserBtn = (Button) findViewById(R.id.createuserBtn);

        createuserBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                    Intent intent = new Intent(ChooseUserActivity.this, CreateUserActivity.class);
                    startActivity(intent);


                }

        });


    }

}


