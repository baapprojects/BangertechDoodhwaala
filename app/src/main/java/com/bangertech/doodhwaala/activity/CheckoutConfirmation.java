package com.bangertech.doodhwaala.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bangertech.doodhwaala.R;

/**
 * Created by annutech on 8/1/2016.
 */
public class CheckoutConfirmation extends AppCompatActivity{
    private ImageView ivConfirmationScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_confirmation);

        ivConfirmationScreen = (ImageView) findViewById(R.id.ivConfirmationScreen);

        ivConfirmationScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckoutConfirmation.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CheckoutConfirmation.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }
}
