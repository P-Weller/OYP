package com.example.oyp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.oyp.MainActivity;
import com.example.oyp.R;
import com.example.oyp.TaskDetailActivity;
import com.example.oyp.TaskListViewAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.oyp.DBStrings.DATABASE_IP;
import static com.example.oyp.DBStrings.DATABASE_NAME;
import static com.example.oyp.DBStrings.DATABASE_PASSWORD;
import static com.example.oyp.DBStrings.DATABASE_USER;

public class TasksFragment extends Fragment {

    Context thisContext;
    ArrayList<Integer> tImage = new ArrayList<>();
    ArrayList<String> tName = new ArrayList<>();
    ArrayList<String> tUser = new ArrayList<>();
    ArrayList<Integer> tColorID = new ArrayList<>();
    ListView otasksListView;
    int i = 0;

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_CHOSENTASK = "key_chosentask";

    public TasksFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        thisContext = this.getContext();


        otasksListView = view.findViewById(R.id.otasksListView);
        Button closedTaskBtn = view.findViewById(R.id.closedTaskBtn);
        TextView opentasksTextView = view.findViewById(R.id.opentasksTextView);
        Button clearTasksBtn = view.findViewById(R.id.clearTasksBtn);

        getHousehold();


        clearTasksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Do you really want to clear all Tasks?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClearTasks clearTasks = new ClearTasks();
                                clearTasks.execute("");
                                GetClosedTaskData retrieveClosedTaskData = new GetClosedTaskData();
                                retrieveClosedTaskData.execute("");
                                closedTaskBtn.setText("Show me open tasks");
                                opentasksTextView.setText("Closed Tasks");
                                clearTasksBtn.setVisibility(View.VISIBLE);
                                i = 1;
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


        closedTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tImage = new ArrayList<>();
                tName = new ArrayList<>();
                tUser = new ArrayList<>();
                tColorID = new ArrayList<>();


                if(i == 0) {
                    GetClosedTaskData retrieveClosedTaskData = new GetClosedTaskData();
                    retrieveClosedTaskData.execute("");
                    closedTaskBtn.setText("Show me open Tasks");
                    opentasksTextView.setText("Closed Tasks");
                    clearTasksBtn.setVisibility(View.VISIBLE);
                    i = 1;
                } else if(i == 1){
                    GetOpenTaskData retrieveOpenTaskData = new GetOpenTaskData();
                    retrieveOpenTaskData.execute("");
                    closedTaskBtn.setText("Show me closed Tasks");
                    opentasksTextView.setText("Open Tasks");
                    clearTasksBtn.setVisibility(View.GONE);
                    i = 0;
                }
            }
        });


        if(i == 0) {
            GetOpenTaskData retrieveOpenTaskData = new GetOpenTaskData();
            retrieveOpenTaskData.execute("");
            clearTasksBtn.setVisibility(View.GONE);
        }


        // Creating a method to be able to click on the list rows
        otasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                String selectedFromList =(String) (parent.getItemAtPosition(i));

                GetID retrieveIDData = new GetID(selectedFromList, tUser.get(i));
                retrieveIDData.execute("");
            }
        });



        return view;
    }

    public String getHousehold(){

        SharedPreferences sp = this.getActivity().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        String household = sp.getString("key_householdname", "");
        return household;

    }

    public void saveTask(int i){

        int activityID = i;

        SharedPreferences sp = this.getActivity().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(KEY_CHOSENTASK, activityID);

        editor.apply();

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

            String householdid ="";
            String query1;

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                query1 = "SELECT HouseholdID FROM household WHERE HName = '" + householdstr + "'";

                Statement stmt1 = conn.createStatement();

                stmt1.executeUpdate(query1);

                ResultSet rs1 = stmt1.executeQuery(query1);

                while (rs1.next()) {
                    householdid = rs1.getString(1);

                }

                Log.d("HouseholdID", householdid);

                stmt = conn.createStatement();
                String sql = "SELECT AIcon,AName,UName,ColorID,TDate FROM activity,task,user WHERE task.ActivityID = activity.ActivityID AND StatusID = 0 AND task.UserID = user.UserID AND user.HouseholdID = '" + householdid + "' ORDER BY TDate ASC";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    String aImageString = rs.getString("AIcon");
                    String aName = rs.getString("AName");
                    String aUser = rs.getString("UName") + " - " + rs.getString("TDate");
                    int aColorID = rs.getInt("ColorID");

                    Resources res = getResources();
                    int aImage = res.getIdentifier(aImageString , "drawable", getActivity().getPackageName());

                    tImage.add(i, aImage);
                    tName.add(i, aName);
                    tUser.add(i, aUser);
                    tColorID.add(i, aColorID);
                    i++;

                }


               // System.out.println(tImage);
               // System.out.println(tName);
                //System.out.println(tUser);

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

            TaskListViewAdapter taskListViewAdapter = new TaskListViewAdapter(thisContext, tImage, tName, tUser, tColorID);
            otasksListView.setAdapter(taskListViewAdapter);
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

            String householdid ="";
            String query1;

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                query1 = "SELECT HouseholdID FROM household WHERE HName = '" + householdstr + "'";

                Statement stmt1 = conn.createStatement();

                stmt1.executeUpdate(query1);

                ResultSet rs1 = stmt1.executeQuery(query1);

                while (rs1.next()) {
                    householdid = rs1.getString(1);

                }

                Log.d("HouseholdID", householdid);

                stmt = conn.createStatement();
                String sql = "SELECT AIcon,AName,UName,ColorID FROM activity,task,user WHERE task.ActivityID = activity.ActivityID AND StatusID = 1 AND task.UserID = user.UserID AND user.HouseholdID = '" + householdid + "'";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    String aImageString = rs.getString("AIcon");
                    String aName = rs.getString("AName");
                    String aUser = rs.getString("UName");
                    int aColorID = rs.getInt("ColorID");

                    Resources res = getResources();
                    int aImage = res.getIdentifier(aImageString , "drawable", getActivity().getPackageName());

                    tImage.add(i, aImage);
                    tName.add(i, aName);
                    tUser.add(i, aUser);
                    tColorID.add(i,aColorID);
                    i++;

                }


                System.out.println(tImage);
                System.out.println(tName);
                System.out.println(tUser);
                System.out.println(tColorID);
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

            TaskListViewAdapter taskListViewAdapter = new TaskListViewAdapter(thisContext, tImage, tName,tUser, tColorID);
            otasksListView.setAdapter(taskListViewAdapter);
        }
    }

    private class GetID extends AsyncTask<String, String, String> {
        String msg = "";
        String tName;
        String tUser;
        int taskID;

        private GetID(String taskName, String userName){
            tName = taskName;
            tUser = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;
            System.out.println("ICH BIN WICHTIG" + tUser +", "+ tName);

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                stmt = conn.createStatement();
                String sql = "SELECT TaskID FROM activity,task,user WHERE task.ActivityID = activity.ActivityID AND activity.AName = '" + tName + "' AND task.UserID = user.UserID AND UName = '" + tUser + "'";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    taskID = rs.getInt("TaskID");
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

            saveTask(taskID);
            System.out.println("1: " + taskID);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent showDetailActivity = new Intent(getActivity().getApplicationContext(), TaskDetailActivity.class);
            showDetailActivity.putExtra("com.example.oyp.Fragments.ACTIVITY_INDEX", taskID);
            startActivity(showDetailActivity);
        }
    }

    private class ClearTasks extends AsyncTask<String, String, String> {
        String msg = "";
        String householdStr = getHousehold();

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
                String sql = "DELETE task FROM `task`,user,household WHERE task.StatusID = 1 AND task.UserID=user.UserID AND user.HouseholdID = household.HouseholdID AND HName = '"+ householdStr +"'";
                ResultSet rs = stmt.executeQuery(sql);

                msg = "Clear complete.";
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

            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }
}

