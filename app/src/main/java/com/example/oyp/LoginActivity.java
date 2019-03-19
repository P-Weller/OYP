package com.example.oyp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText mName, mPassword;
    Button logIn2;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mName = (EditText) findViewById(R.id.mNameEditText);
        mPassword = (EditText) findViewById(R.id.passwordEditText);
        logIn2 = (Button) findViewById(R.id.logIn2Btn);

        connectionClass = new ConnectionClass();

        progressDialog=new ProgressDialog(this);
    }


}