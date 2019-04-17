package com.example.oyp;

import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.example.oyp.DBStrings.DATABASE_IP;
import static com.example.oyp.DBStrings.DATABASE_NAME;
import static com.example.oyp.DBStrings.DATABASE_PASSWORD;
import static com.example.oyp.DBStrings.DATABASE_USER;

public class LoginActivity extends AppCompatActivity {

    EditText mName, mPassword;
    Button logIn2;
    ProgressBar progressBar;
    ConnectionClass connectionClass;

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_LOGINHNAME = "key_householdname";
    private static final String KEY_LOGINHPASSWORD = "key_loginhpassword";

   Connection conn;

   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mName = (EditText) findViewById(R.id.mNameEditText);
        mPassword = (EditText) findViewById(R.id.mPasswordEditText);
        logIn2 = (Button) findViewById(R.id.logIn2Btn);


        connectionClass = new ConnectionClass();

        progressBar=new ProgressBar(this);


        logIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUsername();
                Dologin dologin=new Dologin();
                dologin.execute();
            }
        });
    }

    private void saveUsername(){
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);


        SharedPreferences.Editor editor = sp.edit();

        editor.putString(KEY_LOGINHNAME, name);
        editor.putString(KEY_LOGINHPASSWORD, password);


        editor.apply();
    }




    public class Dologin extends AsyncTask<String,String,String> {



        String namestr=mName.getText().toString();
        String passstr=mPassword.getText().toString();

        String z="Household name or password is wrong. \n               Please try again!";
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
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    }
                    else {

                        String query= "SELECT HName, HPassword FROM household WHERE HName = '"+namestr+"' AND HPassword = '"+passstr+"'";


                        Statement stmt = conn.createStatement();
                            stmt.executeUpdate(query);


                        ResultSet rs=stmt.executeQuery(query);

                        while (rs.next())

                        {
                            nm= rs.getString(1);
                            password=rs.getString(2);


                            if(nm.equals(namestr)&&password.equals(passstr))

                            {
                                isSuccess=true;
                                z = "Login successful!";

                            }
                            else {
                                isSuccess = false;
                                z = "Household or password is wrong. Please try again!";
                            }
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

                Intent intent=new Intent(LoginActivity.this,ChooseUserActivity.class);

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