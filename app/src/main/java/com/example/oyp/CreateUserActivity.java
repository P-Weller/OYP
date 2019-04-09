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

import com.example.oyp.Fragments.UsersFragment;
import com.example.oyp.LoginActivity;


/****************************************************
 * Was fehlt:
 * Household muss immer angegeben werden, kann man die Variable vom Login übernehmen ?
 * Wie wollen wir die Color mitgeben ? Draufklicken und eine Farbauswahl bekommen ?
 *
 */

public class CreateUserActivity extends AppCompatActivity{

    Button cancelBtn;
    Button createBtn;
    EditText usernameEt, householdEt;
    RadioGroup radio_g;
    RadioButton radio_b;


    ConnectionClass connectionClass;

    Connection conn;
    String un,pass,db,ip;

    public void onCreate(Bundle savedInstanceState) {

        connectionClass = new ConnectionClass();


        ip = "192.168.1.164";
        db = "oyp_database";
        un = "root";
        pass = "pass";

        super.onCreate(savedInstanceState);

        //Get the view from activity_adduser.xml
        setContentView(R.layout.activity_adduser);

        //Find cancelBtn by his ID
        cancelBtn = (Button) findViewById(R.id.cancelcreateUserBtn);
        createBtn = (Button) findViewById(R.id.createUserBtn);
        usernameEt = (EditText) findViewById(R.id.addUserEditText);
        householdEt = (EditText) findViewById(R.id.household2EditText);
        radio_g = (RadioGroup) findViewById(R.id.radiogroup);


        //Capture click on cancelBtn to go back to ChooseUserActivity
        cancelBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(CreateUserActivity.this, StartActivity.class);
                startActivity(intent);


            }

        });

        //Capture click on createBtn to go and add the user to the database and go to screen scoreboard
        createBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                //get selected radio button from radiogroup
                int selectedId = radio_g.getCheckedRadioButtonId();

                //find the radiobutton by returned id
                radio_b = (RadioButton) findViewById(selectedId);



                Adduser adduser = new Adduser();
                adduser.execute();

            }

        });

    }

    private class Adduser extends AsyncTask<String,String,String>  {

        String genderstr=radio_b.getText().toString();
        String usernamestr=usernameEt.getText().toString();
        String householdstr=householdEt.getText().toString();
        String z="";
        String householdid;

        boolean isSuccess=false;



        @Override
        protected String doInBackground(String... params) {

            if(usernamestr.trim().equals("")|| householdstr.trim().equals("")  )
                z = "Please enter all fields...";
            else
            {
                try {
                    conn = connectionclass(un, pass, db, ip);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    }
                    else {
                        String query1= "SELECT HouseholdID FROM household WHERE HName = '"+householdstr+"'";

                        Statement stmt = conn.createStatement();

                        stmt.executeUpdate(query1);

                        ResultSet rs=stmt.executeQuery(query1);

                        while (rs.next()) {
                            householdid = rs.getString(1);

                        }




                        String query= "INSERT INTO user (UName, GenderID, HouseholdID) VALUES('"+usernamestr+"' ,'"+genderstr+"', '"+householdid+"' )";


                        Statement stmt2 = conn.createStatement();
                        stmt2.executeUpdate(query);


                        z = "Inserting Successfull";

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
