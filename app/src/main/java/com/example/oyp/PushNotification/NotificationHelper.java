package com.example.oyp.PushNotification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    int numMessages;

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

        channel.enableLights(true);
        channel.enableVibration(true);





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






        System.out.println("NotificationHelper");
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setAutoCancel(true)
                .setColor(Color.rgb(72,169,197))
                .setContentIntent(contentIntent)
                .setContentTitle("Reminder!")
                .setContentText("Hey! There is something to do in your household!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Hey! There is something to do in your household!")
                        .setBigContentTitle("Reminder!")
                        .setSummaryText("New Task!"))
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent);




        }


    }

