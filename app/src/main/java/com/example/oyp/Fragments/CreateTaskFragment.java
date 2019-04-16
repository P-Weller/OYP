package com.example.oyp.Fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.oyp.PushNotification.AlertReceiver;
import com.example.oyp.PushNotification.DatePickerFragment;
import com.example.oyp.R;
import com.example.oyp.RepeatSpinnerAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/***********************************************+
 * Was noch fehlt:
 * - SQL Statement zum eintragen des Tasks
 * - setContentView
 * - getBaseContext
 * - Intent
 */


public class CreateTaskFragment extends Fragment {

    public EditText taskEt, personEt, pointsEt;
    public static EditText dateEt;
    Spinner repeatSpinner;
    Button createBtn;
    Context thisContext;

    String dateText;
    String timeText;

    String[] repeatName={"Settings","Person"};
    int icons[] = {R.drawable.ic_settings_black_32dp, R.drawable.ic_person_add_black_24dp};

    //Creating public instance of Calendar to use in CreateTaskFragment, TimePickerFragment and DatePickerFragment
    public static Calendar c = Calendar.getInstance();

    //Creating new DateFormat and TimeFormat to have the right format for the database
   // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   // SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");


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


        taskEt = view.findViewById(R.id.createTaskEditText);
        dateEt = view.findViewById(R.id.dateEditText);
        //taskpointsSpinner = view.findViewById(R.id.taskpointsSpinner);
        createBtn = view.findViewById(R.id.createTaskBtn);
        repeatSpinner = view.findViewById(R.id.repeatSpinner);
       //
        //personSpinner = view.findViewById(R.id.personSpinner);

        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        RepeatSpinnerAdapter repeatSpinnerAdapter = new RepeatSpinnerAdapter(getActivity().getApplicationContext(), icons, repeatName);
        repeatSpinner.setAdapter(repeatSpinnerAdapter);


        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creates new instance of DatePicker
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");



                }

        });
                //Capture click on createTaskBtn
        createBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Createtask createtask = new Createtask();
                createtask.execute();

                startAlarm(getActivity());
            }
        });

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
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

    }

    public void updateText() {

    //checks if the dateEt EditText is null
    if (dateEt != null) {

        //Saving the chosen Date and Time in the dateText and timeText String
     //   dateText = dateFormat.format(c.getTime());
      //  timeText = timeFormat.format(c.getTime());
        //Setting the date on the dateEt EditText
        dateEt.setText(dateText + " " + timeText);
    }

}







    class Createtask extends AsyncTask<String, String, String> {

        String taskstr = taskEt.getText().toString();
        String datestr = dateEt.getText().toString();
        String timestr;
        String userstr;
        String statusstr;

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
                                "('" +datestr+ "','"+timestr+"','"+userstr+"','"+statusstr+"','"+taskstr+"')";

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

                /*Intent intent = new Intent(CreateTaskFragment.this, MainActivity.class);

                intent.putExtra("task",taskstr);
                intent.putExtra("person",personstr);
                intent.putExtra("date",datestr);
                intent.putExtra("repeat",repeatstr);
                intent.putExtra("points",pointsstr);
                intent.putExtra("time",timestr);
                intent.putExtra("status",statusstr);


                startActivity(intent);
                */
            }

        }

    }


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

}

