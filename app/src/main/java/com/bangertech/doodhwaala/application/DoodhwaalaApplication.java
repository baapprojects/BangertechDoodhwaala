package com.bangertech.doodhwaala.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.bangertech.doodhwaala.activity.Home;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppConstants;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Sarvesh on 9/11/2015.
 */
public class DoodhwaalaApplication extends Application {
    private static final String TAG = "DoodhwaalaApplication";
    public static Context mContext;
    public static boolean isUserLoggedIn = false;
    private static SharedPreferences mSharedPreferences;
    public static int backgroundService = 20;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initSharedPreferences();
        initSingletons();
        initSimpleFacebook();
    }

    private void initSimpleFacebook() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.bash", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void initSingletons() {

        //DataBaseManager.initInstance(mContext);
        //GCMHelper.checkDeviceRegistration(mContext);

    }


    private void initSharedPreferences(){
        PreferenceManager.initInstance();
        mSharedPreferences = getSharedPreferences("DOODH_PREF", MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public static String getPrivateFilePath() {
        return mContext.getExternalFilesDir(AppConstants.FILE_DIR).getAbsolutePath();
    }

    private static DoodhwaalaApplication mInstance;

    public static synchronized DoodhwaalaApplication getInstance() {
        return mInstance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String message = intent.getStringExtra("message");

            //do other stuff here
            new Home().fetchProductType();
        }
    };
}
