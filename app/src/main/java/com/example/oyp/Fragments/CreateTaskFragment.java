package com.example.oyp.Fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.oyp.ConnectionClass;
import com.example.oyp.CreateUserActivity;
import com.example.oyp.MainActivity;
import com.example.oyp.PersonSpinnerAdapter;
import com.example.oyp.PushNotification.AlertReceiver;
import com.example.oyp.PushNotification.DatePickerFragment;
import com.example.oyp.PushNotification.TimePickerFragment;
import com.example.oyp.R;
import com.example.oyp.RepeatSpinnerAdapter;
import com.example.oyp.StartActivity;
import com.example.oyp.TaskPointsSpinnerAdapter;
import com.example.oyp.UNamesAdapter;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.oyp.R.id.createTaskEditText;

/***********************************************+
 * Was noch fehlt:
 * - SQL Statement zum eintragen des Tasks
 * - setContentView
 * - getBaseContext
 * - Intent
 */


public class CreateTaskFragment extends Fragment {

    public EditText taskEt, dateEt;
    Spinner repeatSpinner, taskpointsSpinner, personSpinner;
    Button createBtn;
    Context thisContext;

    ArrayList<String> rNames = new ArrayList<>();
    ArrayList<String> pNames = new ArrayList<>();
    ArrayList<Integer> pIcon = new ArrayList<>();


    private static final String SHARED_PREF_NAME = "userdata";

    int iconsRepeat[] = {R.drawable.ic_access_time_black_32dp, R.drawable.ic_access_time_black_32dp, R.drawable.ic_access_time_black_32dp, R.drawable.ic_access_time_black_32dp, R.drawable.ic_access_time_black_32dp};


    String [] pointsName = {"Points", "10 Points", "20 Points" , "30 Points", "50 Points"};
    int iconsPoints []= {R.drawable.ic_attach_money_black_32dp, R.drawable.ic_attach_money_black_32dp, R.drawable.ic_attach_money_black_32dp, R.drawable.ic_attach_money_black_32dp, R.drawable.ic_attach_money_black_32dp};

    //Creating public instance of Calendar to use in CreateTaskFragment, TimePickerFragment and DatePickerFragment
    public Calendar c = Calendar.getInstance();

    public int updateTextID = 0;




    Connection conn;
    String un, pass, db, ip;





    public CreateTaskFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);





        View view = inflater.inflate(R.layout.fragment_createtask, container, false);
        thisContext = this.getContext();

        ip = "192.168.1.164";
        db = "oyp_database";
        un = "root";
        pass = "pass";


        rNames.add("repeat");
        pNames.add("household member");
        pIcon.add(R.drawable.ic_baseline_people_24px);


        taskEt = view.findViewById(R.id.createTaskEditText);
        dateEt = view.findViewById(R.id.dateEditText);
        taskpointsSpinner = view.findViewById(R.id.taskpointsSpinner);
        createBtn = view.findViewById(R.id.createTaskBtn);
        repeatSpinner = view.findViewById(R.id.repeatSpinner);
        personSpinner = view.findViewById(R.id.personSpinner);


        getHousehold();

        GetRepeatData getRepeatData = new GetRepeatData();
        getRepeatData.execute("");

        GetPersonData getPersonData = new GetPersonData();
        getPersonData.execute("");




        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        taskpointsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TaskPointsSpinnerAdapter taskPointsSpinnerAdapter = new TaskPointsSpinnerAdapter(getActivity().getApplicationContext(), iconsPoints, pointsName);
        taskpointsSpinner.setAdapter(taskPointsSpinnerAdapter);

        personSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");

                //updateText();

                }

        });
        /*
                //Capture click on createTaskBtn
        createBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Createtask createtask = new Createtask();
                createtask.execute();
            }
        });
*/
        return view;

    }


    public void startAlarm(Context context) {


        //creates Object of AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //passing the Alarm to AlertReceiver Class
        Intent intent = new Intent(context, AlertReceiver.class);
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

        //Compares the chosen time with the real time
        /*if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }*/

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void updateText() {
                String timeText = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime()) + "  " + DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
            dateEt.setText(timeText);
}





/*

    class Createtask extends AsyncTask<String, String, String> {

        String taskstr = taskEt.getText().toString();
        String datestr = dateEt.getText().toString();
        String timestr;
        String pointsString = taskpointsSpinner.getSelectedItem().toString();
        String personString = personSpinner.getSelectedItem().toString();


        String z = "";

        boolean isSuccess = false;

        public String captureactivity(String... params) {

            taskEt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                }
            });

            String taskEt ="";
            return taskEt;
        }




        @Override
        protected String doInBackground(String... params) {

            if (taskstr.trim().equals("") || datestr.trim().equals(""))
                z = "Please fill in all fields";


            else {


                try {
                    conn = connectionclass(un, pass, db, ip);
                    if (conn == null) {
                        z = "Please check your internet connection";
                    } else {

                        String query = "INSERT INTO task (TPoints, TDate, TTime, UserID, StatusID, RepeatID, ActivityID) VALUES" +
                                "('" + pointsString + "' ,'" +datestr+ "','"+timestr+"','"+personString+"',"0", '"+taskstr+"')";

                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate(query);

                        z = "Created task successfully";

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
            //Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show();


            if (isSuccess) {

                Intent intent = new Intent(CreateTaskFragment.this, MainActivity.class);

                intent.putExtra("task",taskstr);
                intent.putExtra("person",personstr);
                intent.putExtra("date",datestr);
                intent.putExtra("repeat",repeatstr);
                intent.putExtra("points",pointsstr);
                intent.putExtra("time",timestr);
                intent.putExtra("status",statusstr);


                startActivity(intent);

            }

        }

    } **/


    public Connection connectionclass(String user, String password, String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

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

    private class GetPersonData extends AsyncTask<String, String, String> {
        String msg= "";

        String householdid;
        String householdstr = getHousehold();



        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt30 = null;

            String ip = "192.168.1.164";
            String db = "oyp_database";
            String un = "root";
            String pass = "pass";
            String query30;
            String query40;


            try {
                conn = connectionclass(un, pass, db, ip);

                query40 = "SELECT HouseholdID FROM household WHERE HName = '" + householdstr + "'";

                Statement stmt1 = conn.createStatement();

                stmt1.executeUpdate(query40);

                ResultSet rs1 = stmt1.executeQuery(query40);

                while (rs1.next()) {
                    householdid = rs1.getString(1);

                }



                query30 = "SELECT UName, GIcon FROM user,gender WHERE HouseholdID = '" + householdid + "' AND user.GenderID = gender.GenderID ORDER BY UName ASC";


                stmt30 = conn.createStatement();
                stmt30.executeUpdate(query30);


                ResultSet rs30=stmt30.executeQuery(query30);

                while (rs30.next())


                {
                    String person = rs30.getString("UName");
                    String icon = rs30.getString("GIcon");

                    Resources res = getResources();
                    int gImage = res.getIdentifier(icon , "drawable", getActivity().getPackageName());

                    pNames.add(person);
                    pIcon.add(gImage);

                }


                msg = "Process complete.";
                rs30.close();
                stmt30.close();
                conn.close();

            } catch (SQLException connError) {
                msg = "An exception was thrown by JDBC.";
                connError.printStackTrace();
            } finally {
                try {
                    if (stmt30 != null) {
                        stmt30.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

          /*  ArrayList<String> count = new ArrayList<>();
            for (int k = 0; k < rNames.size(); k++) {
                String str = k + 1 + ".";
                count.add(str);


            }*/
            //String[] count2 = new String[count.size()];
            //count2 = count.toArray(count2);


            PersonSpinnerAdapter personSpinnerAdapter = new PersonSpinnerAdapter(getActivity().getApplicationContext(), pIcon, pNames);
            personSpinner.setAdapter(personSpinnerAdapter);

        }
    }






    private class GetRepeatData extends AsyncTask<String, String, String> {
        String msg = "";


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;

            String ip = "192.168.1.164";
            String db = "oyp_database";
            String un = "root";
            String pass = "pass";
            String query25;


            try {
                conn = connectionclass(un, pass, db, ip);


                query25 = "SELECT * FROM `repeat`";


                Statement stmt25 = conn.createStatement();
                stmt25.executeUpdate(query25);


                System.out.println("woSindwirDenn");
                ResultSet rs25=stmt25.executeQuery(query25);

                while (rs25.next())


                {
                    String repeat = rs25.getString("RName");
                    rNames.add(repeat);

                    System.out.println("SchonHier");
                }

                System.out.println("myTag");
                System.out.println(rNames);


                msg = "Process complete.";
                rs25.close();
                stmt25.close();
                conn.close();

            } catch (SQLException connError) {
                msg = "An exception was thrown by JDBC.";
                connError.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("es tut!");
            return null;
            }







        @Override
        protected void onPostExecute(String s) {

          /*  ArrayList<String> count = new ArrayList<>();
            for (int k = 0; k < rNames.size(); k++) {
                String str = k + 1 + ".";
                count.add(str);


            }*/
            //String[] count2 = new String[count.size()];
            //count2 = count.toArray(count2);


            RepeatSpinnerAdapter repeatSpinnerAdapter = new RepeatSpinnerAdapter(getActivity().getApplicationContext(), iconsRepeat, rNames);
            repeatSpinner.setAdapter(repeatSpinnerAdapter);

        }
    }

    public String getHousehold(){

        SharedPreferences sp = this.getActivity().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        String household = sp.getString("key_householdname", "");
        return household;

    }

}


