package com.bangertech.doodhwaala.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONException;
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
    private String productId, productMappingId;
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
        productId=getIntent().getStringExtra(ConstantVariables.PRODUCT_ID_KEY);
        productMappingId=getIntent().getStringExtra(ConstantVariables.PRODUCT_MAPPING_ID_KEY);
        if((!TextUtils.isEmpty(previousValue))) {

            try {
                JSONObject obj = new JSONObject(previousValue);
               selectedQuantity=obj.getInt("product_quantity");
                textViewQuantity.setText(String.valueOf(selectedQuantity));
                largeText.setText(String.valueOf(selectedQuantity));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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
                if (selectedQuantity < 50) {
                    ++selectedQuantity;
                }
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
            startActivity(new Intent(ShowQuantity.this, ShowFrequency.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(ConstantVariables.SELECTED_USER_PLAN_KEY, obj.toString()));
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            finish();
        }
        catch(Exception e)
        {

        }
    }

    @Override
    public void onBackPressed() {
        try {

            JSONObject obj = new JSONObject(previousValue);
            obj.put("product_quantity",String.valueOf(selectedQuantity));
            startActivity(new Intent(ShowQuantity.this, ProductDetail.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(ConstantVariables.SELECTED_USER_PLAN_KEY, obj.toString()));
            finish();
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        }
        catch(Exception e)
        {

        }

        super.onBackPressed();
    }
}
