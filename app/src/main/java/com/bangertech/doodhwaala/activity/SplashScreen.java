package com.bangertech.doodhwaala.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.application.DoodhwaalaApplication;
import com.bangertech.doodhwaala.gcm.GcmCreatingClass;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.CUtils;

/**
 * Created by annutech on 11/30/2015.
 */
public class SplashScreen extends AppCompatActivity {

    private Toolbar mToolbar;

    private GcmCreatingClass gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mToolbar.setMinimumHeight(CUtils.getStatusBarHeight(SplashScreen.this));
        mToolbar.setVisibility(View.GONE);

        gcm = new GcmCreatingClass(SplashScreen.this, SplashScreen.this);

        gcm.registerGCM();

        gcm.registerInBackground();

        System.out.println("SplashScreen " + gcm.registerGCM() + " " + gcm.registerInBackground());

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                try {
                    if(PreferenceManager.getInstance().getUserId() != null){
                        Intent mainIntent = new Intent(SplashScreen.this, Home.class);
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    }
                    else
                    {
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
