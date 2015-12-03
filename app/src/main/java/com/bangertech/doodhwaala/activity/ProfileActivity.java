package com.bangertech.doodhwaala.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.bangertech.doodhwaala.fragment.MeFragment;
import com.bangertech.doodhwaala.fragment.MilkbarFragment;
import com.bangertech.doodhwaala.fragment.MyMilkFragment;
import com.bangertech.doodhwaala.R;

public class ProfileActivity extends AppCompatActivity {
    private FragmentTabHost mTabHost;
    public static View milkbarView, mymilkView, meView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        milkbarView = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.addtrans_tab_layout,null);
        mymilkView = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.addtrans_tab_layout, null);
        meView = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.addtrans_tab_layout, null);

        ((TextView) milkbarView.findViewById(R.id.tabtext)).setText("Milk Bar");
        ((TextView) mymilkView.findViewById(R.id.tabtext)).setText("My Milk");
        ((TextView) meView.findViewById(R.id.tabtext)).setText("Me");

        ((TextView) milkbarView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#F66298"));
        ((TextView) mymilkView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#989898"));
        ((TextView) meView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#989898"));

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(milkbarView),
                MilkbarFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(mymilkView),
                MyMilkFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator(meView),
                MeFragment.class, null);

        mTabHost.getTabWidget().setDividerDrawable(null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("tab1")) {
                    mTabHost.setCurrentTab(0);
                    ((TextView) milkbarView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#F66298"));
                    ((TextView) mymilkView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#989898"));
                    ((TextView) meView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#989898"));
                }
                if (tabId.equals("tab2")) {
                    mTabHost.setCurrentTab(1);
                    ((TextView) milkbarView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#989898"));
                    ((TextView) mymilkView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#F66298"));
                    ((TextView) meView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#989898"));
                }
                if (tabId.equals("tab3")) {
                    mTabHost.setCurrentTab(2);
                    ((TextView) milkbarView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#989898"));
                    ((TextView) mymilkView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#989898"));
                    ((TextView) meView.findViewById(R.id.tabtext)).setTextColor(Color.parseColor("#F66298"));
                }
            }
        });
    }


}
