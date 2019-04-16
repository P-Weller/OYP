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


public class TaskDetailActivity extends AppCompatActivity {

    TextView taskDetailTextView;
    TextView userDetailTextView;
    TextView dateTimeDetailTextView;
    TextView repeatDetailTextView;
    TextView pointsDetailTextView;

    private static final String SHARED_PREF_NAME = "userdata";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Get the view from activity_taskdetails.xml
        setContentView(R.layout.activity_taskdetails);


        taskDetailTextView = findViewById(R.id.taskDetailTextView);
        userDetailTextView = findViewById(R.id.userDetailTextView);
        dateTimeDetailTextView = findViewById(R.id.dateTimeDetailTextView);
        repeatDetailTextView = findViewById(R.id.repeatDetailTextView);
        pointsDetailTextView = findViewById(R.id.pointsDetailTextView);

        GetData retrieveData = new GetData();
        retrieveData.execute("");

        deleteTask();
    }


    private int getTask() {

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        int taskID = sp.getInt("key_chosentask", 0);
        System.out.println("2: " + taskID);
        return taskID;

    }

    private void deleteTask() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        sp.edit().remove("key_chosentask").apply();
        System.out.println("TASKDELETE: Ich wurde aufgrufen!");

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
        int taskID = getTask();
        String tName = "";
        String tUser = "";
        String tDateTime = "";
        String tRepeat = "";
        String tPoints = "";


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

            int tNameID = 0;
            int tUserID = 0;
            int tRepeatID = 0;
            String tDate = "";
            String tTime = "";

            try {
                conn = connectionclass(un, pass, db, ip);

                stmt = conn.createStatement();
                String sql = "SELECT * FROM task WHERE TaskID = '" + taskID + "'";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    tNameID = rs.getInt("ActivityID");
                    tUserID = rs.getInt("UserID");
                    tDate = rs.getString("TDate");
                    tTime = rs.getString("TTime");
                    tRepeatID = rs.getInt("RepeatID");
                    tPoints = rs.getString("TPoints");
                }

                msg = "Process complete.";

                stmt = conn.createStatement();
                System.out.println("tNameID: " + tNameID);
                sql = "SELECT AName FROM activity WHERE ActivityID = '" + tNameID + "'";
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    tName = rs.getString("AName");
                }



                stmt = conn.createStatement();
                System.out.println("tUserID: " + tUserID);
                sql = "SELECT UName FROM user WHERE UserID = '" + tUserID + "'";
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    tUser = rs.getString("UName");
                }


                stmt = conn.createStatement();
                System.out.println("tRepeatID: " + tRepeatID);
                sql = "SELECT * FROM `repeat` WHERE RepeatID = '" + tRepeatID + "'";
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    tRepeat = rs.getString("RName");
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


            tDateTime = tDate + " " + tTime;
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (taskID != 0) {
                taskDetailTextView.setText(tName);
                userDetailTextView.setText(tUser);
                dateTimeDetailTextView.setText(tDateTime);
                repeatDetailTextView.setText(tRepeat);
                pointsDetailTextView.setText(tPoints);
            } else {
                taskDetailTextView.setText("-");
                userDetailTextView.setText("-");
                dateTimeDetailTextView.setText("-");
                repeatDetailTextView.setText("-");
                pointsDetailTextView.setText("-");
            }

        }
    }
}