package com.example.oyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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


        logIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dologin dologin=new Dologin();
                dologin.execute();
            }
        });
    }

    private class Dologin extends AsyncTask<String,String,String> {


        String namestr=mName.getText().toString();
        String passstr=mPassword.getText().toString();
        String z="";
        boolean isSuccess=false;

        String nm,password;


        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Loading...");
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if(namestr.trim().equals("")||passstr.trim().equals(""))
                z = "Please enter all fields...";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    z = "Connection successful";
                    if (con == null) {
                        z = "Please check your internet connection";
                    } else {

                        String query=" select * from master where mName='"+namestr+"' and mPassword = '"+passstr+"'";


                        Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);


                        ResultSet rs=stmt.executeQuery(query);

                        while (rs.next())

                        {
                            nm= rs.getString(2);
                            password=rs.getString(3);




                            if(nm.equals(namestr)&&password.equals(passstr))
                            {

                                isSuccess=true;
                                z = "Login successful";

                            }

                            else

                                isSuccess=false;



                        }





                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions"+ex;
                }
            }
            return z;        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show();


            if(isSuccess) {

                Intent intent=new Intent(LoginActivity.this,Login2Activity.class);

                intent.putExtra("name",namestr);

                startActivity(intent);
            }


            progressDialog.hide();

        }
    }
}