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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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

/*********************************************************************************************+
 * Was hier noch fehlt:
 * Prüfen, ob die beiden Passwörter gleich sind
 * Prüfen, ob die Checkbox angeklickt ist
 * sinnvolle Kommentare hinzufügen
 * nach drücken vom button erstellen auf die nächste seite verweisen
 *
 *
 *
 *
 */


public class RegistrationActivity extends AppCompatActivity{

    Button createhouseholdBtn;

    EditText householdEt, emailET, passwordET, confpasswordET;

    CheckBox termsCb;

    ConnectionClass connectionClass;

    Connection conn;
    String un,pass,db,ip;

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_HOUSEHOLDNAME = "key_householdname";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_HPASSWORD = "key_hpassword";




    public void onCreate(Bundle savedInstanceState) {

        connectionClass = new ConnectionClass();


        ip = "192.168.1.164";
        db = "oyp_database";
        un = "root";
        pass = "pass";

        super.onCreate(savedInstanceState);

        //Get the view from activity_registration.xml
        setContentView(R.layout.activity_registration);

        //Find cancelBtn by his ID
        createhouseholdBtn = (Button) findViewById(R.id.createAccountBtn);
        householdEt = (EditText) findViewById(R.id.HNameEditText);
        emailET = (EditText) findViewById(R.id.eMailEditText);
        passwordET = (EditText) findViewById(R.id.password2EditText);
        confpasswordET = (EditText) findViewById(R.id.password2EditText);
        termsCb = (CheckBox) findViewById(R.id.termsAndConditionsCheckBox);


        //Capture click on createBtn to go and add the user to the database and go to screen scoreboard
        createhouseholdBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                saveHousehold();
                Addhousehold addhousehold = new Addhousehold();
                addhousehold.execute();

            }

        });
    }

        private void saveHousehold(){
            String household = householdEt.getText().toString();
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();


            SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();

            editor.putString(KEY_HOUSEHOLDNAME, household);
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_HPASSWORD, password);

            editor.apply();
        }



    private class Addhousehold extends AsyncTask<String,String,String>  {

        String householdstr=householdEt.getText().toString();
        String emailstr = emailET.getText().toString();
        String passwordstr = passwordET.getText().toString();
        String confpasswordstr = confpasswordET.getText().toString();

        String z="";
        String householdid;

        boolean isSuccess=false;







        @Override
        protected String doInBackground(String... params) {

            if(householdstr.trim().equals("")|| emailstr.trim().equals("") || passwordstr.trim().equals("") || confpasswordstr.trim().equals("")
                    && !confpasswordstr.equals(passwordstr) )
                z = "Please enter all fields or check your password or accept TaC";



            else{


                try {
                    conn = connectionclass(un, pass, db, ip);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    }



                    else {

                        String query= "INSERT INTO household (HName, HEMail, HPassword) VALUES('"+householdstr+"' ,'"+emailstr+"', '"+passwordstr+"' )";

                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate(query);

                        z = "Inserting Successfull";

                        Intent intent = new Intent(RegistrationActivity.this, ChooseUserActivity.class);

                        intent.putExtra("household",householdstr);
                        intent.putExtra("email",emailstr);
                        intent.putExtra("password",passwordstr);


                        startActivity(intent);

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
            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show();


            if(isSuccess) {

                Intent intent = new Intent(RegistrationActivity.this, ChooseUserActivity.class);

                intent.putExtra("household",householdstr);
                intent.putExtra("email",emailstr);
                intent.putExtra("password",passwordstr);


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
