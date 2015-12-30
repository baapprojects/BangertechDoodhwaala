package com.bangertech.doodhwaala.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.application.DoodhwaalaApplication;
import com.bangertech.doodhwaala.gcm.GcmCreatingClass;
import com.bangertech.doodhwaala.manager.PreferenceManager;

/**
 * Created by annutech on 11/30/2015.
 */
public class SplashScreen extends Activity {

    //private GcmCreatingClass gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            //ActionBar actionBar = getActionBar();
            //getSupportActionBar().hide();

            // Hide the navigation bar.
            int uiOptions2 = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions2);
        }
        setContentView(R.layout.splash_screen);

        //gcm = new GcmCreatingClass(SplashScreen.this, SplashScreen.this);

        //gcm.registerGCM();

       // gcm.registerInBackground();

       // System.out.println("SplashScreen " + gcm.registerGCM() + " " + gcm.registerInBackground());

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                try {
                    if (PreferenceManager.getInstance().getUserId() != null) {
                        Intent mainIntent = new Intent(SplashScreen.this, Home.class);
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    } else {
                        DoodhwaalaApplication.isUserLoggedIn = false;
                        Intent mobileIntent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(mobileIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //close this activity
                finish();
            }
        }, 3000);

    }
}
