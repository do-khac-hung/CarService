package com.khachungbg97gmail.carservice.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.khachungbg97gmail.carservice.Maintenance;
import com.khachungbg97gmail.carservice.R;


public class MyReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1;
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, Maintenance.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Maintenance.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        String Vin=intent.getStringExtra("Vin");
        String level=intent.getStringExtra("Level");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Notification notification = builder.setContentTitle("Đã đến lịch bảo dưỡng xe "+Vin)
                .setContentText("Cấp bảo dưỡng "+level)
                .setTicker("New Message Alert!")
                .setSmallIcon(R.drawable.ic_action_alarms)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }
}
