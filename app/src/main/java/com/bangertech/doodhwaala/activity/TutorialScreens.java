package com.bangertech.doodhwaala.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.adapter.PagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by annutech on 31/12/2015.
 */
public class TutorialScreens extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private Button mSkipBtn;
    private PagerAdapter mAdapter;
    private CirclePageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        mSkipBtn = (Button) findViewById(R.id.btn_skip);

        int[] arrayOfDrawables = new int[]{R.drawable.discover,
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
                Intent navigation = new Intent(TutorialScreens.this, LoginActivity.class);
                navigation.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(navigation);
                finish();
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 3) {
            ((Button) findViewById(R.id.btn_skip)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.btn_skip)).setText("Done");
        } else {
            ((Button) findViewById(R.id.btn_skip)).setVisibility(View.INVISIBLE);
            ((Button) findViewById(R.id.btn_skip)).setText("Skip");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
