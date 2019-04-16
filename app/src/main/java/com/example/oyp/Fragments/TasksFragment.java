package com.example.oyp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.oyp.R;
import com.example.oyp.TaskDetailActivity;
import com.example.oyp.TasksAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TasksFragment extends Fragment {

    Context thisContext;
    ArrayList<Integer> tImage = new ArrayList<>();
    ArrayList<String> tName = new ArrayList<>();
    ArrayList<String> tUser = new ArrayList<>();
    ListView otasksListView;
    int i = 0;

    private static final String SHARED_PREF_NAME = "userdata";

    public TasksFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        thisContext = this.getContext();


        otasksListView = view.findViewById(R.id.otasksListView);
        Button closedTaskBtn = view.findViewById(R.id.closedTaskBtn);
        TextView opentasksTextView = view.findViewById(R.id.opentasksTextView);

        getHousehold();

        closedTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tImage = new ArrayList<>();
                tName = new ArrayList<>();
                tUser = new ArrayList<>();


                if(i == 0) {
                    GetClosedTaskData retrieveClosedTaskData = new GetClosedTaskData();
                    retrieveClosedTaskData.execute("");
                    closedTaskBtn.setText("Show me open tasks");
                    opentasksTextView.setText("Closed Tasks");
                    i = 1;
                } else if(i == 1){
                    GetOpenTaskData retrieveOpenTaskData = new GetOpenTaskData();
                    retrieveOpenTaskData.execute("");
                    closedTaskBtn.setText("Show me closed tasks");
                    opentasksTextView.setText("Open Tasks");
                    i = 0;
                }
            }
        });
        if(i == 0) {
            GetOpenTaskData retrieveOpenTaskData = new GetOpenTaskData();
            retrieveOpenTaskData.execute("");
        }

        // Creating a method to be able to click on the list rows
        otasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Intent showDetailActivity = new Intent(getActivity().getApplicationContext(), TaskDetailActivity.class);
                showDetailActivity.putExtra("com.example.oyp.Fragments.ITEM_INDEX", i);
                startActivity(showDetailActivity);

            }
        });

        return view;
    }

    public String getHousehold(){

        SharedPreferences sp = this.getActivity().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        String household = sp.getString("key_householdname", "");
        return household;

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


    private class GetOpenTaskData extends AsyncTask<String, String, String> {
        String msg = "";
        String householdstr = getHousehold();


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
            String householdid ="";
            String query1;

            try {
                conn = connectionclass(un, pass, db, ip);

                query1 = "SELECT HouseholdID FROM household WHERE HName = '" + householdstr + "'";

                Statement stmt1 = conn.createStatement();

                stmt1.executeUpdate(query1);

                ResultSet rs1 = stmt1.executeQuery(query1);

                while (rs1.next()) {
                    householdid = rs1.getString(1);

                }

                Log.d("HouseholdID", householdid);

                stmt = conn.createStatement();
                String sql = "SELECT AIcon,AName,UName FROM activity,task,user WHERE task.ActivityID = activity.ActivityID AND StatusID = 0 AND task.UserID = user.UserID AND user.HouseholdID = '" + householdid + "'";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    String aImageString = rs.getString("AIcon");
                    String aName = rs.getString("AName");
                    String aUser = rs.getString("UName");

                    Resources res = getResources();
                    int aImage = res.getIdentifier(aImageString , "drawable", getActivity().getPackageName());

                    tImage.add(i, aImage);
                    tName.add(i, aName);
                    tUser.add(i, aUser);
                    i++;

                }


                System.out.println(tImage);
                System.out.println(tName);
                System.out.println(tUser);

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

            TasksAdapter tasksAdapter = new TasksAdapter(thisContext, tImage, tName, tUser);
            otasksListView.setAdapter(tasksAdapter);
        }
    }

    private class GetClosedTaskData extends AsyncTask<String, String, String> {
        String msg = "";
        String householdstr = getHousehold();


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
            String householdid ="";
            String query1;

            try {
                conn = connectionclass(un, pass, db, ip);

                query1 = "SELECT HouseholdID FROM household WHERE HName = '" + householdstr + "'";

                Statement stmt1 = conn.createStatement();

                stmt1.executeUpdate(query1);

                ResultSet rs1 = stmt1.executeQuery(query1);

                while (rs1.next()) {
                    householdid = rs1.getString(1);

                }

                Log.d("HouseholdID", householdid);

                stmt = conn.createStatement();
                String sql = "SELECT AIcon,AName,UName FROM activity,task,user WHERE task.ActivityID = activity.ActivityID AND StatusID = 1 AND task.UserID = user.UserID AND user.HouseholdID = '" + householdid + "'";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    String aImageString = rs.getString("AIcon");
                    String aName = rs.getString("AName");
                    String aUser = rs.getString("UName");

                    Resources res = getResources();
                    int aImage = res.getIdentifier(aImageString , "drawable", getActivity().getPackageName());

                    tImage.add(i, aImage);
                    tName.add(i, aName);
                    tUser.add(i, aUser);
                    i++;

                }


                System.out.println(tImage);
                System.out.println(tName);
                System.out.println(tUser);
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

            TasksAdapter tasksAdapter = new TasksAdapter(thisContext, tImage, tName,tUser);
            otasksListView.setAdapter(tasksAdapter);
        }
    }
}

