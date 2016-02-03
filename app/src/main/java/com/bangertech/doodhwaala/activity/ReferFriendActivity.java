package com.bangertech.doodhwaala.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by annutech on 2/2/2016.
 */
public class ReferFriendActivity extends AppCompatActivity implements AsyncResponse {
    private Toolbar toolbar;
    private TextView tvifrienddiscount, tviyourdiscount, delivery_details, tvirefer_friend, tvi_whatsapp, tvi_hike, tvi_sms, tvi_email;
    private RelativeLayout rl_whats_app, rl_messenger_app, rl_sms_app, rl_mail_app;
    private String inviteMessage = "Hey! I've been using Doodhwala for my morning milk subscription and it's awesome! Download it from Google Play on https://goo.gl/TPMYis and get 5% off with referral code: '" + PreferenceManager.getInstance().getShareReferralCode()+"'.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refer_friend);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Refer Friend");

        tvifrienddiscount = (TextView) findViewById(R.id.tvifrienddiscount);
        tviyourdiscount = (TextView) findViewById(R.id.tviyourdiscount);
        delivery_details = (TextView) findViewById(R.id.delivery_details);
        tvirefer_friend = (TextView) findViewById(R.id.tvirefer_friend);
        tvi_whatsapp = (TextView) findViewById(R.id.tvi_whatsapp);
        tvi_hike = (TextView) findViewById(R.id.tvi_hike);
        tvi_sms = (TextView) findViewById(R.id.tvi_sms);
        tvi_email = (TextView) findViewById(R.id.tvi_email);

        rl_whats_app = (RelativeLayout) findViewById(R.id.rl_whats_app);
        rl_messenger_app = (RelativeLayout) findViewById(R.id.rl_messenger_app);
        rl_sms_app = (RelativeLayout) findViewById(R.id.rl_sms_app);
        rl_mail_app = (RelativeLayout) findViewById(R.id.rl_mail_app);

        tvifrienddiscount.setTypeface(CUtils.RegularTypeFace(getApplicationContext()));
        tviyourdiscount.setTypeface(CUtils.RegularTypeFace(getApplicationContext()));
        delivery_details.setTypeface(CUtils.RegularTypeFace(getApplicationContext()));
        tvirefer_friend.setTypeface(CUtils.RegularTypeFace(getApplicationContext()));
        tvi_whatsapp.setTypeface(CUtils.RegularTypeFace(getApplicationContext()));
        tvi_hike.setTypeface(CUtils.RegularTypeFace(getApplicationContext()));
        tvi_sms.setTypeface(CUtils.RegularTypeFace(getApplicationContext()));
        tvi_email.setTypeface(CUtils.RegularTypeFace(getApplicationContext()));

        //com.whatsapp package name for hike messenger
        rl_whats_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, inviteMessage);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        //com.bsb.hike package name for hike messenger
        rl_messenger_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, inviteMessage);
                sendIntent.setPackage("com.bsb.hike");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        rl_sms_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("sms_body", inviteMessage);
                startActivity(sendIntent);

            }
        });
        rl_mail_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToGMail();
            }
        });

        deliveryLocalities();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        super.onBackPressed();
    }

    public void shareToGMail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation to Doodhwala");
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, inviteMessage);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for(final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);
    }

    public void deliveryLocalities() {
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("deliveryLocalities", ReferFriendActivity.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action"},
                new String[]{"user", "deliveryLocalities"});
        myAsyncTask.execute();
    }

    @Override
    public void backgroundProcessFinish(String from, String output) {
        if (from.equalsIgnoreCase("deliveryLocalities")) {
            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getBoolean("result")) {
                        delivery_details.setText("*Valid only in "+jsonObject.getString("delivery_localities"));
                    } else {
                        Toast.makeText(ReferFriendActivity.this, "Localities are not available", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                DialogManager.showDialog(ReferFriendActivity.this, "Server Error Occurred! Try Again!");
            }
        }
    }
}
