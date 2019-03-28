package com.example.oyp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    EditText mName, mPassword;
    Button logIn2;
    ProgressBar progressBar;
    ConnectionClass connectionClass;

    Connection conn;
    String un,pass,db,ip;

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

        progressBar=new ProgressBar(this);

        ip = "192.168.1.164";
        db = "oyp_database";
        un = "root";
        pass = "pass";


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

            progressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if(namestr.trim().equals("")||passstr.trim().equals(""))
                z = "Please enter all fields...";
            else
            {
                try {
                    conn = connectionclass(un, pass, db, ip);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    } else {

                        String query=" insert into testi values ('"+namestr+"', '"+passstr+"')";


                        Statement stmt = conn.createStatement();
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

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public Connection connectionclass(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try
        {
            Class.forName("org.mariadb.jdbc.Driver");
            ConnectionURL = "jdbc:mariadb://"+server+"/"+database;
            connection = DriverManager.getConnection(ConnectionURL, user, password);
        }
        catch (SQLException se){
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e){
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e){
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}