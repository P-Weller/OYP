package com.example.oyp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ActivityDetailActivity extends AppCompatActivity {

    TextView activityNameTextView;
    TextView activityDescriptionTextView;

    private static final String SHARED_PREF_NAME = "userdata";

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        //Get the view from activity_activitydetail.xml
        setContentView(R.layout.activity_activitydetail);

        activityNameTextView = findViewById(R.id.activityNameTextView);
        activityDescriptionTextView = findViewById(R.id.activityDescriptionTextView);

        GetData retrieveData = new GetData();
        retrieveData.execute("");

        deleteActivity();
    }

    private int getActivity(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        int activityID = sp.getInt("key_chosenacitivity", 0);
        System.out.println("2: " + activityID);
        return activityID;

    }

    private void deleteActivity(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);


        SharedPreferences settings = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        settings.edit().remove("key_chosenacitivity").apply();
        System.out.println("Ich wurde aufgrufen!");

    }


    public Connection connectionclass(String user, String password, String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            ConnectionURL = "jdbc:mariadb://" + server + "/" + database;
            connection = DriverManager.getConnection(ConnectionURL, user, password);
        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }

    private class GetData extends AsyncTask<String, String, String> {
        String msg = "";
        String aName = "";
        String aDescription = "";
        int activityID = getActivity();



        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;
            System.out.println("3: "+ activityID);
            String ip = "192.168.1.164";
            String db = "oyp_database";
            String un = "root";
            String pass = "pass";

            try {
                conn = connectionclass(un, pass, db, ip);

                stmt = conn.createStatement();
                String sql = "SELECT AName, ADescription FROM activity WHERE activityID = '" + activityID + "'";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    aName = rs.getString("AName");
                    aDescription = rs.getString("AName");
                    i++;
                }
                msg = "Process complete.";
                rs.close();
                stmt.close();
                conn.close();


            } catch (SQLException connError) {
                msg = "An exception was thrown by JDBC.";
                connError.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(activityID != 0) {
                activityNameTextView.setText(aName);
                activityDescriptionTextView.setText(aDescription);
            } else{
                activityNameTextView.setText("-");
                activityDescriptionTextView.setText("Unfortunately something went wrong. Please go back and try again! \nWe apologise for any inconvenience!");
            }

        }
    }

}