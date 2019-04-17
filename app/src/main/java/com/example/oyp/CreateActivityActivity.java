package com.example.oyp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.oyp.DBStrings.DATABASE_IP;
import static com.example.oyp.DBStrings.DATABASE_NAME;
import static com.example.oyp.DBStrings.DATABASE_PASSWORD;
import static com.example.oyp.DBStrings.DATABASE_USER;

public class CreateActivityActivity extends AppCompatActivity {

    EditText activityNameEditText;
    EditText activityDescriptionEditText;
    Button createActivityBtn;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Get the view from activity CreateActivityActivity
        setContentView(R.layout.activity_createactivity);

        activityNameEditText = findViewById(R.id.activityNameEditText);
        activityDescriptionEditText = findViewById(R.id.activityDescriptionEditText);
        createActivityBtn = findViewById(R.id.createActivityBtn);


    }

    private class AddActivity extends AsyncTask<String,String,String> {

        String aNameStr=activityNameEditText.getText().toString();
        String aDescriptionStr=activityDescriptionEditText.getText().toString();

        String z="";

        boolean isSuccess=false;

        @Override
        protected String doInBackground(String... params) {

            String query1 = null;

            if (aNameStr.trim().equals(""))
                z = "Please enter all fields...";
            else {
                try {
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    } else {

                        String query2 = "INSERT INTO activity (AName, ADescription, AIcon) VALUES" +
                                "('" + aNameStr + "','" + aDescriptionStr + "',icon_other)";

                        Log.d("SQL Eingabe",query2);
                        Statement stmt2 = conn.createStatement();
                        stmt2.executeUpdate(query2);

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
                        //intent.putExtra("upoints",20);
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
}
