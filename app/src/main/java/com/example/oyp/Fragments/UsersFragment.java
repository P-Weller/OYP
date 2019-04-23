package com.example.oyp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.oyp.R;
import com.example.oyp.UserListViewAdapter;

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

public class UsersFragment extends Fragment {

    //Declaration:
    Context thisContext;
    ArrayList<String> uNames = new ArrayList<>();
    ArrayList<String> uPoints = new ArrayList<>();
    ListView myListView;

    private static final String SHARED_PREF_NAME = "userdata";

    public UsersFragment() {
    }


    //Fragment onCreateView implementation:
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        thisContext = this.getContext();

        myListView = view.findViewById(R.id.myListView);

        getHousehold();

        // New Object of getData and its execution:
        GetData retrieveData = new GetData();
        retrieveData.execute("");

        return view;
    }


    // Connection to database:
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


    // Class to get the required Data for the fragment out of the database:
    private class GetData extends AsyncTask<String, String, String> {
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

                // Save the data out of the SQL-query in the declared ArrayLists:
                while (rs1.next()) {
                    householdid = rs1.getString(1);
                }

                stmt = conn.createStatement();
                String sql = "SELECT * FROM user WHERE HouseholdID = '" + householdid + "' ORDER BY UPoints DESC";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    String name = rs.getString("UName");
                    String points = rs.getString("UPoints");

                    uNames.add(i, name);
                    uPoints.add(i, points);
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


        // Add the ArrayLists to the ListView-Adapter:
        @Override
        protected void onPostExecute(String s) {

            ArrayList<String> count = new ArrayList<>();
            for (int k = 0; k < uNames.size(); k++) {
                String str = k + 1 + ".";
                count.add(k,str);
            }
            UserListViewAdapter unamesAdapter = new UserListViewAdapter(thisContext, uNames, uPoints, count);
            myListView.setAdapter(unamesAdapter);
        }
    }


    // Get household name from shared preferences
    public String getHousehold(){

        SharedPreferences sp = this.getActivity().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        String household = sp.getString("key_householdname", "");
        return household;

    }

}
