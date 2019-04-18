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

    Connection conn;

    private static final String SHARED_PREF_NAME = "userdata";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("onCreate");



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



    public class DoLogin extends AsyncTask<String,String,String> {

        String nameStr=getHousehold();
        String passStr=getPassword();

        String z="Household name or password is wrong. \n               Please try again!";
        boolean isSuccess=false;

        String nm,password;



        @Override
        protected void onPreExecute() {



            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("DoInBackground");
            System.out.println(nameStr);
            System.out.println(passStr);
            if(nameStr.trim().equals("") || passStr.trim().equals("")) {
                System.out.println("I change to StartActivity now!");
                Intent intent = new Intent(MainEmptyActivity.this, StartActivity.class);
                intent.putExtra("name", nameStr);
                startActivity(intent);
            } else {
                try {
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    } else {

                        String query = "SELECT HName, HPassword FROM household WHERE HName = '" + nameStr + "' AND HPassword = '" + passStr + "'";


                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate(query);


                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next()) {
                            nm = rs.getString("HName");
                            password = rs.getString("HPassword");


                            if (nm.equals(nameStr) && password.equals(passStr)) {
                                isSuccess = true;
                                System.out.println("I change to MainActivity now!");
                                Intent intent = new Intent(MainEmptyActivity.this, MainActivity.class);
                                intent.putExtra("name", nameStr);
                                startActivity(intent);

                            } else {
                                isSuccess = false;
                                System.out.println("I change to MainActivity now!");
                                Intent intent = new Intent(MainEmptyActivity.this, StartActivity.class);
                                intent.putExtra("name", nameStr);
                                startActivity(intent);
                            }
                        }
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions" + ex;
                }
            }
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
