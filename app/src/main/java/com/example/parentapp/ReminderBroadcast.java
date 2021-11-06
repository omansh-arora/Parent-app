package com.example.parentapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.parentapp.UI.TimerScreen;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import com.example.parentapp.R;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context,TimerScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);
        i.putExtra("Type",1);

        Uri alarmSound = RingtoneManager. getDefaultUri (RingtoneManager. TYPE_NOTIFICATION );
        MediaPlayer mp = MediaPlayer. create (context.getApplicationContext(), alarmSound);
        mp.start();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"timer")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Parent App")
                .setContentText("Timer finished")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_launcher_foreground,"Stop",pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        builder.setSound(alarmSound);
        notificationManager.notify(200, builder.build());

    }
}
