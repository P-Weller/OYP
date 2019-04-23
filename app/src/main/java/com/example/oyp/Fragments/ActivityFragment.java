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

import com.example.oyp.ActivityListViewAdapter;
import com.example.oyp.ActivityDetailActivity;
import com.example.oyp.CreateActivityActivity;
import com.example.oyp.R;

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

public class ActivityFragment extends Fragment {

    // Declaration:

    Button createActivityBtn; // Button to create an Activity

    Context thisContext;
    ArrayList<Integer> aImage = new ArrayList<>(); // ArrayList with the Integer value of the Activity icons
    ArrayList<String> aName = new ArrayList<>(); // ArrayList with all Activity names
    ListView activityListView; // ListView to show all Activities with their icon

    private static final String SHARED_PREF_NAME = "userdata"; // Shared preferences for Username
    private static final String KEY_CHOSENACTIVITY = "key_chosenacitivity"; // Shared preferences for the selected Activity

    public ActivityFragment(){
    }


    //Fragment onCreateView implementation:
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // set reference to the fragment_activity xml-file:
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        thisContext = this.getContext(); // Refer to ActivityFragment Context


        activityListView = view.findViewById(R.id.activityListView); // Retrieve the ListView out of fragment_activity xml-file

        GetData retrieveData = new GetData(); // New Object of GetData
        retrieveData.execute(""); // Execute the new object


        // Creating a method to be able to click on the list rows
        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                String selectedFromList =(String) (parent.getItemAtPosition(i));

                GetID retrieveIDData = new GetID(selectedFromList);
                retrieveIDData.execute("");
            }
        });

        createActivityBtn = view.findViewById(R.id.newActivityBtn); // Retrieve the Button out of fragment_activity xml-file

        // Creating a method to be able to add a new activity
        createActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateActivityActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }

    // Method to save the selected activity in shared preferences:
    public void saveActivity(int i){

        int activityID = i;

        SharedPreferences sp = this.getActivity().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(KEY_CHOSENACTIVITY, activityID);

        editor.apply();
    }

    // Connection to the Database:
    public Connection connectionclass(String user, String password, String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;

        try {
            Class.forName("org.mariadb.jdbc.Driver"); // get JDBC-Driver
            ConnectionURL = "jdbc:mariadb://" + server + "/" + database; // URL of the Database
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


    // Class to get the required Data for the fragment out of the database:
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

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP); // Get connection to database

                stmt = conn.createStatement();
                String sql = "SELECT * FROM activity ORDER BY AName ASC"; // Required SQL-query

                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                // Save the data out of the SQL-query in the declared ArrayLists:
                while (rs.next()) {
                    String aImageString = rs.getString("AIcon");
                    String aNameString = rs.getString("AName");

                    Resources res = getResources();
                    int aImageInt = res.getIdentifier(aImageString , "drawable", getActivity().getPackageName());

                    aImage.add(i, aImageInt);
                    aName.add(i, aNameString);
                    i++;
                }


                msg = "Process complete.";
                rs.close();
                stmt.close();
                conn.close();

            // Catch exceptions and close the connection:
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

        // Add the ArrayLists to the ListView-Adapter:
        @Override
        protected void onPostExecute(String s) {
            ActivityListViewAdapter activityListViewAdapter = new ActivityListViewAdapter(thisContext, aImage, aName);
            activityListView.setAdapter(activityListViewAdapter);
        }
    }



    // Get the ActivityID because of the Activity-Name:
    private class GetID extends AsyncTask<String, String, String> {
        String msg = "";
        String aName;
        int activityID;

        private GetID(String activityName){
            aName = activityName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Connection to database and execute query
        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                stmt = conn.createStatement();
                String sql = "SELECT ActivityID FROM activity WHERE AName = '" + aName + "'";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    activityID = rs.getInt("ActivityID");
                    i++;
                }

                msg = "Process complete.";
                rs.close();
                stmt.close();
                conn.close();


                // Catch exceptions and close the connection:
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
            saveActivity(activityID);
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            Intent showDetailActivity = new Intent(getActivity().getApplicationContext(), ActivityDetailActivity.class);
            showDetailActivity.putExtra("com.example.oyp.Fragments.ACTIVITY_INDEX", 0);
            startActivity(showDetailActivity);
        }
    }
}

