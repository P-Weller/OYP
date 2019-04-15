package com.example.oyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.example.oyp.Fragments.MoreFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




public class ChangeemailActivity extends AppCompatActivity {

    Button changeBtn;
    EditText newemailEt;
    EditText new2emailEt;

    ConnectionClass connectionclass;

    Connection conn;
    String un, pass, db, ip;

    String oldemail;
    String newemail;

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_HEMAIL = "key_email";


    public void onCreate(Bundle savedInstanceState){

        connectionclass = new ConnectionClass();

        ip = "192.168.1.164";
        db = "oyp_database";
        un = "root";
        pass = "pass";

        super.onCreate(savedInstanceState);

        //Get the view from activity_changepassword.xml
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

    private String getHName(){

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String HNAME = sp.getString("key_householdname", "");
        return HNAME;
    }



    private void updateSharedPref(){
        String email = newemailEt.getText().toString();

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString(KEY_HEMAIL, email);

        editor.apply();
    }

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
                    conn = connectionclass(un, pass, db, ip);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    }
                    else {
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

                Intent intent=new Intent(ChangeemailActivity.this, MainActivity.class);

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

