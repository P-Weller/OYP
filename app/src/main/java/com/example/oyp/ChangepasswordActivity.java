package com.example.oyp;

import android.content.Intent;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*****************************************+
 * - beim initialen Login das Password mitnehmen und mit dem unter "oldpassword" eingetragenen abgleichen
 * - prüfen, ob beide neuen Passwörter gleich sind
 */



public class ChangepasswordActivity extends AppCompatActivity {

    Button changeBtn;
    EditText oldpasswordEt;
    EditText newpasswordEt;
    EditText new2passwordEt;

    ConnectionClass connectionclass;

    Connection conn;
    String un, pass, db, ip;

    String oldpass;
    String newpass;

    public void onCreate(Bundle savedInstanceState){

        connectionclass = new ConnectionClass();

        ip = "192.168.1.164";
        db = "oyp_database";
        un = "root";
        pass = "pass";

        super.onCreate(savedInstanceState);

        //Get the view from activity_changepassword.xml
        setContentView(R.layout.activity_changepassword);

        changeBtn = (Button) findViewById(R.id.changepasswordBtn);
        oldpasswordEt = (EditText) findViewById(R.id.oldpasswordEditText);
        newpasswordEt = (EditText) findViewById(R.id.newpasswordEditText);
        new2passwordEt = (EditText) findViewById(R.id.confirmpasswordEditText);

        changeBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                Changepassword changepassword = new Changepassword();
                changepassword.execute();

            }
        });


    }

    private class Changepassword extends AsyncTask<String,String,String>{

        String oldpasswordstr = oldpasswordEt.getText().toString();
        String newpasswordstr = newpasswordEt.getText().toString();
        String confpasswordstr = new2passwordEt.getText().toString();

        String z="";

        boolean isSuccess=false;




        @Override
        protected String doInBackground(String... params) {


            if(oldpasswordstr.trim().equals("")|| newpasswordstr.trim().equals("") || confpasswordstr.trim().equals("")  )
                z = "Please enter all fields...";


            else
            {
                try {
                    conn = connectionclass(un, pass, db, ip);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    }
                    else {
                        String query1= "UPDATE household SET HPassword = '"+newpasswordstr+"' WHERE HPassword = '"+oldpasswordstr+"' ";

                        Statement stmt = conn.createStatement();

                        stmt.executeUpdate(query1);

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

                Intent intent=new Intent(ChangepasswordActivity.this, MainActivity.class);

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

