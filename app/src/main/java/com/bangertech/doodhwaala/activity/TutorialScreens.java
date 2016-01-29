package com.bangertech.doodhwaala.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.adapter.PagerAdapter;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

/**
 * Created by annutech on 31/12/2015.
 */
public class TutorialScreens extends AppCompatActivity implements ViewPager.OnPageChangeListener, AsyncResponse {

    private Button mSkipBtn;
    private PagerAdapter mAdapter;
    private CirclePageIndicator mIndicator;
    private String appVersionName;
    private MyAsynTaskManager myAsyncTask;
    private Activity mActivity;
    private int mPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mActivity = TutorialScreens.this;
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        appVersionName = pInfo.versionName;

        // Toast.makeText(getApplicationContext(),"sai "+version,Toast.LENGTH_LONG).show();

        mSkipBtn = (Button) findViewById(R.id.btn_skip);

        final int[] arrayOfDrawables = new int[]{R.drawable.discover,
                R.drawable.search, R.drawable.choose, R.drawable.change};

        mAdapter = new PagerAdapter(this, arrayOfDrawables);
        final ViewPager mPager = (ViewPager) findViewById(R.id.help_pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setOnPageChangeListener(this);

        mSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSkipBtn.getText().toString().equals("Next")) {
                    mIndicator.setCurrentItem(mPosition+1);
                } else {
                    Intent navigation = new Intent(TutorialScreens.this, LoginActivity.class);
                    navigation.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(navigation);
                    finish();
                    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                }
            }
        });

        checkAppVersion();
    }

    public void checkAppVersion() {
        myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("checkAppVersion", mActivity, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "appVersion"},
                new String[]{"user", "checkAppVersion", appVersionName});
        myAsyncTask.execute();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //mPosition = position;
    }

    @Override
    public void onPageSelected(final int position) {
        mPosition = position;
        if (position == 3) {
            ((Button) findViewById(R.id.btn_skip)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.btn_skip)).setText("Done");
        } else {
            ((Button) findViewById(R.id.btn_skip)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.btn_skip)).setText("Next");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //mPosition = state;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        super.onBackPressed();
    }

    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("checkAppVersion")){
            Log.i("AppVersionResult", output);
            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getBoolean("result")) {
                        Log.i("AppVersionResult", "No need to update");
                    } else {
                        if(jsonObject.getInt("forceUpgrade") == 1) {
                            DialogManager.showDialogForceUpdate(mActivity, "Please upgrade to the latest version of Doodhwala to continue");
                        }
                        if(jsonObject.getInt("recommendUpgrade") == 1) {
                            DialogManager.showDialogRecommendedUpdate(mActivity,"A new version of Doodhwala is available please press OK to update?");
                        }

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                DialogManager.showDialog(mActivity, "Server Error Occurred! Try Again!");
            }
        }
    }
}
