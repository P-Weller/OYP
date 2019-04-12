package com.example.oyp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.oyp.Fragments.ActivityFragment;
import com.example.oyp.Fragments.CreateTaskFragment;
import com.example.oyp.Fragments.MoreFragment;
import com.example.oyp.Fragments.TasksFragment;
import com.example.oyp.Fragments.UsersFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ClosedTasksActivity extends AppCompatActivity {

    Context thisContext;
    ArrayList<Integer> tImage = new ArrayList();
    ArrayList<String> tName = new ArrayList();
    ListView ctasksListView;

    public ClosedTasksActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.layout.fadein, R.layout.fadeout);
        setContentView(R.layout.activity_closed_task);

        thisContext = this;

        ctasksListView = findViewById(R.id.ctasksListView);
        Button openTaskBtn = findViewById(R.id.openTaskBtn);



        GetData retrieveData = new GetData();
        retrieveData.execute("");

        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TasksFragment()).commit();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_tasks:
                            selectedFragment = new TasksFragment();
                            break;
                        case R.id.navigation_activity:
                            selectedFragment = new ActivityFragment();
                            break;
                        case R.id.navigation_createTask:
                            selectedFragment = new CreateTaskFragment();
                            break;
                        case R.id.navigation_more:
                            selectedFragment = new MoreFragment();
                            break;
                        case R.id.navigation_users:
                            selectedFragment = new UsersFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }

            };



    public Connection connectionclass(String user, String password, String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

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


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;

            String ip = "192.168.1.164";
            String db = "oyp_database";
            String un = "root";
            String pass = "pass";

            ArrayList<String> activityIDs = new ArrayList<>();


            try {
                conn = connectionclass(un, pass, db, ip);

                stmt = conn.createStatement();
                String sql = "SELECT * FROM activity,task WHERE task.ActivityID = activity.ActivityID AND StatusID = 1";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    String aImageString = rs.getString("AIcon");
                    String aName = rs.getString("AName");

                    Resources res = getResources();
                    int aImage = res.getIdentifier(aImageString , "drawable", getPackageName());

                    tImage.add(i, aImage);
                    tName.add(i, aName);
                    i++;

                }


                System.out.println(tImage);
                System.out.println(tName);
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

            TasksAdapter tasksAdapter = new TasksAdapter(thisContext, tImage, tName);
            ctasksListView.setAdapter(tasksAdapter);
        }
    }
}

