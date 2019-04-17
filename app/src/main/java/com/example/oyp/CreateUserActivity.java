package com.example.oyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.oyp.Fragments.UsersFragment;
import com.example.oyp.LoginActivity;

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

    ConnectionClass connectionClass;

    Connection conn;

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_USERNAME = "key_username";

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
        radio_g = (RadioGroup) findViewById(R.id.radiogroup);


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

                saveUsername();
                getHousehold();
                Log.d("Haushalt:", getHousehold());

                Adduser adduser = new Adduser();
                adduser.execute();

            }

        });

    }

    //method to save the created username in the sharepref file
    private void saveUsername(){
        String name = usernameEt.getText().toString();

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString(KEY_USERNAME, name);

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
    private class Adduser extends AsyncTask<String,String,String>  {

        String genderstr=radio_b.getText().toString();
        String usernamestr=usernameEt.getText().toString();
        String householdstr = getHousehold();

        String z="";
        String householdid;

        boolean isSuccess=false;

        @Override
        protected String doInBackground(String... params) {

            String query1 = null;

            if (usernamestr.trim().equals(""))
                z = "Please enter all fields...";
            else {
                try {
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    } else {

                        query1 = "SELECT HouseholdID FROM household WHERE HName = '" + householdstr + "'";


                        Statement stmt1 = conn.createStatement();

                        stmt1.executeUpdate(query1);

                        ResultSet rs1 = stmt1.executeQuery(query1);

                        while (rs1.next()) {
                            householdid = rs1.getString(1);

                        }

                        Log.d("Householdid", householdid);

                        String query2 = "INSERT INTO user (UName, GenderID, HouseholdID) VALUES" +
                                "('" + usernamestr + "','" + genderstr + "','" + householdid + "' )";

                        Log.d("SQL Eingabe",query2);
                        Statement stmt2 = conn.createStatement();
                        stmt2.executeUpdate(query2);


                        z = "Inserting Successfull";

                        Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
                        intent.putExtra("username",usernamestr);
                        intent.putExtra("gender",genderstr);
                        intent.putExtra("household",householdstr);

                        startActivity(intent);

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

}
