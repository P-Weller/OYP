package com.example.oyp.PushNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.oyp.Fragments.CreateTaskFragment;

//AlertReceiver class

//receives the Alarm from the startAlarm method
public class AlertReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("AlertReceiver");


        //creates Object of NotificationHelper and sends notification
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        final int notificationID = (int) System.currentTimeMillis();
        notificationHelper.getManager().notify(notificationID, nb.build());
    }
}