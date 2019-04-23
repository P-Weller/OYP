package com.example.oyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oyp.Fragments.CreateTaskFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.oyp.DBStrings.DATABASE_IP;
import static com.example.oyp.DBStrings.DATABASE_NAME;
import static com.example.oyp.DBStrings.DATABASE_PASSWORD;
import static com.example.oyp.DBStrings.DATABASE_USER;

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

    private static final String SHARED_PREF_NAME = "userdata";
    private static final String KEY_LOGINHNAME = "key_householdname";
    private static final String KEY_HEMAIL = "key_email";
    private static final String KEY_LOGINHPASSWORD = "key_loginhpassword";






    public void onCreate(Bundle savedInstanceState) {


        connectionClass = new ConnectionClass();

        super.onCreate(savedInstanceState);

        //Get the view from activity_registration.xml
        setContentView(R.layout.activity_registration);

        //Find cancelBtn by his ID
        createhouseholdBtn = (Button) findViewById(R.id.createAccountBtn);
        householdEt = (EditText) findViewById(R.id.HNameEditText);
        emailET = (EditText) findViewById(R.id.eMailEditText);
        passwordET = (EditText) findViewById(R.id.password2EditText);
        passwordET.setTransformationMethod(new PasswordTransformationMethod());
        confpasswordET = (EditText) findViewById(R.id.password3EditText);
        confpasswordET.setTransformationMethod(new PasswordTransformationMethod());
        termsCb = (CheckBox) findViewById(R.id.termsAndConditionsCheckBox);






        //Capture click on createBtn to go and add the user to the database and go to screen scoreboard
        createhouseholdBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                checkDataEntered();

                Addhousehold addhousehold = new Addhousehold();
                addhousehold.execute();

            }

        });

    }



        boolean isEmail(EditText text){
        CharSequence emailET = text.getText().toString();
        return (!TextUtils.isEmpty(emailET)) && Patterns.EMAIL_ADDRESS.matcher(emailET).matches();
        }

        void checkDataEntered(){
        if(isEmail(emailET) == false) {
            emailET.setError("Invalid Email");
        }
        }

        private void saveHousehold(){
            String household = householdEt.getText().toString();
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();


            SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();

            editor.putString(KEY_LOGINHNAME, household);
            editor.putString(KEY_HEMAIL, email);
            editor.putString(KEY_LOGINHPASSWORD, password);

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



            if (householdstr.trim().equals("") || emailstr.trim().equals("") || passwordstr.trim().equals("") || confpasswordstr.trim().equals("")
                    && !confpasswordstr.equals(passwordstr)) {
                z = "Please enter all fields or check your password or accept TaC";
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (householdstr.trim().equals("")) {
                            householdEt.setError("Required field");
                        }
                        if (emailstr.trim().equals("")) {
                            emailET.setError("Required field");
                        }
                        if (passwordstr.trim().equals("")) {
                            passwordET.setError("Required field");
                        }
                        if (confpasswordstr.trim().equals("")) {
                            confpasswordET.setError("Required field");
                        }
                    }
                });


            }else{


                try {
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    } else if(isEmail(emailET) == false) {
                        z = "Please enter a valid email address";
                        emailET.setError("Invalid Email");

                    } else if(!termsCb.isChecked()) {
                        z = "Please accept the terms and conditions";
                        termsCb.setError("Please accept!");

                    }else if(!confpasswordstr.equals(passwordstr)){
                        z= "Please enter two matching passwords";
                        passwordET.setError("Invalid Password");
                        confpasswordET.setError("Invalid Password");
                    }


                    else {


                            try {

                                String query = "INSERT INTO household (HName, HEMail, HPassword) VALUES('" + householdstr + "' ,'" + emailstr + "', '" + passwordstr + "' )";

                                Statement stmt = conn.createStatement();
                                stmt.executeUpdate(query);

                                z = "Inserting Successful";
                                saveHousehold();

                                Intent intent = new Intent(RegistrationActivity.this, CreateUserActivity.class);


                                startActivity(intent);

                            }catch (Exception SQLIntegrityConstraintViolationException)
                            {

                                z = "The household name is already taken. Please choose a different name for your household.";
                            }




                    }

                }

                catch (Exception ex)
                {
                    isSuccess = false;
                }

            }
            return z;
        }



        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),""+ z,Toast.LENGTH_LONG).show();


            if(isSuccess) {

                Intent intent = new Intent(RegistrationActivity.this, CreateTaskFragment.class);

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
    public void onBackPressed() {
        Log.e("######","You can't get out!");
    }
}


