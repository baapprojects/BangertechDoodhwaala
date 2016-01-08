package com.bangertech.doodhwaala.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.utils.CUtils;

/**
 * Created by annutech on 9/23/2015.
 */
public class BrandAllProduct extends AppCompatActivity {
    private Toolbar app_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_allproduct);
        app_bar = (Toolbar) findViewById(R.id.app_bar);
        //app_bar.setPadding(0, CUtils.getStatusBarHeight(BrandAllProduct.this), 0, 0);
        setSupportActionBar(app_bar);
       
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
}
