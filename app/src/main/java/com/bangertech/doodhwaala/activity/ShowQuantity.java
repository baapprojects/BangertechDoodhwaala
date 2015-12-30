package com.bangertech.doodhwaala.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONObject;

/**
 * Created by annutech on 12/24/2015.
 */
public class ShowQuantity extends AppCompatActivity implements AsyncResponse {

    private RadioGroup radioGroupQuantity;
    private ImageView ivminus, ivplus;
    private int selectedQuantity=1;
    private TextView largeText;
    private TextView textViewQuantity;
    String previousValue="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);

        ivminus = (ImageView) findViewById(R.id.ivminus);
        ivplus = (ImageView) findViewById(R.id.ivplus);
        largeText = (TextView) findViewById(R.id.largeText);
        textViewQuantity = (TextView) findViewById(R.id.textViewQuantity);
        previousValue=getIntent().getStringExtra(ConstantVariables.SELECTED_USER_PLAN_KEY);
        ivminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedQuantity > 1) {
                    --selectedQuantity;
                }
                textViewQuantity.setText(String.valueOf(selectedQuantity));
                largeText.setText(String.valueOf(selectedQuantity));
            }
        });
        ivplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++selectedQuantity;
                textViewQuantity.setText(String.valueOf(selectedQuantity));
                largeText.setText(String.valueOf(selectedQuantity));
            }
        });
    }

    @Override
    public void backgroundProcessFinish(String from, String output) {

    }

    public void gotoContinueQuantity(View view)
    {
        try {

            JSONObject obj = new JSONObject(previousValue);
            obj.put("product_quantity",String.valueOf(selectedQuantity));
            startActivity(new Intent(ShowQuantity.this, ShowFrequency.class).putExtra(ConstantVariables.SELECTED_USER_PLAN_KEY, obj.toString()));
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            finish();
        }
        catch(Exception e)
        {

        }

    }
}
