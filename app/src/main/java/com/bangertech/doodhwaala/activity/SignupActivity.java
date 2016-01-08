package com.bangertech.doodhwaala.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.application.DoodhwaalaApplication;
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
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.json.JSONObject;

import java.util.List;


public class SignupActivity extends AppCompatActivity implements  AsyncResponse, Validator.ValidationListener {
    private Context mContext;
    private Activity mActivity;
    private General general;
    private MyAsynTaskManager myAsyncTask;

    @Required(order = 1)
    @Email(order = 2, message = "Please Check and Enter a valid Email Address")
    private EditText txtEmailAddress;

    @Required(order = 3)
    @TextRule(order = 4, minLength = 2, message = "Enter valid Name with minimum 2 chars")
    private EditText txtName;

    @Required(order = 5)
    @NumberRule(order = 6, message = "Enter Mobile Number in Numeric", type = NumberRule.NumberType.LONG)
    @TextRule(order = 7, message = "Enter valid Mobile Number", minLength = 10, maxLength = 10)
    private EditText txtPhonenumber;

    @Required(order = 8)
    @Password(order = 9)
    @TextRule(order = 10, minLength = 6, maxLength = 35, trim = true, message = "Enter Minimum 6 characters")
    private EditText txtPassword;

    private String email = null, device_id = null;
    private Toolbar toolbar;
    private Button tvSave;
    private Validator validator;
    private TextView terms_services, policy;
    private GcmCreatingClass gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setPadding(0, CUtils.getStatusBarHeight(SignupActivity.this), 0, 0);
        getSupportActionBar().setTitle("Sign up");

        tvSave = (Button) toolbar.findViewById(R.id.tvSave);
        email = getIntent().getExtras().getString("email");
        init();
        txtEmailAddress.setText(email);
        device_id = general.deviceId(mContext);

        validator = new Validator(this);
        validator.setValidationListener(this);

        gcm = new GcmCreatingClass(SignupActivity.this, SignupActivity.this);

        gcm.registerGCM();

        gcm.registerInBackground();

        System.out.println("SignupActivity "+gcm.registerGCM()+" "+gcm.registerInBackground());

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
    private void init() {
        mContext = SignupActivity.this;
        mActivity = SignupActivity.this;
        general = new General();
        txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
        txtName = (EditText) findViewById(R.id.txtName);
        txtPhonenumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        terms_services = (TextView) findViewById(R.id.terms_services);
        policy = (TextView) findViewById(R.id.policy);
       // btnSignup = (Button) findViewById(R.id.btnSignup);

        tvSave.setOnClickListener(clickListener);
        terms_services.setOnClickListener(clickListener);
        policy.setOnClickListener(clickListener);


        txtEmailAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (s.length() > 0) {
                    // position the text type in the left top corner
                    // txtEmailAddress.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    onOffSignUpButton();
                } else {
                    // no text entered. Center the hint text.
                    txtEmailAddress.setGravity(Gravity.CENTER);
                    onOffSignUpButton();
                }*/
            }
        });
        txtName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (s.length() > 0) {
                    // position the text type in the left top corner
                   // txtName.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    onOffSignUpButton();
                } else {
                    // no text entered. Center the hint text.
                    txtName.setGravity(Gravity.CENTER);
                    onOffSignUpButton();
                }*/
            }
        });
        txtPhonenumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 10)
                {
                    txtPassword.requestFocus();
                }
                /*if (s.length() > 0) {
                    // position the text type in the left top corner
                   // txtPhonenumber.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    onOffSignUpButton();
                } else {
                    // no text entered. Center the hint text.
                    txtPhonenumber.setGravity(Gravity.CENTER);
                    onOffSignUpButton();
                }*/
            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if (s.length() > 0) {
                    // position the text type in the left top corner
                   // txtPassword.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    onOffSignUpButton();
                } else {
                    // no text entered. Center the hint text.
                    txtPassword.setGravity(Gravity.CENTER);
                    onOffSignUpButton();
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
    private void onOffSignUpButton()
    {
       if(txtName.getText().length()>0 ||txtPhonenumber.getText().length()>0||
                txtEmailAddress.getText().length()>0||txtPassword.getText().length()>0)
            tvSave.setVisibility(View.VISIBLE);
        else
           tvSave.setVisibility(View.GONE);
    }
    public void SignUp() {
        myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("SignUp", mActivity, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "email_id", "mobile", "password", "name", "device_id", "device_type"},
                new String[]{"user", "signUp", txtEmailAddress.getText().toString(), txtPhonenumber.getText().toString(), txtPassword.getText().toString(),
                txtName.getText().toString(), gcm.registerGCM(), "1"});
        myAsyncTask.execute();
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
           // case R.id.btnSignup:
            *//*case R.id.llSignUp:
                if(txtEmailAddress.getText().toString().length()>0) {
                    if(!general.validate(txtEmailAddress.getText().toString())) {
                        Toast.makeText(mContext, "Please Enter A valid Email address", Toast.LENGTH_LONG).show();
                    } else if(txtName.getText().toString().length() == 0) {
                        Toast.makeText(mContext, "Please Enter Your name", Toast.LENGTH_LONG).show();
                    } else if(txtPhonenumber.getText().toString().length() > 0 && txtPhonenumber.getText().toString().length() != 10) {
                        Toast.makeText(mContext, "Please Enter A valid Phone number", Toast.LENGTH_LONG).show();
                    } else if (txtPassword.getText().toString().length()<=0){
                        Toast.makeText(mContext, "Please Enter password", Toast.LENGTH_LONG).show();
                    } else {
                        if(general.isNetworkAvailable(mContext)) {
                            SignUp();
                        } else {
                            Toast.makeText(mContext, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(mContext, "Please Enter Email address", Toast.LENGTH_LONG).show();
                }
            break;*//*

            case R.id.tvSave:
                validator.validate();
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            break;

        }


    }*/

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvSave:
                    validator.validate();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    break;

                case R.id.terms_services:
                    Intent intent= new Intent(SignupActivity.this, PrivatePolicy.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("terms","Terms and Condition");
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                    break;

                case R.id.policy:
                    Intent navigation= new Intent(SignupActivity.this, PrivatePolicy.class);
                    navigation.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    navigation.putExtra("terms","Private Policy");
                    startActivity(navigation);
                    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                    break;
            }
        }
    };

    @Override
    public void backgroundProcessFinish(String from, String output) {
        String user_id;
        if(from.equalsIgnoreCase("SignUp")){

            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    user_id = jsonObject.getString("user_id");



                    if (jsonObject.getBoolean("result")) {
                        PreferenceManager.getInstance().setUserEmailId(txtEmailAddress.getText().toString());
                        PreferenceManager.getInstance().setUserDetails
                                (jsonObject.getString("user_id"), PreferenceManager.getInstance().getUserEmailId());
                        PreferenceManager.getInstance().setUserId(PreferenceManager.getInstance().getUserId());

                        //PreferenceManager.getInstance().setUserId(jsonObject.getString("user_id"));
                        CGlobal.getCGlobalObject().setUserId(jsonObject.getString("user_id"));
                       // Intent mobileIntent = new Intent(mContext, ProfileActivity.class);
                        Intent mobileIntent = new Intent(mContext, Home.class);
                        mobileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mobileIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    } else {
                        if(!jsonObject.getString("msg").equals("failed")) {
                            customConfirmSignInDialogbox(SignupActivity.this);
                        }
                        else {
                            Toast.makeText(mContext, "Something wrong while SignUp", Toast.LENGTH_LONG).show();
                        }
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
        SignUp();
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

    public void customConfirmSignInDialogbox(Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_signin);

        //TextView header = (TextView) dialog.findViewById(R.id.title);
        //header.setText(title);
        //TextView text = (TextView) dialog.findViewById(R.id.text);
        //text.setText(message);

        Button sign_in = (Button) dialog.findViewById(R.id.sign_in);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getInstance().resetUserDetails();
                DoodhwaalaApplication.isUserLoggedIn = false;
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("email", txtEmailAddress.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
                Log.e("Saisheshan", "SignIn");
                dialog.dismiss();
            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        dialog.show();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        //dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
