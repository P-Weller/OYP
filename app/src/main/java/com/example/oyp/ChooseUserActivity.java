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

import com.example.oyp.Fragments.TasksFragment;

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


public class ChooseUserActivity extends AppCompatActivity{

    //List View for users
    Context thisContext;
    ListView usersListView;
    Button createuserBtn;
    ArrayList<String> uNames = new ArrayList<>();
    ArrayList<Integer> uImage = new ArrayList<>();

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_CHOSENUSER = "key_chosenuser";


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

                String selectedFromList =(String) (parent.getItemAtPosition(i));
                GetUserID retrieveIDData = new GetUserID(selectedFromList);
                retrieveIDData.execute("");
            }
        });

    }

    public void saveUser(int i){

        int username = i;

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(KEY_CHOSENUSER, username);

        editor.apply();

    }

    private String getHousehold(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String household = sp.getString("key_householdname", "");
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

                String sql = "SELECT UName, GIcon FROM user,gender WHERE HouseholdID = '" + householdid + "' AND user.GenderID = gender.GenderID ORDER BY UName ASC";

                stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    String name = rs.getString("UName");
                    String gImageString = rs.getString("GIcon");

                    Resources res = getResources();
                    int gImage = res.getIdentifier(gImageString , "drawable", getPackageName());

                    uImage.add(i, gImage);
                    uNames.add(i, name);
                    i++;
                    Log.d("Uname", String.valueOf(i));
                }

                System.out.println(uImage);
                System.out.println(uNames);

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
            ChooseUserListViewAdapter usersAdapter = new ChooseUserListViewAdapter(thisContext,uImage, uNames);
            usersListView.setAdapter(usersAdapter);
        }
    }

    private class GetUserID extends AsyncTask<String, String, String> {
        String msg = "";
        String uName;
        int userID;

        private GetUserID(String userName){
            uName = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;
            String householdStr = getHousehold();

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                stmt = conn.createStatement();
                String sql = "SELECT UserID FROM user,household WHERE household.HName = '" + householdStr + "' AND user.UName = '" + uName + "'";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    userID = rs.getInt("UserID");
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

            saveUser(userID);
            System.out.println("HOUSEHOLD: " + householdStr);
            System.out.println("USERNAME: " + uName);
            System.out.println("SAVEDUSERID: " + userID);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            mainActivity.putExtra("com.example.oyp.Fragments.ITEM_INDEX", userID);
            startActivity(mainActivity);
        }
    }

}


