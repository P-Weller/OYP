package com.example.oyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ChooseUserActivity extends AppCompatActivity{

    //List View for users
    Context thisContext;
    ListView usersListView;
    Button createuserBtn;
    ArrayList<String> uNames = new ArrayList<>();

    private static final String SHARED_PREF_NAME = "userdata";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisContext = this;
        //Get the view from activity_chooseuser.xml
        setContentView(R.layout.activity_chooseuser);

        Resources res = getResources();
        usersListView = (ListView) findViewById(R.id.usersListView);

        getHousehold();

        GetData retrieveData = new GetData();
        retrieveData.execute("");


        createuserBtn = (Button) findViewById(R.id.createuserBtn);

        createuserBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                    Intent intent = new Intent(ChooseUserActivity.this, CreateUserActivity.class);
                    startActivity(intent);


                }

        });

        // Creating a method to be able to click on the list rows
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                mainActivity.putExtra("com.example.oyp.Fragments.ITEM_INDEX", i);
                startActivity(mainActivity);

            }
        });


    }

    private String getHousehold(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String household = sp.getString("key_loginhname", "");
        return household;

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

                String sql = "SELECT UName FROM user WHERE HouseholdID = '" + householdid + "' ORDER BY UName ASC";

                stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    String name = rs.getString("UName");

                    uNames.add(i, name);
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
            ChooseUserAdapter usersAdapter = new ChooseUserAdapter(thisContext, uNames);
            usersListView.setAdapter(usersAdapter);
        }
    }

}


