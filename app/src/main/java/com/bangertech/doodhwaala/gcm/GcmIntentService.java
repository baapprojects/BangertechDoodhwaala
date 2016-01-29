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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.activity.Home;
import com.bangertech.doodhwaala.activity.LoginActivity;
import com.bangertech.doodhwaala.activity.SplashScreen;
import com.bangertech.doodhwaala.activity.TutorialScreens;
import com.bangertech.doodhwaala.application.DoodhwaalaApplication;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import java.util.List;
import java.util.Random;
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

    private final String Tag = "GCM DOODHWALA";

    @Override
    protected void onHandleIntent(Intent intent) {
        context = GcmIntentService.this;
        final Bundle extras = intent.getExtras();
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

                /*Intent intent2 = new Intent(this, CheckService.class);
                startService(intent2);*/

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

        /*contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Home.class), 0);*/
        Intent notification;
        if (PreferenceManager.getInstance().getUserId()!=null) {
            System.out.println(" USER_ID DOODHWALA_A " + PreferenceManager.getInstance().getUserId());
            updateMyActivity(this, "unique_name");
            PreferenceManager.getInstance().setFlag(true);
            notification = new Intent(this, Home.class);
            notification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            System.out.println(" USER_ID DOODHWALA_B "+PreferenceManager.getInstance().getUserId());
            PreferenceManager.getInstance().setFlag(false);
            notification = new Intent(this, LoginActivity.class);
            notification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        Random generator = new Random();

        contentIntent = PendingIntent.getActivity(this, generator.nextInt(),
                    notification, PendingIntent.FLAG_UPDATE_CURRENT);

        /*PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT).cancel();*/
        //contentIntent.cancel();

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

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notification_icon);
        int color = getResources().getColor(R.color.accentColor);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setColor(color)
                        //.setSmallIcon(R.drawable.ic_launcher)
                .setSmallIcon(R.drawable.notification_small_icon)
                .setTicker(msg.get("message").toString())
                .setLargeIcon(bm)
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

    public static void updateMyActivity(Context context, String message) {

        Intent intent = new Intent("unique_name");

        //put whatever data you want to send, if any
        intent.putExtra("message", message);

        //send broadcast
        context.sendBroadcast(intent);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notification_icon : R.drawable.notification_icon;
    }
}
