package com.khachungbg97gmail.carservice.Notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationManagerCompat;

import com.khachungbg97gmail.carservice.Maintenance;
import com.khachungbg97gmail.carservice.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificationService extends IntentService {
    private static final int NOTIFICATION_ID = 1;
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

        }
        Notification.Builder builder=new Notification.Builder(this);
        builder.setContentTitle("Đặt lịch bảo dưỡng");
        builder.setContentText("");
        builder.setSmallIcon(R.drawable.ic_menu_slideshow);
        builder.setAutoCancel(true);
        builder.setLights(Color.GREEN, 500, 500);
        Intent notifyIntent = new Intent(this, Maintenance.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);

    }


}
