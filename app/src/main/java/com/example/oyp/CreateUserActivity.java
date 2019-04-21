package com.example.oyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.oyp.Fragments.CreateTaskFragment;
import com.example.oyp.Fragments.UsersFragment;
import com.example.oyp.LoginActivity;
import com.google.common.base.Strings;

import static com.example.oyp.DBStrings.DATABASE_IP;
import static com.example.oyp.DBStrings.DATABASE_NAME;
import static com.example.oyp.DBStrings.DATABASE_PASSWORD;
import static com.example.oyp.DBStrings.DATABASE_USER;


/**
 * Activity to create a new user in a household
 */

public class CreateUserActivity extends AppCompatActivity{

    Button cancelBtn;
    Button createBtn;
    EditText usernameEt, householdEt;
    RadioGroup radio_g;
    RadioButton radio_b;
    Spinner userColor;

    ArrayList<Integer> colorImage = new ArrayList<>();
    ArrayList<String> colorName = new ArrayList<>();

    ConnectionClass connectionClass;

    Connection conn;

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_CHOSENUSER = "key_chosenuser";

    public void onCreate(Bundle savedInstanceState) {

        connectionClass = new ConnectionClass();

        super.onCreate(savedInstanceState);

        //Get the view from activity_adduser.xml
        setContentView(R.layout.activity_adduser);

        //compare objects to the specific object in the XML
        cancelBtn = (Button) findViewById(R.id.cancelcreateUserBtn);
        createBtn = (Button) findViewById(R.id.createUserBtn);
        usernameEt = (EditText) findViewById(R.id.addUserEditText);
        //householdEt = (EditText) findViewById(R.id.household2EditText);
        radio_g = findViewById(R.id.radiogroup);
        userColor = findViewById(R.id.userColorSpinner);

        colorImage.add(R.drawable.ic_color_lens_black_32dp);
        colorName.add ("color");

        GetColorData getColorData = new GetColorData();
        getColorData.execute("");


        userColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Capture click on cancelBtn to go back to ChooseUserActivity
        cancelBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                Intent intent = new Intent(CreateUserActivity.this, ChooseUserActivity.class);
                startActivity(intent);

            }

        });

        //Capture click on createBtn to go and add the user to the database and go to screen UsersFragment
        createBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                //get selected radio button from radiogroup
                int selectedId = radio_g.getCheckedRadioButtonId();

                //find the radiobutton by returned id
                radio_b = (RadioButton) findViewById(selectedId);

                getHousehold();
                Log.d("Haushalt:", getHousehold());

                AddUser adduser = new AddUser();
                adduser.execute();

            }

        });

    }

    //method to save the created username in the sharepref file
    public void saveUser(int i){

        int username = i;

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(KEY_CHOSENUSER, username);

        editor.apply();
    }

    //method to get the current householdname out of the sharedpref file
    private String getHousehold(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String household = sp.getString("key_householdname", "");
        return household;

    }


    /**
     * class which  - connects to the database
     *              - select the ID of the current household
     *              - insert username, householdname and genderid into the database table user
     */
    private class AddUser extends AsyncTask<String,String,String>  {

        String genderstr=radio_b.getText().toString();
        String usernamestr=usernameEt.getText().toString();
        String householdstr = getHousehold();
        String uName;
        String uColorStr = userColor.getSelectedItem().toString();
        int uColorID = 0;

        String z="";
        String householdid;

        boolean isSuccess=false;

        @Override
        protected String doInBackground(String... params) {
            int userID = 0;
            String query1 = null;

            if (usernamestr.trim().equals("") || uColorStr.equals("color")) {
                z = "Please enter all fields...";
            }
            else {
                try {
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    } else {
                        Statement stmt = conn.createStatement();
                        String query4 = "SELECT UName FROM user,household WHERE HName = '" + householdstr + "' AND household.HouseholdID = user.HouseholdID AND UName = '" + usernamestr + "'";
                        ResultSet rs = stmt.executeQuery(query4);

                        while (rs.next()) {
                            uName = rs.getString("UName");

                        }

                        query4 = "SELECT ColorID FROM color WHERE CName = '" + uColorStr + "'";
                        rs = stmt.executeQuery(query4);

                        while (rs.next()) {
                            uColorID = rs.getInt("ColorID");

                        }

                        rs.close();
                        stmt.close();
                        if(!Strings.isNullOrEmpty(uName)){
                            z = "This username already exists. Please try another one.";
                        } else {


                            query1 = "SELECT HouseholdID FROM household WHERE HName = '" + householdstr + "'";

                            Statement stmt1 = conn.createStatement();

                            stmt1.executeUpdate(query1);

                            ResultSet rs1 = stmt1.executeQuery(query1);

                            while (rs1.next()) {
                                householdid = rs1.getString(1);

                            }

                            Log.d("Householdid", householdid);

                            String query2 = "INSERT INTO user (UName, GenderID, HouseholdID, ColorID) VALUES" +
                                    "('" + usernamestr + "','" + genderstr + "','" + householdid + "','" + uColorID + "' )";

                            Log.d("SQL Eingabe", query2);
                            Statement stmt2 = conn.createStatement();
                            stmt2.executeUpdate(query2);

                            z = "Inserting Successfull";

                            stmt1 = conn.createStatement();
                            String query3 = "SELECT UserID FROM user WHERE HouseholdID = '" + householdid + "' AND user.UName = '" + usernamestr + "'";
                            ResultSet rs2 = stmt1.executeQuery(query3);

                            while (rs2.next()) {
                                userID = rs2.getInt("UserID");
                            }

                            z = "Process complete.";
                            rs2.close();
                            stmt1.close();
                            conn.close();

                            saveUser(userID);
                           // System.out.println("HOUSEHOLD: " + householdid);
                           // System.out.println("USERNAME: " + usernamestr);
                           // System.out.println("SAVEDUSERID: " + userID);

                            Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
                            intent.putExtra("username", usernamestr);
                            intent.putExtra("gender", genderstr);
                            intent.putExtra("household", householdstr);

                            startActivity(intent);
                        }
                    }

                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions" + ex;
                }
            }
            return z;
        }


        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show();


            if(isSuccess) {

                Intent intent=new Intent(CreateUserActivity.this, MainActivity.class);

                intent.putExtra("username",usernamestr);

                startActivity(intent);
            }
        }
    }

    private class GetColorData extends AsyncTask<String, String, String> {
        String msg = "";
        String household = getHousehold();
        String householdID = "";


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

                String queryhouseholdID = "SELECT `householdid` FROM `household` WHERE `household`.`HName` = '" + household + "'";

                Statement stmtHouseholdID = conn.createStatement();
                stmtHouseholdID.executeUpdate(queryhouseholdID);
                ResultSet rsHouseholdID = stmtHouseholdID.executeQuery(queryhouseholdID);

                while (rsHouseholdID.next()) {
                    householdID = rsHouseholdID.getString(1);

                }
                System.out.println(householdID);

                stmt = conn.createStatement();

                System.out.println(household);


                String sql = "SELECT CName FROM color WHERE CNAME NOT IN (SELECT CName FROM color,user WHERE user.HouseholdID = '" + householdID + "'  AND user.ColorID = color.ColorID)";

                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String aImageString = rs.getString("CName");
                    String aNameString = rs.getString("CName");

                    Resources res = getResources();
                    int aImageInt = res.getIdentifier(aImageString , "drawable", getPackageName());

                    colorImage.add(aImageInt);
                    colorName.add(aNameString);


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

            UsercolorSpinnerAdapter usercolorSpinnerAdapter = new UsercolorSpinnerAdapter(getApplicationContext(), colorImage, colorName);
            userColor.setAdapter(usercolorSpinnerAdapter);


        }
    }




    public Connection connectionclass(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try
        {
            Class.forName("org.mariadb.jdbc.Driver");
            ConnectionURL = "jdbc:mariadb://"+server+"/"+database;
            connection = DriverManager.getConnection(ConnectionURL, user, password);
        }
        catch (SQLException se){
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e){
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e){
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }

    @Override
    public void onBackPressed() {
        Log.e("######","You can't get out!");
    }
}
