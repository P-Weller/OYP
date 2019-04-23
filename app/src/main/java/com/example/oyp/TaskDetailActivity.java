package com.example.oyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.oyp.Fragments.CreateTaskFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.oyp.DBStrings.DATABASE_IP;
import static com.example.oyp.DBStrings.DATABASE_NAME;
import static com.example.oyp.DBStrings.DATABASE_PASSWORD;
import static com.example.oyp.DBStrings.DATABASE_USER;


public class TaskDetailActivity extends AppCompatActivity {

    TextView taskDetailTextView;
    TextView userDetailTextView;
    TextView dateTimeDetailTextView;
    TextView repeatDetailTextView;
    TextView pointsDetailTextView;
    Button closeTaskButton;
    Button deleteTaskBtn;
    int taskID;
    String tPoints;
    int tStatus;
    int tNameID = 0;
    int tUserID = 0;
    public static int tRepeatID = 0;
    static String tDate = "";
    static String tTime = "";


    public static Calendar cRepeat = Calendar.getInstance();

    private static final String SHARED_PREF_NAME = "userdata";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Get the view from activity_taskdetails.xml
        setContentView(R.layout.activity_taskdetails);

        taskID = getTask();
        tUserID = getUser();

        taskDetailTextView = findViewById(R.id.taskDetailTextView);
        userDetailTextView = findViewById(R.id.userDetailTextView);
        dateTimeDetailTextView = findViewById(R.id.dateTimeDetailTextView);
        repeatDetailTextView = findViewById(R.id.repeatDetailTextView);
        pointsDetailTextView = findViewById(R.id.pointsDetailTextView);
        closeTaskButton = findViewById(R.id.closeTaskBtn);
        deleteTaskBtn = findViewById(R.id.deleteTaskBtn);

        System.out.println("tRepeatID: " + tRepeatID);
        closeTaskButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                SetClosed setClosed = new SetClosed();
                setClosed.execute("");
                System.out.println("tRepeatID: " + tRepeatID);
                if(tRepeatID != 1 && tRepeatID != 0) {



                    SetRepeat setRepeat = new SetRepeat();
                    setRepeat.execute("");
                }
                Intent intent = new Intent(TaskDetailActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        deleteTaskBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskDetailActivity.this);
                alertDialog.setMessage("Do you really want to delete this Task?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DeleteTask deleteTask = new DeleteTask();
                                deleteTask.execute("");
                                Intent intent = new Intent(TaskDetailActivity.this, MainActivity.class);
                                startActivity(intent);


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();




            }
        });

        GetData retrieveData = new GetData();
        retrieveData.execute("");

        deleteTask();
    }


    private int getTask() {

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        int taskID = sp.getInt("key_chosentask", 0);
        System.out.println("TaskID: " + taskID);
        return taskID;
    }

    private int getUser() {

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        int tUserID = sp.getInt("key_chosenuser", 0);
        System.out.println("UserID: " + tUserID);
        return tUserID;
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
        String tName = "";
        String tUser = "";
        String tDateTime = "";
        String tRepeat = "";


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
                String sql = "SELECT * FROM task WHERE TaskID = '" + taskID + "'";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    tNameID = rs.getInt("ActivityID");
                    tUserID = rs.getInt("UserID");
                    tDate = rs.getString("TDate");
                    tTime = rs.getString("TTime");
                    tRepeatID = rs.getInt("RepeatID");
                    tPoints = rs.getString("TPoints");
                    tStatus = rs.getInt("StatusID");
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


                if(tStatus == 1) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            closeTaskButton.setVisibility(View.GONE);
                            deleteTaskBtn.setVisibility(View.GONE);

                        }
                    });
                }



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

    private class SetClosed extends AsyncTask<String, String, String> {
        String msg = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt2 = null;

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                String query= "UPDATE task SET StatusID = 1 WHERE taskID = '" + taskID + "'";
                stmt2 = conn.createStatement();
                stmt2.executeUpdate(query);

                query= "UPDATE user SET UPoints = UPoints+'" + tPoints + "' WHERE UserID =  '" + tUserID + "'";
                stmt2 = conn.createStatement();
                stmt2.executeUpdate(query);

                query= "UPDATE task SET UserID = '" + tUserID + "' WHERE taskID = '" + taskID + "'";
                stmt2 = conn.createStatement();
                stmt2.executeUpdate(query);

                msg = "Update successful.";



            } catch (SQLException connError) {
                msg = "An exception was thrown by JDBC.";
                connError.printStackTrace();
            } finally {
                try {
                    if (stmt2 != null) {
                        stmt2.close();
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
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {


        }
    }


    public class SetRepeat extends AsyncTask<String, String, String> {
        String msg = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt2 = null;

            Calendar c = Calendar.getInstance();

            SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");





            try {
                c.setTime(dfDate.parse(tDate));
                cRepeat.setTime(dfDateTime.parse(tDate + " " + tTime));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(tRepeatID == 2){
                c.add(Calendar.DATE, 1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE

            } else if (tRepeatID == 3){
                c.add(Calendar.DATE, 7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            } else if (tRepeatID == 4){
                c.add(Calendar.MONTH, 1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            } else if (tRepeatID == 5){
                c.add(Calendar.YEAR, 1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            }
            //SimpleDateFormat dfDate = new SimpleDateFormat("MM-dd-yyyy");
            String newTDate = dfDate.format(c.getTime());

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                String query = "INSERT INTO task (TPoints, TDate, TTime, UserID, StatusID, RepeatID, ActivityID) VALUES" +
                        "('" + tPoints + "','" + newTDate + "','" + tTime + "','" + tUserID + "',0,'" + tRepeatID + "','" + tNameID + "')";

                System.out.println("tDate: "+tDate);
                System.out.println("newtDate: "+newTDate);
                System.out.println(dfDateTime.format(cRepeat.getTime()));



                Log.d("SQL",query);
                Statement stmt3 = conn.createStatement();
                stmt3.executeUpdate(query);

                msg = "Inserting successful";



            } catch (SQLException connError) {
                msg = "An exception was thrown by JDBC.";
                connError.printStackTrace();
            } finally {
                try {
                    if (stmt2 != null) {
                        stmt2.close();
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
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {

            CreateTaskFragment createTaskFragment = new CreateTaskFragment();

            createTaskFragment.startAlarmRepeat(TaskDetailActivity.this);

        }
    }

    private class DeleteTask extends AsyncTask<String, String, String> {
        String msg = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt3 = null;

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                String query= "DELETE FROM task WHERE taskID = '" + taskID + "'";
                stmt3 = conn.createStatement();
                stmt3.executeUpdate(query);

                msg = "Deletion successful.";



            } catch (SQLException connError) {
                msg = "An exception was thrown by JDBC.";
                connError.printStackTrace();
            } finally {
                try {
                    if (stmt3 != null) {
                        stmt3.close();
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

        }
    }


}