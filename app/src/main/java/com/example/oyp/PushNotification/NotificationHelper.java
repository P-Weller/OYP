package com.example.oyp.PushNotification;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.oyp.Fragments.CreateTaskFragment;
import com.example.oyp.MainActivity;
import com.example.oyp.R;

//NotificationHelper class

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {

        //sets Intent on the PushNotification
        Intent activityIntent = new Intent(this, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setAutoCancel(true)
                .setContentTitle("New Task!")
                .setContentText("You have a new task named:" + "Name of the Activity")
                .setColor(Color.rgb(72,169,197))
                .setSmallIcon(R.drawable.ic_event)
                .setContentIntent(contentIntent);
    }
}
