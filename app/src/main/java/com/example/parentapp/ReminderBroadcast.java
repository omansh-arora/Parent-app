package com.example.parentapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.parentapp.UI.TimerScreen;

import android.net.Uri;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context,TimerScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);
        i.putExtra("Type",1);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"timer")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Parent App")
                .setContentText("Timer finished")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_baseline_timer_24,"Stop",pendingIntent)
                .setStyle(new NotificationCompat.InboxStyle())
                .setSound(Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.ringtone));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(250, builder.build());

    }
}
