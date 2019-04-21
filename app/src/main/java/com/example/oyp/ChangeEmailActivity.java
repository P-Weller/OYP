package com.example.oyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
 * Activity to change the email address from household
 */
public class ChangeEmailActivity extends AppCompatActivity {

    Button changeBtn;
    EditText newemailEt;
    EditText new2emailEt;

    ConnectionClass connectionclass;

    Connection conn;

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_HEMAIL = "key_email";


    public void onCreate(Bundle savedInstanceState){

        connectionclass = new ConnectionClass();

        super.onCreate(savedInstanceState);



        //Get the view from activity_changeemail.xml
        setContentView(R.layout.activity_changeemail);

        changeBtn = (Button) findViewById(R.id.changeemailBtn);
        newemailEt = (EditText) findViewById(R.id.newemailEdittext);
        new2emailEt = (EditText) findViewById(R.id.confirmemailEditText);


        changeBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                getHName();
                Changeemail changeemail = new Changeemail();
                changeemail.execute();
                updateSharedPref();

            }
        });


    }


    //method to get the householdname from local sharedpref file - the name will be used to verify the right household will be updated
    private String getHName(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String HNAME = sp.getString("key_householdname", "");
        return HNAME;
    }


    //method to write and update the email in the sharedpref file
    private void updateSharedPref(){
        String email = newemailEt.getText().toString();

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString(KEY_HEMAIL, email);

        editor.apply();
    }


    //class which connects to the database and update the email in the right household
    private class Changeemail extends AsyncTask<String,String,String>{

        String hnamestr = getHName();
        String newemailstr = newemailEt.getText().toString();
        String confemailstr = new2emailEt.getText().toString();

        String z="";

        boolean isSuccess=false;




        @Override
        protected String doInBackground(String... params) {


            if(newemailstr.trim().equals("") || confemailstr.trim().equals("")  )
                z = "Please enter all fields...";




            else
            {
                try {
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    }
                    else if(!newemailstr.equals(confemailstr)) {
                        z = "Please enter two matching e-mails.";
                    } else {
                        String query1= "UPDATE household SET HEMail = '"+newemailstr+"' WHERE HName = '"+hnamestr+"' ";

                        Statement stmt = conn.createStatement();

                        stmt.executeUpdate(query1);

                        isSuccess = true;

                        z = "email updated successfully";

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

                Intent intent=new Intent(ChangeEmailActivity.this, MainActivity.class);

                intent.putExtra("email",newemailstr);

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

