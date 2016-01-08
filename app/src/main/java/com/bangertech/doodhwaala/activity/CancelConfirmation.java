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
public class CancelConfirmation extends AppCompatActivity {
    private ImageView ivCancellationScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_confirmation);

        ivCancellationScreen = (ImageView) findViewById(R.id.ivCancellationScreen);

        ivCancellationScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CancelConfirmation.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CancelConfirmation.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }
}
