package com.example.oyp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText mNameEditText, passwordEditText;
    Button logIn2Btn;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mNameEditText = (EditText) findViewById(R.id.name);
        passwordEditText = (EditText) findViewById(R.id.pass);
        logIn2Btn = (Button) findViewById(R.id.login);

        connectionClass = new ConnectionClass();

        progressDialog=new ProgressDialog(this);

        BackgroundActivity bg = new BackgroundActivity();
        bg.execute(MName, MPassword);

    }
}
