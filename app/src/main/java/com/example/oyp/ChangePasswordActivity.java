package com.example.oyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.oyp.DBStrings.DATABASE_IP;
import static com.example.oyp.DBStrings.DATABASE_NAME;
import static com.example.oyp.DBStrings.DATABASE_PASSWORD;
import static com.example.oyp.DBStrings.DATABASE_USER;

/**
 * Activity to change the password from the household
 */
public class ChangePasswordActivity extends AppCompatActivity {

    Button changeBtn;
    EditText newpasswordEt;
    EditText new2passwordEt;

    ConnectionClass connectionclass;

    Connection conn;

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_LOGINHPASSWORD = "key_loginhpassword";


    public void onCreate(Bundle savedInstanceState){

        connectionclass = new ConnectionClass();

        super.onCreate(savedInstanceState);

        //Get the view from activity_changepassword.xml
        setContentView(R.layout.activity_changepassword);

        changeBtn = (Button) findViewById(R.id.changepasswordBtn);
        newpasswordEt = (EditText) findViewById(R.id.newpasswordEditText);
        new2passwordEt = (EditText) findViewById(R.id.confirmpasswordEditText);


        changeBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                getHousehold();
                Changepassword changepassword = new Changepassword();
                changepassword.execute();
                updateSharedPref();

            }
        });


    }
    //method to get the current householdname in the sharedpref file
    private String getHousehold(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String household = sp.getString("key_householdname", "");
        return household;
    }

    //method to write to new password into the sharedpref file
    private void updateSharedPref(){
        String password = newpasswordEt.getText().toString();

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString(KEY_LOGINHPASSWORD, password);

        editor.apply();
    }

    //class which connects to the database and update the password from the right household
    private class Changepassword extends AsyncTask<String,String,String>{

        String hnamestr = getHousehold();
        String newpasswordstr = newpasswordEt.getText().toString();
        String confpasswordstr = new2passwordEt.getText().toString();

        String z="";

        boolean isSuccess=false;




        @Override
        protected String doInBackground(String... params) {


            if(newpasswordstr.trim().equals("") || confpasswordstr.trim().equals("")  )
                z = "Please enter all fields...";




            else
            {
                try {
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    }
                    else if(!confpasswordstr.equals(newpasswordstr)) {
                z = "Please enter two matching passwords.";


            } else {
                        String query1= "UPDATE household SET HPassword = '"+newpasswordstr+"' WHERE HName = '"+hnamestr+"' ";

                        Statement stmt = conn.createStatement();

                        stmt.executeUpdate(query1);

                        isSuccess = true;

                        z = "Password updated successfully";

                    }

                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions"+ex;
                }
            }

            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),""+z, Toast.LENGTH_LONG).show();


            if(isSuccess) {

                Intent intent=new Intent(ChangePasswordActivity.this, MainActivity.class);

                intent.putExtra("password",newpasswordstr);

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

