package com.example.oyp.Fragments;

import android.content.Context;
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
import com.example.oyp.UnamesAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UsersFragment extends Fragment {

    Context thisContext;
    ArrayList<String> uNames = new ArrayList();
    ArrayList<String> uPoints = new ArrayList();
    ListView myListView;


    public UsersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        thisContext = this.getContext();

        myListView = view.findViewById(R.id.myListView);

        GetData retrieveData = new GetData();
        retrieveData.execute("");

        return view;

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


            try {
                conn = connectionclass(un, pass, db, ip);

                stmt = conn.createStatement();
                String sql = "SELECT * FROM user ORDER BY UPoints DESC";
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

        @Override
        protected void onPostExecute(String s) {

            ArrayList<String> count = new ArrayList<String>();
            for (int k = 0; k < uNames.size(); k++) {
                String str = k + 1 + ".";
                count.add(k,str);
            }

            UnamesAdapter unamesAdapter = new UnamesAdapter(thisContext, uNames, uPoints, count);
            myListView.setAdapter(unamesAdapter);
        }
    }
}
