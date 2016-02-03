package com.bangertech.doodhwaala.gcm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.bangertech.doodhwaala.activity.Home;
import com.bangertech.doodhwaala.manager.PreferenceManager;

/**
 * Created by annutech on 10/1/2016.
 */
public class CheckService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        boolean run = PreferenceManager.getInstance().getFlag();
        if (run) {
// do what you want if the app is running
//run actualize in your case
        //new Home().fetchProductType();
        } else {
//do whatever you want again if it's not
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("app_gcm", "Service destroying");
    }
}