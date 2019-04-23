package com.example.oyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mysql.jdbc.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.oyp.DBStrings.DATABASE_IP;
import static com.example.oyp.DBStrings.DATABASE_NAME;
import static com.example.oyp.DBStrings.DATABASE_PASSWORD;
import static com.example.oyp.DBStrings.DATABASE_USER;

public class MainEmptyActivity extends AppCompatActivity {

    TextView noConnectionTextView;
    Button retryBtn;

    private static final String SHARED_PREF_NAME = "userdata";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainempty);
        noConnectionTextView = findViewById(R.id.noConnectionTextView);
        retryBtn = findViewById(R.id.retryBtn);
        retryBtn.setVisibility(View.GONE);
        noConnectionTextView.setVisibility(View.GONE);


        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }

        });

        DoLogin doLogin = new DoLogin();
        doLogin.execute("");
    }

    private String getHousehold(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String household = sp.getString("key_householdname", "");
        return household;
    }

    private String getPassword(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String household = sp.getString("key_loginhpassword", "");
        return household;
    }

    private int getUser(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        int userID = sp.getInt("key_chosenuser",0);
        return userID;
    }






    public class DoLogin extends AsyncTask<String,String,String> {

        String hNameStr=getHousehold();
        String passStr=getPassword();
        int userID=getUser();
        Connection conn;

        String z="Household name or password is wrong. \n               Please try again!";
        boolean isSuccess=false;

        String nm,password;



        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("Ich versuche es wenigstens1");
            try {
                    System.out.println("Ich versuche es wenigstens");
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    if (conn == null) {
                        z = "Please check your internet connection";
                        noConnectionTextView.setVisibility(View.VISIBLE);
                        retryBtn.setVisibility(View.VISIBLE);
                    } else {

                        if(hNameStr.trim().equals("") || passStr.trim().equals("")) {
                            System.out.println("I change to StartActivity now!");
                            Intent intent = new Intent(MainEmptyActivity.this, StartActivity.class);
                            intent.putExtra("name", hNameStr);
                            startActivity(intent);
                        } else if(userID == 0) {
                            Intent intent = new Intent(MainEmptyActivity.this, ChooseUserActivity.class);
                            intent.putExtra("name", hNameStr);
                            startActivity(intent);
                        } else {

                            String query = "SELECT HName, HPassword FROM household WHERE HName = '" + hNameStr + "' AND HPassword = '" + passStr + "'";


                            Statement stmt = conn.createStatement();
                            stmt.executeUpdate(query);


                            ResultSet rs = stmt.executeQuery(query);

                            while (rs.next()) {
                                nm = rs.getString("HName");
                                password = rs.getString("HPassword");


                                if (nm.equals(hNameStr) && password.equals(passStr)) {
                                    isSuccess = true;
                                    System.out.println("I change to MainActivity now!");
                                    Intent intent = new Intent(MainEmptyActivity.this, MainActivity.class);
                                    intent.putExtra("name", hNameStr);
                                    startActivity(intent);

                                } else {
                                    isSuccess = false;
                                    System.out.println("I change to MainActivity now!");
                                    Intent intent = new Intent(MainEmptyActivity.this, StartActivity.class);
                                    intent.putExtra("name", hNameStr);
                                    startActivity(intent);
                                }
                            }
                            stmt.close();
                            rs.close();
                        }

                        }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions" + ex;
                }
            System.out.println("1. TRY" + conn);


            return z;
            }

        @Override
        protected void onPostExecute(String s) {

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
