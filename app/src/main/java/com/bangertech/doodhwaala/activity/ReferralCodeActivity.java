package com.bangertech.doodhwaala.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.json.JSONObject;

/**
 * Created by annutech on 1/2/2016.
 */
public class ReferralCodeActivity extends AppCompatActivity implements AsyncResponse, Validator.ValidationListener {
    private TextView referral_text_one, delivery_places, delivery_places_two, tvi_success_blue, tvi_success_gray;

    @Required(order = 1)
    @TextRule(order = 2, minLength = 6, maxLength = 6, trim = true, message = "Please enter minimum 6 characters")
    private EditText et_referral_code;

    private Button btn_apply, tvContinue;
    private Toolbar toolbar;
    private MyAsynTaskManager myAsyncTask;
    private Validator validator;
    private RelativeLayout rl_referral_code_success, rl_referral_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referral_code);

        validator = new Validator(this);
        validator.setValidationListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setPadding(0, CUtils.getStatusBarHeight(EditMyPlan.this), 0, 0);
        getSupportActionBar().setTitle("Welcome");
        tvContinue = (Button) toolbar.findViewById(R.id.tvSave);
        tvContinue.setText("CONTINUE");

        tvContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ReferralCodeActivity.this, Home.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
            }
        });


        referral_text_one = (TextView) findViewById(R.id.referral_text_one);
        delivery_places = (TextView) findViewById(R.id.delivery_places);
        et_referral_code = (EditText) findViewById(R.id.et_referral_code);
        delivery_places_two = (TextView) findViewById(R.id.delivery_places_two);
        rl_referral_code_success = (RelativeLayout) findViewById(R.id.rl_referral_code_success);
        rl_referral_code = (RelativeLayout) findViewById(R.id.rl_referral_code);
        tvi_success_blue = (TextView) findViewById(R.id.tvi_success_blue);
        tvi_success_gray = (TextView) findViewById(R.id.tvi_success_gray);
        btn_apply = (Button) findViewById(R.id.btn_apply);

        referral_text_one.setTypeface(CUtils.LightTypeFace(getApplicationContext()));
        delivery_places.setTypeface(CUtils.LightTypeFace(getApplicationContext()));
        et_referral_code.setTypeface(CUtils.LightTypeFace(getApplicationContext()));
        delivery_places_two.setTypeface(CUtils.LightTypeFace(getApplicationContext()));
        btn_apply.setTypeface(CUtils.LightTypeFace(getApplicationContext()));
        btn_apply.setTypeface(CUtils.LightTypeFace(getApplicationContext()));
        tvi_success_blue.setTypeface(CUtils.LightTypeFace(getApplicationContext()));
        tvi_success_gray.setTypeface(CUtils.LightTypeFace(getApplicationContext()));

        et_referral_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    validator.validate();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }

        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        deliveryLocalities();
    }

    public void deliveryLocalities() {
        myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("deliveryLocalities", ReferralCodeActivity.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action"},
                new String[]{"user", "deliveryLocalities"});
        myAsyncTask.execute();
    }

    public void updateFriendReferralCode() {
        myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("updateFriendReferralCode", ReferralCodeActivity.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "user_id", "friend_referral_code"},
                new String[]{"user", "updateFriendReferralCode", PreferenceManager.getInstance().getUserId(), et_referral_code.getText().toString()});
        myAsyncTask.execute();
    }

    @Override
    public void backgroundProcessFinish(String from, String output) {
        String friend_id,friend_referral_code;
        if (from.equalsIgnoreCase("deliveryLocalities")) {
            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getBoolean("result")) {
                        delivery_places.setText("Delivery to "+jsonObject.getString("delivery_localities"));
                        delivery_places_two.setText("Delivery only to "+jsonObject.getString("delivery_localities"));
                    } else {
                        Toast.makeText(ReferralCodeActivity.this, "Localities are not available", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                DialogManager.showDialog(ReferralCodeActivity.this, "Server Error Occurred! Try Again!");
            }
        }

        if(from.equalsIgnoreCase("updateFriendReferralCode")) {
            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getBoolean("result")) {
                        friend_id = jsonObject.getString("friend_id");
                        friend_referral_code = jsonObject.getString("friend_referral_code");

                        PreferenceManager.getInstance().setFriendReferralCode(friend_referral_code);
                        PreferenceManager.getInstance().setFriendReferralCode(friend_id);
                        rl_referral_code.setVisibility(View.GONE);
                        rl_referral_code_success.setVisibility(View.VISIBLE);

                        //Toast.makeText(ReferralCodeActivity.this, "Hurray! Place your first order and get 5% discount", Toast.LENGTH_LONG).show();
                    } else {
                        if(jsonObject.getString("msg").equals("failed")) {
                            Toast.makeText(ReferralCodeActivity.this, "Referral Code is not updated", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ReferralCodeActivity.this, "Referral Code is Invalid", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                DialogManager.showDialog(ReferralCodeActivity.this, "Server Error Occurred! Try Again!");
            }
        }
    }

    @Override
    public void onValidationSucceeded() {
        updateFriendReferralCode();
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
            //((EditText) failedView).setError(Html.fromHtml("<font color='black'>" + message + "</font>"));
        }
    }
}
