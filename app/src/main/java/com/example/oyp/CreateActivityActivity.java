package com.example.oyp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        createActivityBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                AddActivity addActivity = new AddActivity();
                addActivity.execute();
            }
        });
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

    private class AddActivity extends AsyncTask<String,String,String> {

        String aNameStr=activityNameEditText.getText().toString();
        String aDescriptionStr=activityDescriptionEditText.getText().toString();

        String z="";

        boolean isSuccess=false;

        @Override
        protected String doInBackground(String... params) {
            Connection conn = null;
            Statement stmt = null;


            String query1 = null;

            if (aNameStr.trim().equals(""))
                z = "Please enter all fields...";
            else {
                try {
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    } else {

                        String query = "INSERT INTO activity (AName, ADescription, AIcon) VALUES" +
                                "('" + aNameStr + "','" + aDescriptionStr + "', 'icon_other')";

                        Log.d("SQL",query);
                        Statement stmt2 = conn.createStatement();
                        stmt2.executeUpdate(query);

                        z = "Inserting successful";

                        Intent intent = new Intent(CreateActivityActivity.this, MainActivity.class);
                        intent.putExtra("activityName",aNameStr);
                        intent.putExtra("activityDescription",aDescriptionStr);
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

                Intent intent = new Intent(CreateActivityActivity.this, MainActivity.class);
                intent.putExtra("activityName",aNameStr);
                intent.putExtra("activityDescription",aDescriptionStr);
                startActivity(intent);
            }

        }
    }
}
