package com.bangertech.doodhwaala.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.gcm.GcmCreatingClass;
import com.bangertech.doodhwaala.general.General;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CGlobal;
import com.bangertech.doodhwaala.utils.CUtils;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.json.JSONObject;


public class SigninActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse, Validator.ValidationListener {
    Context mContext;
    Activity mActivity;
    TextView btnForgot;
    MyAsynTaskManager myAsyncTask;
    General general;
    @Required(order = 1)
    @Email(order = 2, message = "Please Check and Enter a valid Email Address")
    private EditText txtEmailAddress;

    @Required(order = 3)
    @Password(order = 4)
    @TextRule(order = 5, minLength = 6, maxLength = 35, trim = true, message = "Please enter Minimum 6 characters")
    private EditText txtPassword;

    private EditText forgotEmailAddress;

    String email = null, device_id = null;
    private GcmCreatingClass gcm;
    private Button btn_save;
    private Toolbar toolbar;
    private Validator validator;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Intent i = getIntent();
        if(i!=null)
        {
            email = i.getExtras().getString("email");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // toolbar.setPadding(0, CUtils.getStatusBarHeight(SigninActivity.this), 0, 0);
        getSupportActionBar().setTitle("Sign in");
        btn_save = (Button) findViewById(R.id.btn_save);

        gcm = new GcmCreatingClass(SigninActivity.this, SigninActivity.this);

        gcm.registerGCM();

        gcm.registerInBackground();

        System.out.println("SigninActivity " + gcm.registerGCM() + " " + gcm.registerInBackground());

        init();
        txtEmailAddress.setText(email);
        device_id = general.deviceId(mContext);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void init() {
        mContext = SigninActivity.this;
        mActivity = SigninActivity.this;
        general = new General();
        txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnForgot = (TextView) findViewById(R.id.btnForgot);

        btnForgot.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        txtEmailAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if (s.length() > 0) {
                    // position the text type in the left top corner
                    txtEmailAddress.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                } else {
                    // no text entered. Center the hint text.
                    txtEmailAddress.setGravity(Gravity.CENTER);
                }*/
            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (s.length() > 0) {
                    // position the text type in the left top corner
                    txtPassword.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                } else {
                    // no text entered. Center the hint text.
                    txtPassword.setGravity(Gravity.CENTER);
                }*/
            }
        });

        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnForgot:
                customForgotPasswordDialogbox(SigninActivity.this);
                break;

            case R.id.btn_save:
                validator.validate();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }
    public void SignIn() {
        myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("SignIn", mActivity, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "email_id", "password", "device_id", "device_type"},
                new String[]{"user", "signInByDoodh", txtEmailAddress.getText().toString(), txtPassword.getText().toString(), gcm.registerGCM(), "1"});
        myAsyncTask.execute();
    }

    public void ForgotPassword() {
        myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("ForgotPassword", mActivity, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "email_id"},
                new String[]{"user", "forgotPassword", forgotEmailAddress.getText().toString()});
        myAsyncTask.execute();
    }

    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("SignIn")){

            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);

                    if (jsonObject.getBoolean("result")) {
                        PreferenceManager.getInstance().setUserEmailId(txtEmailAddress.getText().toString());
                        PreferenceManager.getInstance().setUserDetails
                                (jsonObject.getString("user_id"), PreferenceManager.getInstance().getUserEmailId());
                        PreferenceManager.getInstance().setUserId(jsonObject.getString("user_id"));
                       // Intent mobileIntent = new Intent(mContext, ProfileActivity.class);
                        CGlobal.getCGlobalObject().setUserId(jsonObject.getString("user_id"));
                        Intent mobileIntent = new Intent(mContext, Home.class);
                        mobileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mobileIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    } else {
                        DialogManager.showDialog(mActivity, jsonObject.getString("msg"));
                        //Toast.makeText(mContext, "Something wrong while SignUp", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                DialogManager.showDialog(mActivity, "Server Error Occurred! Try Again!");
            }
        }
        else if(from.equalsIgnoreCase("ForgotPassword")){

            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);

                    if (jsonObject.getBoolean("result")) {
                        DialogManager.showDialog(mActivity, jsonObject.getString("msg"));
                    } else {
                        DialogManager.showDialog(mActivity, jsonObject.getString("msg"));
                        //Toast.makeText(mContext, "Something wrong while SignUp", Toast.LENGTH_LONG).show();
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

    public void SignInBtn()
    {
        if(txtEmailAddress.getText().toString().length()>0) {
            if(!general.validate(txtEmailAddress.getText().toString())) {
                Toast.makeText(mContext, "Please Enter A valid Email address", Toast.LENGTH_LONG).show();
            } else if (txtPassword.getText().toString().length()<=0){
                Toast.makeText(mContext, "Please Enter password", Toast.LENGTH_LONG).show();
            } else {
                if(general.isNetworkAvailable(mContext)) {
                    SignIn();
                } else {
                    Toast.makeText(mContext, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(mContext, "Please Enter Email address", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onValidationSucceeded() {
            SignInBtn();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
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

    public void customForgotPasswordDialogbox(Activity context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.email_dialog);

        //TextView header = (TextView) dialog.findViewById(R.id.title);
        //header.setText(title);
        //TextView text = (TextView) dialog.findViewById(R.id.text);
        //text.setText(message);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        forgotEmailAddress = (EditText) dialog.findViewById(R.id.forgotEmail);

        forgotEmailAddress.setText(txtEmailAddress.getText().toString());

        Button btn_forgot = (Button) dialog.findViewById(R.id.btn_forgot);

        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!general.validate(forgotEmailAddress.getText().toString())) {
                    forgotEmailAddress.setError("Please enter a valid Email Address");
                }
                else {
                    ForgotPassword();
                }
            }
        });

        forgotEmailAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if(!general.validate(forgotEmailAddress.getText().toString())) {
                        forgotEmailAddress.setError("Please enter a valid Email Address");
                    }
                    else {
                        ForgotPassword();
                    }
                }
                return false;
            }

        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        //dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
