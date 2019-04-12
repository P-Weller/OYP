package com.example.oyp.PushNotification;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.example.oyp.Fragments.CreateTaskFragment;
import com.example.oyp.MainActivity;

import java.util.Calendar;

// Creates Fragment of the TimePicker
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    //Creates instance of the DatePickerFragment
    DatePickerFragment datePickerFragment = new DatePickerFragment();
    //Creates instance of the CreateTaskFragment
    CreateTaskFragment createTaskFragment = new CreateTaskFragment();

    @NonNull
    @Override
    //creates a new TimePickerDialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //creates Instance of Calendar for the creation of TimePickerDialog
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }


    @Override
    //setting the time
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        //saving the time values in the created Calendar instance in the DatePickerFragment
        datePickerFragment.c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datePickerFragment.c.set(Calendar.MINUTE, minute);
        datePickerFragment.c.set(Calendar.SECOND, 0);
        //jumps into the updateDateTimeText() method in CreateTaskFragment.java

        createTaskFragment.updateText();
        //jumps into the startAlarm() method in CreateTaskFragment.java


        createTaskFragment.startAlarm(datePickerFragment.c);


    }
}
