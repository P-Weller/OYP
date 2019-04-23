package com.example.oyp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.oyp.HelpActivity;
import com.example.oyp.R;
import com.example.oyp.SettingsActivity;
import com.example.oyp.StartActivity;
import com.example.oyp.TaskDetailActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.oyp.DBStrings.DATABASE_IP;
import static com.example.oyp.DBStrings.DATABASE_NAME;
import static com.example.oyp.DBStrings.DATABASE_PASSWORD;
import static com.example.oyp.DBStrings.DATABASE_USER;

public class MoreFragment extends Fragment {


    //Fragment onCreateView implementation:
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // set reference to the fragment_more xml-file:
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        TimerTask action = new TimerTask() {
            public void run() {
                GetID getID = new GetID();
                getID.execute();
            }
        };

        Timer caretaker = new Timer();
        caretaker.schedule(action, 1000, 5000);

        //Locate the button in fragment_more.xml
        Button mlogoutBtn = (Button) view.findViewById(R.id.logoutBtn);
        Button helpBtn =(Button) view.findViewById(R.id.helpBtn);
        Button settingsBtn = (Button) view.findViewById(R.id.settingsBtn);

        //Capture click on button "Logout"
        mlogoutBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Start StartActivity.class
                clearSharedPref();
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
            }
        });


        //Capture click on button "Help"
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
            }
        });


        //Capture click on button "Settings"
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    // Clear all shared preferences
    private void clearSharedPref() {

        SharedPreferences sp = this.getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.clear().apply();
    }

    // Get the TaskID because of the Activity-Name:
    private class GetID extends AsyncTask<String, String, String> {
        String msg = "";
        String tName;
        ArrayList<Integer> taskID = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                stmt = conn.createStatement();
                String sql = "SELECT TaskID FROM task";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    int j = rs.getInt("TaskID");
                    taskID.add(j);
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

            System.out.println("1: " + taskID);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }


    //Connection to database
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
}