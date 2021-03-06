package com.example.oyp.Fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oyp.ActivityDetailActivity;
import com.example.oyp.ActivitySpinnerAdapter;
import com.example.oyp.MainActivity;
import com.example.oyp.PersonSpinnerAdapter;
import com.example.oyp.PushNotification.AlertReceiver;
import com.example.oyp.PushNotification.DatePickerFragment;
import com.example.oyp.R;
import com.example.oyp.RepeatSpinnerAdapter;
import com.example.oyp.StartActivity;
import com.example.oyp.TaskDetailActivity;
import com.example.oyp.TaskPointsSpinnerAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.oyp.DBStrings.DATABASE_IP;
import static com.example.oyp.DBStrings.DATABASE_NAME;
import static com.example.oyp.DBStrings.DATABASE_PASSWORD;
import static com.example.oyp.DBStrings.DATABASE_USER;

/***********************************************+
 * Was noch fehlt:
 * - SQL Statement zum eintragen des Tasks
 * - setContentView
 * - getBaseContext
 * - Intent
 */


public class CreateTaskFragment extends Fragment {

    // Declaration:

    public static TextView dateEt; // TextView for Date and Time
    public static Spinner repeatSpinner, taskpointsSpinner, personSpinner, activitySpinner; // All spinners required in the CreateTaskFragment
    Button createBtn; // Button to create a Task
    Context thisContext;
    String dateText;
    String timeText;

    // Set time and date format:
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");


    // ArrayLists to save all data out of the database
    ArrayList<String> taskPoints = new ArrayList<>();
    ArrayList<String> rNames = new ArrayList<>();
    ArrayList<String> pNames = new ArrayList<>();
    ArrayList<Integer> pIcon = new ArrayList<>();
    ArrayList<Integer> aImage = new ArrayList<>();
    ArrayList<String> aName = new ArrayList<>();

    // Set items for spinner 'points'
    String points10 = "10 Points";
    String points20 = "20 Points";
    String points30 = "30 Points";
    String points50 = "50 Points";
    String points0 = "Points";

    private static final String KEY_CHOSENACTIVITY = "key_chosenacitivity";
    private static final String SHARED_PREF_NAME = "userdata"; // Shared preferences for Username


    // Arrays with icons for spinner ' Repeat and icons'
    int iconsRepeat[] = {R.drawable.ic_loop_black_32dp,R.drawable.ic_loop_black_32dp, R.drawable.ic_loop_black_32dp, R.drawable.ic_loop_black_32dp, R.drawable.ic_loop_black_32dp, R.drawable.ic_loop_black_32dp};
    int iconsPoints []= {R.drawable.ic_attach_money_black_32dp, R.drawable.ic_attach_money_black_32dp, R.drawable.ic_attach_money_black_32dp, R.drawable.ic_attach_money_black_32dp, R.drawable.ic_attach_money_black_32dp};

    //Creating public instance of Calendar to use in CreateTaskFragment, TimePickerFragment and DatePickerFragment
    public static Calendar c = Calendar.getInstance();

    Connection conn;

    public CreateTaskFragment() {
    }


    //Fragment onCreateView implementation:
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        // set reference to the fragment_activity xml-file:
        View view = inflater.inflate(R.layout.fragment_createtask, container, false);
        thisContext = this.getContext();

        // Set default item for all spinners and add data to spinners
        aName.add("Activity");
        aImage.add(R.drawable.ic_help_blue_32dp);
        rNames.add("Repeat");
        pNames.add("Household Member");
        pIcon.add(R.drawable.ic_person_add_black_24dp);
        taskPoints.add(points0);
        taskPoints.add(points10);
        taskPoints.add(points20);
        taskPoints.add(points30);
        taskPoints.add(points50);

        // Retrieve the Spinners, Buttons and EditText from fragment_createtask xml-file
        activitySpinner = view.findViewById(R.id.activitySpinner);
        dateEt = view.findViewById(R.id.dateEditText);
        taskpointsSpinner = view.findViewById(R.id.taskpointsSpinner);
        createBtn = view.findViewById(R.id.createTaskBtn);
        repeatSpinner = view.findViewById(R.id.repeatSpinner);
        personSpinner = view.findViewById(R.id.personSpinner);

        // Add Arrays to Adapter for TaskPointsSpinner
        TaskPointsSpinnerAdapter taskPointsSpinnerAdapter = new TaskPointsSpinnerAdapter(getActivity().getApplicationContext(), iconsPoints, taskPoints);
        taskpointsSpinner.setAdapter(taskPointsSpinnerAdapter);

        getHousehold();

        // New Objects of classes to get Repeat, Person and Activity data from the database
        GetRepeatData getRepeatData = new GetRepeatData();
        getRepeatData.execute("");

        GetPersonData getPersonData = new GetPersonData();
        getPersonData.execute("");

        GetActivityData getActivityData = new GetActivityData();
        getActivityData.execute("");


        // Set an Listener on all spinners to get the selected item out of them:
        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {}
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});

        taskpointsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {}
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});

        personSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {}
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {}
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});


        // Set onClickListener on DateEditText to set time and date
        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");
                }
        });

        //Capture click on createTaskBtn
        createBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            startAlarm(getActivity());
                Createtask createtask = new Createtask();
                createtask.execute();
            }
        });

        return view;
    }


    // Method to set the push-alarm
    public void startAlarm(Context context) {

        System.out.println("startAlarm");



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

    public void startAlarmRepeat(Context context) {


        SimpleDateFormat dfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("startAlarmRepeat");

        TaskDetailActivity t = new TaskDetailActivity();

        //creates Object of AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //passing the Alarm to AlertReceiver Class
        Intent intent = new Intent(context, AlertReceiver.class);
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);


        //Compares the chosen time with the real time
        if (t.cRepeat.before(Calendar.getInstance())) {
            t.cRepeat.add(Calendar.DATE, 1);

        }

        if (t.tRepeatID == 2) {

            System.out.println(dfDateTime.format(t.cRepeat.getTime()));

            t.cRepeat.add(Calendar.DATE, 1 );


            System.out.println(dfDateTime.format(t.cRepeat.getTime()));

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, t.cRepeat.getTimeInMillis() , pendingIntent);


        }else if (t.tRepeatID == 3){

            t.cRepeat.add(Calendar.DATE, 7);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, t.cRepeat.getTimeInMillis() , pendingIntent);

            System.out.println(dfDateTime.format(t.cRepeat.getTime()));

        } else if (t.tRepeatID == 4){

            System.out.println(dfDateTime.format(t.cRepeat.getTime()));

            t.cRepeat.add(Calendar.MONTH, 1);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, t.cRepeat.getTimeInMillis() , pendingIntent);

            System.out.println(dfDateTime.format(t.cRepeat.getTime()));

        } else if (t.tRepeatID == 5){
            t.cRepeat.add(Calendar.YEAR, 1);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, t.cRepeat.getTimeInMillis() , pendingIntent);

            System.out.println(dfDateTime.format(t.cRepeat.getTime()));

        }
    }

    // update text to selected time and date
    public void updateText() {

                dateText = dateFormat.format(c.getTime());
                timeText = timeFormat.format(c.getTime());
            dateEt.setText(dateText + "  "+ timeText);
}

    // Method to save the selected activity in shared preferences:
    public void saveActivity(int i){

        int activityID = i;

        SharedPreferences sp = this.getActivity().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(KEY_CHOSENACTIVITY, activityID);

        editor.apply();
    }


    class Createtask extends AsyncTask<String, String, String> {

        // Declaration of all Strings
        String activityString = activitySpinner.getSelectedItem().toString();
        String datestr = dateEt.getText().toString();
        String dateString = dateFormat.format(c.getTime());
        String timeString = timeFormat.format(c.getTime());
        String repeatString = repeatSpinner.getSelectedItem().toString();
        String personString = personSpinner.getSelectedItem().toString();
        String pointsString = taskpointsSpinner.getSelectedItem().toString();
        int statusID = 0;

        String z = "";
        String household = getHousehold();
        boolean isSuccess = false;


        // Insert the selected items of the spinners into the database
        @Override
        protected String doInBackground(String... params) {
            String householdID = "";
            Integer userID = 0;
            Integer repeatID = 0;
            Integer activityID = 0;

            System.out.println(dateString);
            System.out.println(timeString);

            if (activityString.equals("Activity") || repeatString.equals("Repeat") || personString.equals("Household Member") || pointsString.equals("Points") || datestr.equals("When?"))
                z = "Please fill in all fields";


            else {
                try {
                    conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                    System.out.println(personString);

                    if (conn == null) {
                        z = "Please check your internet connection";

                    } else {

                        if (pointsString.equals("20 Points")) {
                            pointsString = "20";
                        } else if (pointsString.equals("30 Points")) {
                            pointsString = "30";
                        } else if (pointsString.equals("50 Points")) {
                            pointsString = "50";
                        } else
                            pointsString = "10";

                        // Get householdID
                        String queryhouseholdID = "SELECT `householdid` FROM `household` WHERE `household`.`HName` = '" + household + "'";

                        Statement stmtHouseholdID = conn.createStatement();
                        stmtHouseholdID.executeUpdate(queryhouseholdID);
                        ResultSet rsHouseholdID = stmtHouseholdID.executeQuery(queryhouseholdID);

                        while (rsHouseholdID.next()) {
                            householdID = rsHouseholdID.getString(1);
                        }

                        // Get UserID
                        String queryUserID = "SELECT `UserID` FROM `user` WHERE `HouseholdID` = '" + householdID + "' AND `UName` = '" + personString + "'";
                        Statement stmtUserID = conn.createStatement();
                        stmtUserID.executeUpdate(queryUserID);
                        ResultSet rsUserID = stmtUserID.executeQuery(queryUserID);
                        while (rsUserID.next()) {
                            userID = Integer.valueOf(rsUserID.getString(1));
                        }

                        // Get RepeatID
                        String queryRepeatID = "SELECT `RepeatID` FROM `repeat` WHERE `RName` = '" + repeatString + "'";
                        Statement stmtRepeatID = conn.createStatement();
                        stmtRepeatID.executeUpdate(queryRepeatID);
                        ResultSet rsRepeatID = stmtUserID.executeQuery(queryRepeatID);
                        while (rsRepeatID.next()) {
                            repeatID = Integer.valueOf(rsRepeatID.getString(1));
                        }

                        // Get ActivityID
                        String queryActivityID = "SELECT `ActivityID` FROM `activity` WHERE `AName` = '" + activityString + "'";
                        Statement stmtActivityID = conn.createStatement();
                        stmtActivityID.executeUpdate(queryActivityID);
                        ResultSet rsActivityID = stmtUserID.executeQuery(queryActivityID);
                        while (rsActivityID.next()) {
                            activityID = Integer.valueOf(rsActivityID.getString(1));
                        }


                        // Insert all Strings and Integer into the database
                        Statement stmt = conn.createStatement();

                        String query = "INSERT INTO task (TPoints, TDate, TTime, UserID, StatusID, RepeatID, ActivityID) VALUES('" + pointsString + "' ,'" + dateString + "','" + timeString + "','" + userID + "','" + statusID + "' , '" + repeatID + "' , '" + activityID + "')";

                        stmt.executeUpdate(query);

                        isSuccess = true;
                        z = "Created task successfully";

                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions" + ex;
                }
            }
            return z;
        }

        // Jump to MainActivity
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getActivity().getBaseContext(), "" + z, Toast.LENGTH_LONG).show();

            if (isSuccess) {
                Intent intent = new Intent(getActivity(), MainActivity.class);

                startActivity(intent);
            }}
        }

    // Connection to the Database:
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


    // get the required Data for the spinner Person out of the database
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
            String query30;
            String query40;


            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

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


                // Save the data out of the SQL-query in the declared ArrayLists:
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


                // Catch exceptions and close the connection:
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


        //Add ArrayLists to the Adapter for Person spinner
        @Override
        protected void onPostExecute(String s) {
            PersonSpinnerAdapter personSpinnerAdapter = new PersonSpinnerAdapter(getActivity().getApplicationContext(), pIcon, pNames);
            personSpinner.setAdapter(personSpinnerAdapter);

        }
    }

    // get the required Data for the spinner Repeat out of the database
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

            String query25;


            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                query25 = "SELECT * FROM `repeat`";

                Statement stmt25 = conn.createStatement();
                stmt25.executeUpdate(query25);

                ResultSet rs25=stmt25.executeQuery(query25);

                // Save the data out of the SQL-query in the declared ArrayLists:
                while (rs25.next())
                {
                    String repeat = rs25.getString("RName");
                    rNames.add(repeat);
                }

                msg = "Process complete.";
                rs25.close();
                stmt25.close();
                conn.close();


                // Catch exceptions and close the connection:
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
            return null;
            }


        //Add ArrayLists to the Adapter for Repeat spinner
        @Override
        protected void onPostExecute(String s) {

            RepeatSpinnerAdapter repeatSpinnerAdapter = new RepeatSpinnerAdapter(getActivity().getApplicationContext(), iconsRepeat, rNames);
            repeatSpinner.setAdapter(repeatSpinnerAdapter);

        }
    }


    // get the required Data for the spinner Activity out of the database
    private class GetActivityData extends AsyncTask<String, String, String> {
        String msg = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);
                stmt = conn.createStatement();
                String sql = "SELECT * FROM activity ORDER BY AName ASC";
                ResultSet rs = stmt.executeQuery(sql);

                // Save the data out of the SQL-query in the declared ArrayLists:
                while (rs.next()) {
                    String aImageString = rs.getString("AIcon");
                    String aNameString = rs.getString("AName");

                    Resources res = getResources();
                    int aImageInt = res.getIdentifier(aImageString , "drawable", getActivity().getPackageName());

                    aImage.add(aImageInt);
                    aName.add(aNameString);

                }
                msg = "Process complete.";
                rs.close();
                stmt.close();
                conn.close();

                // Catch exceptions and close the connection:
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
            return null;
        }


        //Add ArrayLists to the Adapter for Activity spinner
        @Override
        protected void onPostExecute(String s) {
            ActivitySpinnerAdapter activitySpinnerAdapter = new ActivitySpinnerAdapter(getActivity().getApplicationContext(), aImage, aName);
            activitySpinner.setAdapter(activitySpinnerAdapter);;
        }
    }

    // Get the ActivityID because of the Activity-Name:
    private class GetID extends AsyncTask<String, String, String> {
        String msg = "";
        String aName;
        int activityID;
        private GetID(String activityName){
            aName = activityName;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        // Connection to database and execute query
        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;

            try {
                conn = connectionclass(DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_IP);

                stmt = conn.createStatement();
                String sql = "SELECT ActivityID FROM activity WHERE AName = '" + aName + "'";
                ResultSet rs = stmt.executeQuery(sql);
                int i = 0;

                while (rs.next()) {
                    activityID = rs.getInt("ActivityID");
                    i++;
                }
                msg = "Process complete.";
                rs.close();
                stmt.close();
                conn.close();


                // Catch exceptions and close the connection:
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
            saveActivity(activityID);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent showDetailActivity = new Intent(getActivity().getApplicationContext(), ActivityDetailActivity.class);
            showDetailActivity.putExtra("com.example.oyp.Fragments.ACTIVITY_INDEX", 0);
            startActivity(showDetailActivity);
        }
    }

    // Get household name from shared preferences
    public String getHousehold(){

        SharedPreferences sp = this.getActivity().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        String household = sp.getString("key_householdname", "");
        return household;
    }
}


