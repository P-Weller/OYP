package com.example.oyp.PushNotification;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;



import com.example.oyp.Fragments.CreateTaskFragment;
import com.example.oyp.MainActivity;

import java.text.DateFormat;
import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    //Creates instance of Calendar

    CreateTaskFragment createTaskFragment = new CreateTaskFragment();


    @NonNull
    @Override
    //Creates a new DatePickerDialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    //Creates a new instance of Calendar for the creation of the DatePickerDialog
        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
    }

    @Override
    //setting the date
    public void onDateSet(DatePicker view, int chosenYear, int chosenMonth, int chosenDay) {

    //saving the chosen date values in the created Calendar instance
        createTaskFragment.c.set(Calendar.YEAR, chosenYear);
        createTaskFragment.c.set(Calendar.MONTH, chosenMonth);
        createTaskFragment.c.set(Calendar.DAY_OF_MONTH, chosenDay);

        Log.d("onDateSet" , "OnDateSet: " + createTaskFragment.c.toString());

        //creates a new timePickerFragment --> Jumps into onTimeSet() in TimePickerFragment.java
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getFragmentManager(), "time picker");
    }
}





