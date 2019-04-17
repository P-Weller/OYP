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
import android.widget.ListView;

import com.example.oyp.ActivityListViewAdapter;
import com.example.oyp.ActivityDetailActivity;
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

    Context thisContext;
    ArrayList<Integer> aImage = new ArrayList<>();
    ArrayList<String> aName = new ArrayList<>();
    ListView activityListView;

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_CHOSENACTIVITY = "key_chosenacitivity";

    public ActivityFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        thisContext = this.getContext();


        activityListView = view.findViewById(R.id.activityListView);

        GetData retrieveData = new GetData();
        retrieveData.execute("");


        // Creating a method to be able to click on the list rows
        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                String selectedFromList =(String) (parent.getItemAtPosition(i));

                GetID retrieveIDData = new GetID(selectedFromList);
                retrieveIDData.execute("");
            }
        });

        return view;

    }

    public void saveActivity(int i){

        int activityID = i;

        SharedPreferences sp = this.getActivity().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(KEY_CHOSENACTIVITY, activityID);

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
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                stmt = conn.createStatement();
                String sql = "SELECT * FROM activity ORDER BY AName ASC";

                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

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

            ActivityListViewAdapter activityListViewAdapter = new ActivityListViewAdapter(thisContext, aImage, aName);
            activityListView.setAdapter(activityListViewAdapter);
        }
    }

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

