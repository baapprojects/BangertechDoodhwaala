package com.bangertech.doodhwaala.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.application.DoodhwaalaApplication;
import com.bangertech.doodhwaala.general.General;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CUtils;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.json.JSONObject;


public class MobileNoActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse, Validator.ValidationListener {
    Context mContext;
    Activity mActivity;
    General general;
    MyAsynTaskManager myAsyncTask;

    @Required(order = 1)
    @NumberRule(order = 2, message = "Enter Mobile Number in Numeric", type = NumberRule.NumberType.LONG)
    @TextRule(order = 3, message = "Enter valid Mobile Number", minLength = 10, maxLength = 10)
    private EditText txtPhonenumber;

    String user_id;
    private Toolbar mToolbar;

    private Validator validator;

    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_no);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mToolbar.setMinimumHeight(CUtils.getStatusBarHeight(MobileNoActivity.this));
        mToolbar.setVisibility(View.GONE);

        validator = new Validator(this);
        validator.setValidationListener(this);

        user_id = getIntent().getExtras().getString("user_id");
        init();

        customMobileDialogbox(MobileNoActivity.this);

    }

    private void init() {
        mContext = MobileNoActivity.this;
        mActivity = MobileNoActivity.this;
        general = new General();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnProceed:
                break;
        }
    }
    public void updateMobileNumber() {
        myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("updateMobileNumber", mActivity, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "user_id", "mobile"},
                new String[]{"user", "updateMobileNumber", user_id, txtPhonenumber.getText().toString()});
        myAsyncTask.execute();
    }

    @Override
    public void backgroundProcessFinish(String from, String output) {
        if (from.equalsIgnoreCase("updateMobileNumber")) {
            Log.i("Result", output);
            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getBoolean("result")) {
                        Intent mainIntent = new Intent(mContext, Home.class);
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    } else {
                        Toast.makeText(mContext, "Something missing Please check your mobile no", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                DialogManager.showDialog(mActivity, "Server Error Occured! Try Again!");
            }
        }
    }

    @Override
    public void onValidationSucceeded() {
        if(txtPhonenumber.getText().toString().length() > 0 && txtPhonenumber.getText().toString().length() == 10) {
            if(general.isNetworkAvailable(mContext)) {
                updateMobileNumber();
                dialog.dismiss();
            } else {
                Toast.makeText(mContext, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mContext, "Please Enter A valid Phone number", Toast.LENGTH_LONG).show();
        }

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

    public void customMobileDialogbox(Activity context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mobile_dialog);

        //TextView header = (TextView) dialog.findViewById(R.id.title);
        //header.setText(title);
        //TextView text = (TextView) dialog.findViewById(R.id.text);
        //text.setText(message);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        txtPhonenumber = (EditText) dialog.findViewById(R.id.txtPhonenumber);

        Button done = (Button) dialog.findViewById(R.id.btnProceed);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        txtPhonenumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        //dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
