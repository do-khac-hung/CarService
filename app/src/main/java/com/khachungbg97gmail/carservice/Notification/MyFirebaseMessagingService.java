package com.khachungbg97gmail.carservice.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.khachungbg97gmail.carservice.Common.Common;
import com.khachungbg97gmail.carservice.Home;
import com.khachungbg97gmail.carservice.Model.Notification;
import com.khachungbg97gmail.carservice.R;

import java.util.Collections;
import java.util.Map;

/**
 * Created by ASUS on 10/16/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService  {
    private static final String TAG = "MyFirebaseMsgService";
    private static int count = 0;
    Notification notification;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "Notification Message TITLE: " + remoteMessage.getNotification().getTitle());
        Log.d(TAG, "Notification Message BODY: " + remoteMessage.getNotification().getBody());
        //set title and body
            notification=new Notification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody()
            );
        Common.notificationList.add(notification);
        Toast.makeText(this, notification.getBody(), Toast.LENGTH_SHORT).show();
        Collections.reverse(Common.notificationList);
        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(), remoteMessage.getData());


    }
    private void sendNotification(String messageTitle, String messageBody, Map<String,String>row){
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_share))
                .setSmallIcon(R.drawable.ic_menu_share)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(count, notificationBuilder.build());
        count++;
      }

    }

