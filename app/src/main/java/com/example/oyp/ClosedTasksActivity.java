package com.example.oyp;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

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
        setContentView(R.layout.activity_closed_task);

        thisContext = this;

        ctasksListView = findViewById(R.id.ctasksListView);

        GetData retrieveData = new GetData();
        retrieveData.execute("");
    }

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

