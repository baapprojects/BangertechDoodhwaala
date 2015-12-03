package com.bangertech.doodhwaala.gcm;

/**
 * Created by annutech on 12/1/2015.
 */
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.activity.Home;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import java.util.List;
//import com.mobello.couriernow.screens.HomeScreen;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {

    private final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;


    private Context context;

    /**
     * Constructor for GCMIntentService.
     */
    public GcmIntentService() {
        super("GcmIntentService");
    }

    private final String Tag = "GCM LEW";

    @Override
    protected void onHandleIntent(Intent intent) {
        context = GcmIntentService.this;
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                sendNotification(extras);
                Log.i(Tag, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    /**
     * Put the message into a notification and post it.
     * This is just one simple example of what you might choose to do with
     * a GCM message.
     * */
    private void sendNotification(Bundle msg) {

        PendingIntent contentIntent;

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Home.class), 0);

       /* if (login_adapter.toString().equals("[]"))
        {
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, UserFormActivity.class), 0);
        }
        else
        {
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, WelcomeActivity.class), 0);
        }*/



        /*NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(msg.get("title").toString())
                        .setTicker(msg.get("tickerText").toString())
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg.get("message").toString()))
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentText(msg.get("title").toString());
        System.out.println("push_sai "+msg.get("title").toString()+" & "+msg.get("message").toString());

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());*/

        int count = 1;
        int color = getResources().getColor(R.color.white);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setColor(color)
                        //.setSmallIcon(R.drawable.ic_launcher)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                        //.setNumber(Notification.C)
                .setContentIntent(contentIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg.get("message").toString()))
                .setPriority(5) //private static final PRIORITY_HIGH = 5;
                .setContentText(msg.get("message").toString())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
