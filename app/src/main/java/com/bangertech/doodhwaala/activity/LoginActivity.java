package com.bangertech.doodhwaala.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bangertech.doodhwaala.utils.ConstantVariables;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.helpshift.app.ActionBarActivity;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse,
        ConnectionCallbacks, OnConnectionFailedListener, GoogleApiClient.ServerAuthCodeCallbacks, Validator.ValidationListener {
    Context mContext;
    Activity mActivity;
    //General general;
    private CallbackManager callbackManager;
    MyAsynTaskManager myAsyncTask;
    Button btnGmailLogin, btnNext, btnFacebookLogin;

    @Required(order = 1)
    @Email(order = 2, message = "Please Check and Enter a valid Email Address")
    private EditText txtEmailAddress;

    String device_id = null, email = null, str_id = null, str_name = null;

    boolean facebookClick =false, googleClck = false;

    private static final String TAG = "android-plus-quickstart";

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;

    private static final String SAVED_PROGRESS = "sign_in_progress";

    // Client ID for a web server that will receive the auth code and exchange it for a
    // refresh token if offline access is requested.
    private static final String WEB_CLIENT_ID = "930318643687-2masmltopj7mkliqt6643bcg1vujh9br.apps.googleusercontent.com";

    // Base URL for your token exchange server, no trailing slash.
    private static final String SERVER_BASE_URL = "https://accounts.google.com/o/oauth2/token";

    // URL where the client should GET the scopes that the server would like granted
    // before asking for a serverAuthCode
    private static final String EXCHANGE_TOKEN_URL = SERVER_BASE_URL + "/exchangetoken";

    // URL where the client should POST the serverAuthCode so that the server can exchange
    // it for a refresh token,
    private static final String SELECT_SCOPES_URL = SERVER_BASE_URL + "/selectscopes";


    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private GoogleApiClient mGoogleApiClient;

    // We use mSignInProgress to track whether user has clicked sign in.
    // mSignInProgress can be one of three values:
    //
    //       STATE_DEFAULT: The default state of the application before the user
    //                      has clicked 'sign in', or after they have clicked
    //                      'sign out'.  In this state we will not attempt to
    //                      resolve sign in errors and so will display our
    //                      Activity in a signed out state.
    //       STATE_SIGN_IN: This state indicates that the user has clicked 'sign
    //                      in', so resolve successive errors preventing sign in
    //                      until the user has successfully authorized an account
    //                      for our app.
    //   STATE_IN_PROGRESS: This state indicates that we have started an intent to
    //                      resolve an error, and so we should not start further
    //                      intents until the current intent completes.
    private int mSignInProgress;

    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;

    // Used to store the error code most recently returned by Google Play services
    // until the user clicks 'sign in'.
    private int mSignInError;

    // Used to determine if we should ask for a server auth code when connecting the
    // GoogleApiClient.  False by default so that this sample can be used without configuring
    // a WEB_CLIENT_ID and SERVER_BASE_URL.
    private boolean mRequestServerAuthCode = false;

    // Used to mock the state of a server that would receive an auth code to exchange
    // for a refresh token,  If true, the client will assume that the server has the
    // permissions it wants and will not send an auth code on sign in.  If false,
    // the client will request offline access on sign in and send and new auth code
    // to the server.  True by default because this sample does not implement a server
    // so there would be nowhere to send the code.
    private boolean mServerHasToken = true;
    private Toolbar mToolbar;

    private TextView header;
    private GcmCreatingClass gcm;
    private String googleEmail, fbEmail;
    private Validator validator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        boolean finish = getIntent().getBooleanExtra("finish", false);
        /*if (finish) {
            //startActivity(new Intent(mContext, LoginActivity.class));
            //finish();
            return;
        }*/
        setContentView(R.layout.activity_login);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mToolbar.setMinimumHeight(CUtils.getStatusBarHeight(LoginActivity.this));
        mToolbar.setVisibility(View.GONE);

        //  Sarvesh Generate SHA key
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e){
        }catch (NoSuchAlgorithmException e){
        }*/
        init();

        this.mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        gcm = new GcmCreatingClass(LoginActivity.this, LoginActivity.this);

        gcm.registerGCM();

        gcm.registerInBackground();

        validator = new Validator(this);
        validator.setValidationListener(this);

        /*PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.bangertech.doodhwaala", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/
    }

    private void init() {
        mContext = LoginActivity.this;
        mActivity = LoginActivity.this;
        //general = new General();

        mGoogleApiClient = buildGoogleApiClient();
        // device_id = general.deviceId(mContext);
        device_id = CUtils.deviceId(mContext);
        btnGmailLogin = (Button) findViewById(R.id.btnGmailLogin);
        btnFacebookLogin = (Button) findViewById(R.id.btnFacebookLogin);
        btnNext = (Button) findViewById(R.id.btnNext);

        txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
        header = (TextView) findViewById(R.id.header);

        Typeface face = Typeface.createFromAsset(this.getAssets(),
                "fonts/myriad.ttf");
        header.setTypeface(face);

        btnGmailLogin.setOnClickListener(this);
        btnFacebookLogin.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        txtEmailAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (s.length() > 0) {
                    // position the text type in the left top corner
                    txtEmailAddress.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                } else {
                    // no text entered. Center the hint text.
                    txtEmailAddress.setGravity(Gravity.CENTER);
                }*/
            }
        });

        txtEmailAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    validator.validate();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Log.i(TAG, "Enter pressed");
                }
                return false;
            }

        });
    }

    private void onFblogin() {
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile", "user_friends"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");


                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                                str_id = json.getString("id");
                                                str_name = json.getString("name");
                                                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                                        new GraphRequest.GraphJSONObjectCallback() {
                                                            @Override
                                                            public void onCompleted(JSONObject object, GraphResponse response) {
                                                                try {
                                                                    fbEmail = object.getString("email");
                                                                    Log.d("user email ", "email" + fbEmail);
                                                                    facebookLogin(fbEmail, str_name, str_id);
                                                                    LoginManager.getInstance().logOut();
                                                                } catch (JSONException e) {
                                                                    // TODO Auto-generated catch block
                                                                    e.printStackTrace();
                                                                }

                                                            }

                                                        });
                                                Bundle parameters = new Bundle();
                                                parameters.putString("fields", "email");
                                                request.setParameters(parameters);
                                                request.executeAsync();


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }).executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("Cancel", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Error", error.toString());
                    }
                });

    }
    public void checkUser() {
        myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("checkUser", mActivity, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "email_id"},
                new String[]{"user", "checkUserExist", txtEmailAddress.getText().toString()});
        myAsyncTask.execute();
    }

    public void facebookLogin(String fbEmailId, String name, String fbId) {
        myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("facebookLogin", mActivity, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "facebookmailid", "name", "id", "device_id", "device_type"},
                new String[]{"user", "signInByFacebook", fbEmailId, name, fbId, gcm.registerGCM(), "1"});
        myAsyncTask.execute();
    }

    public void googleLogin(String googleEmailId, String name, String googleId) {
        myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("googleLogin", mActivity, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "googlemailid", "name", "googleid", "device_id", "device_type"},
                new String[]{"user", "signInByGoogle", googleEmailId, name, googleId, gcm.registerGCM(), "1"});
        myAsyncTask.execute();
    }
    @Override
    public void backgroundProcessFinish(String from, String output) {
        String user_id, email_id, name, mobile, facebook_id, google_id, msg, flag;
        if(from.equalsIgnoreCase("checkUser")){
            Log.i("Result", output);
            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getBoolean("result") == false) {

                        Intent signupIntent = new Intent(mContext, SignupActivity.class);
                        signupIntent.putExtra("email", txtEmailAddress.getText().toString());
                        startActivity(signupIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    }
                    else
                    {
                        if(jsonObject.getInt("flag") == 4)
                        {
                            Intent signinIntent = new Intent(mContext, SigninActivity.class);
                            signinIntent.putExtra("email", txtEmailAddress.getText().toString());
                            startActivity(signinIntent);
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                            finish();
                        }
                        else if (jsonObject.getInt("flag") == 1)
                        {
                            DialogManager.showDialog(mActivity, "You have already Signed up in Google. Please SignIn by Google.");
                        }
                        else if (jsonObject.getInt("flag") == 2)
                        {
                            DialogManager.showDialog(mActivity, "You have already Signed up in Facebook. Please SignIn by Facebook.");
                        }
                        else if (jsonObject.getInt("flag") == 3)
                        {
                            DialogManager.showDialog(mActivity, "You have already Signed up in Facebook and Google. Please SignIn by any one.");
                        }

                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                DialogManager.showDialog(mActivity, "Server Error Occurred! Try Again!");
            }
        } else if(from.equalsIgnoreCase("facebookLogin")){
            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    user_id = jsonObject.getString("user_id");
                    email_id = jsonObject.getString("email_id");
                    name = jsonObject.getString("name");
                    mobile = jsonObject.getString("mobile");
                    facebook_id = jsonObject.getString("facebook_id");
                    google_id = jsonObject.getString("google_id");
                    msg = jsonObject.getString("msg");
                    // PreferenceManager.getInstance().setUserId(user_id);


                    if (jsonObject.getBoolean("result")) {
                        Intent mobileIntent = new Intent(mContext, MobileNoActivity.class);
                        mobileIntent.putExtra("user_id", user_id);
                        startActivity(mobileIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    } else {
                        if(jsonObject.getString("mobile").equals("") || jsonObject.getString("mobile")== null) {
                            Intent mobileIntent = new Intent(mContext, MobileNoActivity.class);
                            mobileIntent.putExtra("user_id", user_id);
                            startActivity(mobileIntent);
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                            finish();
                        }
                        else {
                            if (msg.equals("failed")) {
                                Toast.makeText(getApplicationContext(), "Server Error Try Again", Toast.LENGTH_LONG).show();
                            } else {
                                PreferenceManager.getInstance().setUserEmailId(fbEmail);
                                PreferenceManager.getInstance().setUserDetails
                                        (jsonObject.getString("user_id"), PreferenceManager.getInstance().getUserEmailId());
                                PreferenceManager.getInstance().setUserId(PreferenceManager.getInstance().getUserId());

                                DoodhwaalaApplication.isUserLoggedIn = true;
                                CGlobal.getCGlobalObject().setUserId(jsonObject.getString("user_id"));
                                Intent mobileIntent = new Intent(mContext, Home.class);
                                startActivity(mobileIntent);
                                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                finish();
                            }
                        }

                        /*Intent mainIntent = new Intent(mContext, MainActivity.class);
                        mainIntent.putExtra("email", email_id);
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();*/
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                DialogManager.showDialog(mActivity, "Server Error Occured! Try Again!");
            }
        } else if(from.equalsIgnoreCase("googleLogin")){
            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    user_id = jsonObject.getString("user_id");
                    email_id = jsonObject.getString("email_id");
                    name = jsonObject.getString("name");
                    mobile = jsonObject.getString("mobile");
                    facebook_id = jsonObject.getString("facebook_id");
                    google_id = jsonObject.getString("google_id");
                    msg = jsonObject.getString("msg");
                    //PreferenceManager.getInstance().setUserId(user_id);


                    if (jsonObject.getBoolean("result")) {
                        Intent mobileIntent = new Intent(mContext, MobileNoActivity.class);
                        mobileIntent.putExtra("user_id", user_id);
                        startActivity(mobileIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    } else {
                        if(jsonObject.getString("mobile").equals("") || jsonObject.getString("mobile")== null) {
                            Intent mobileIntent = new Intent(mContext, MobileNoActivity.class);
                            mobileIntent.putExtra("user_id", user_id);
                            startActivity(mobileIntent);
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                            finish();
                        }
                        else {
                            if (msg.equals("failed")) {
                                DialogManager.showDialog(mActivity, "Server Error Occured! Try Again!");
                            } else {

                                PreferenceManager.getInstance().setUserEmailId(googleEmail);
                                PreferenceManager.getInstance().setUserDetails
                                        (jsonObject.getString("user_id"), PreferenceManager.getInstance().getUserEmailId());
                                PreferenceManager.getInstance().setUserId(PreferenceManager.getInstance().getUserId());

                                DoodhwaalaApplication.isUserLoggedIn = true;
                                CGlobal.getCGlobalObject().setUserId(jsonObject.getString("user_id"));
                                Intent mobileIntent = new Intent(mContext, Home.class);
                                startActivity(mobileIntent);
                                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                finish();
                            }
                        }
                        /*Intent mainIntent = new Intent(mContext, MainActivity.class);
                        mainIntent.putExtra("email", email_id);
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();*/
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGmailLogin:
               /* if(general.isNetworkAvailable(mContext)) {*/
                if(CUtils.isNetworkAvailable(mContext)) {
                    //mStatus.setText(R.string.status_signing_in);
                    facebookClick =false;
                    googleClck = true;
                    if (mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.disconnect();
                    }
                    mSignInProgress = STATE_SIGN_IN;
                    mGoogleApiClient.connect();
                } else {
                    Toast.makeText(mContext, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnFacebookLogin:
                LoginManager.getInstance().logOut();
               /* if(general.isNetworkAvailable(mContext)) {*/
                if(CUtils.isNetworkAvailable(mContext)) {
                    facebookClick =true;
                    googleClck = false;
                    onFblogin();
                } else {
                    Toast.makeText(mContext, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnNext:
                validator.validate();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        super.onActivityResult(reqCode, resCode, i);
        if(facebookClick) {
            callbackManager.onActivityResult(reqCode, resCode, i);
        } else if(googleClck) {
            switch (reqCode) {
                case RC_SIGN_IN:
                    if (resCode == RESULT_OK) {
                        // If the error resolution was successful we should continue
                        // processing errors.
                        mSignInProgress = STATE_SIGN_IN;
                    } else {
                        // If the error resolution was not successful or the user canceled,
                        // we should stop processing errors.
                        mSignInProgress = STATE_DEFAULT;
                    }

                    if (!mGoogleApiClient.isConnecting()) {
                        // If Google Play services resolved the issue with a dialog then
                        // onStart is not called so we need to re-attempt connection here.
                        mGoogleApiClient.connect();
                    }
                    break;
            }
        }
    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);

        if (mRequestServerAuthCode) {
            checkServerAuthConfiguration();
            builder = builder.requestServerAuthCode(WEB_CLIENT_ID, this);
        }

        return builder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
    }

    /* onConnected is called when our Activity successfully connects to Google
         * Play services.  onConnected indicates that an account was selected on the
         * device, that the selected account has granted any requested permissions to
         * our app and that we were able to establish a service connection to Google
         * Play services.
         */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        // Log.i(TAG, "onConnected");
        CUtils.printLog(TAG, "onConnected",ConstantVariables.LOG_TYPE.INFO);

        // Update the user interface to reflect that the user is signed in.
       /* mSignInButton.setEnabled(false);
        mSignOutButton.setEnabled(true);
        mRevokeButton.setEnabled(true);*/

        // Hide the sign-in options, they no longer apply
        //findViewById(R.id.layout_server_auth).setVisibility(View.GONE);

        // Retrieve some profile information to personalize our app for the user.
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        if(currentUser!=null){
            try {
                googleEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
                JSONObject jObj = new JSONObject(currentUser.toString());
                String name = jObj.getString("displayName");
                String id = jObj.getString("id");
                //String emailId = jObj.getString("");

                googleLogin(googleEmail, name, id);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        /*mStatus.setText(String.format(
                getResources().getString(R.string.signed_in_as),
                currentUser.getDisplayName()));*/

        /*Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                .setResultCallback(this);*/

        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;
    }
    /* onConnectionFailed is called when our Activity could not connect to Google
     * Play services.  onConnectionFailed indicates that the user needs to select
     * an account, grant permissions or resolve an error in order to sign in.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
     /*   Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());*/
        CUtils.printLog(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode(), ConstantVariables.LOG_TYPE.INFO);

        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // An API requested for GoogleApiClient is not available. The device's current
            // configuration might not be supported with the requested API or a required component
            // may not be installed, such as the Android Wear application. You may need to use a
            // second GoogleApiClient to manage the application's optional APIs.
            Log.w(TAG, "API Unavailable.");
        } else if (mSignInProgress != STATE_IN_PROGRESS) {
            // We do not have an intent in progress so we should store the latest
            // error resolution intent for use when the sign in button is clicked.
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {
                // STATE_SIGN_IN indicates the user already clicked the sign in button
                // so we should continue processing errors until the user is signed in
                // or they click cancel.
                resolveSignInError();
            }
        }

        // In this sample we consider the user signed out whenever they do not have
        // a connection to Google Play services.
        // onSignedOut();
    }

    /* Starts an appropriate intent or dialog for user interaction to resolve
     * the current error preventing the user from being signed in.  This could
     * be a dialog allowing the user to select an account, an activity allowing
     * the user to consent to the permissions being requested by your app, a
     * setting to enable device networking, etc.
     */
    private void resolveSignInError() {
        if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error.  For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback.  This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
               /* Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());*/
                CUtils.printLog(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage(), ConstantVariables.LOG_TYPE.INFO);
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // Google Play services wasn't able to provide an intent for some
            // error types, so we show the default Google Play services error
            // dialog which may still start an intent on our behalf if the
            // user can resolve the issue.
            createErrorDialog().show();
        }
    }


    private Dialog createErrorDialog() {
        if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
            return GooglePlayServicesUtil.getErrorDialog(
                    mSignInError,
                    this,
                    RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // Log.e(TAG, "Google Play services resolution cancelled");
                            CUtils.printLog(TAG, "Google Play services resolution cancelled",ConstantVariables.LOG_TYPE.ERROR);
                            mSignInProgress = STATE_DEFAULT;
                        }
                    });
        } else {
            return new AlertDialog.Builder(this)
                    .setMessage(R.string.play_services_error)
                    .setPositiveButton(R.string.close,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                  /*  Log.e(TAG, "Google Play services error could not be "
                                            + "resolved: " + mSignInError);*/
                                    CUtils.printLog(TAG, "Google Play services error could not be "
                                            + "resolved: " + mSignInError, ConstantVariables.LOG_TYPE.ERROR);
                                    mSignInProgress = STATE_DEFAULT;
                                }
                            }).create();
        }
    }

    private void checkServerAuthConfiguration() {
        // Check that the server URL is configured before allowing this box to
        // be unchecked
        if ("WEB_CLIENT_ID".equals(WEB_CLIENT_ID) ||
                "SERVER_BASE_URL".equals(SERVER_BASE_URL)) {
            //Log.w(TAG, "WEB_CLIENT_ID or SERVER_BASE_URL configured incorrectly.");
            CUtils.printLog(TAG,  "WEB_CLIENT_ID or SERVER_BASE_URL configured incorrectly.", ConstantVariables.LOG_TYPE.WARNING);
            Dialog dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.configuration_error))
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();

            dialog.show();
        }
    }
    @Override
    public boolean onUploadServerAuthCode(String idToken, String serverAuthCode) {
        // Upload the serverAuthCode to the server, which will attempt to exchange it for
        // a refresh token.  This callback occurs on a background thread, so it is OK
        // to perform synchronous network access.  Returning 'false' will fail the
        // GoogleApiClient.connect() call so if you would like the client to ignore
        // server failures, always return true.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(EXCHANGE_TOKEN_URL);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("serverAuthCode", serverAuthCode));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            final String responseBody = EntityUtils.toString(response.getEntity());
           /* Log.i(TAG, "Code: " + statusCode);
            Log.i(TAG, "Resp: " + responseBody);
*/

            CUtils.printLog(TAG, "Code: " + statusCode, ConstantVariables.LOG_TYPE.INFO);
            CUtils.printLog(TAG, "Resp: " + responseBody, ConstantVariables.LOG_TYPE.INFO);

            // Show Toast on UI Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, responseBody, Toast.LENGTH_LONG).show();
                }
            });
            return (statusCode == 200);
        } catch (ClientProtocolException e) {
            //  Log.e(TAG, "Error in auth code exchange.", e);
            CUtils.printLog(TAG, "Error in auth code exchange."+ e.getMessage(), ConstantVariables.LOG_TYPE.ERROR);
            return false;
        } catch (IOException e) {
            // Log.e(TAG, "Error in auth code exchange.", e);
            CUtils.printLog(TAG, "Error in auth code exchange." + e.getMessage(), ConstantVariables.LOG_TYPE.ERROR);
            return false;
        }
    }

    @Override
    public CheckResult onCheckServerAuthorization(String idToken, Set<Scope> scopeSet) {
        //Log.i(TAG, "Checking if server is authorized.");
        //Log.i(TAG, "Mocking server has refresh token: " + String.valueOf(mServerHasToken));
        CUtils.printLog(TAG, "Checking if server is authorized.", ConstantVariables.LOG_TYPE.INFO);
        CUtils.printLog(TAG, "Mocking server has refresh token: " + String.valueOf(mServerHasToken), ConstantVariables.LOG_TYPE.INFO);

        if (!mServerHasToken) {
            // Server does not have a valid refresh token, so request a new
            // auth code which can be exchanged for one.  This will cause the user to see the
            // consent dialog and be prompted to grant offline access. This callback occurs on a
            // background thread so it is OK to do synchronous network access.

            // Ask the server which scopes it would like to have for offline access.  This
            // can be distinct from the scopes granted to the client.  By getting these values
            // from the server, you can change your server's permissions without needing to
            // recompile the client application.
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(SELECT_SCOPES_URL);
            HashSet<Scope> serverScopeSet = new HashSet<Scope>();

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int responseCode = httpResponse.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(httpResponse.getEntity());

                if (responseCode == 200) {
                    String[] scopeStrings = responseBody.split(" ");
                    for (String scope : scopeStrings) {
                        // Log.i(TAG, "Server Scope: " + scope);
                        CUtils.printLog(TAG, "Server Scope: " + scope, ConstantVariables.LOG_TYPE.INFO);
                        serverScopeSet.add(new Scope(scope));
                    }
                } else {
                    // Log.e(TAG, "Error in getting server scopes: " + responseCode);
                    CUtils.printLog(TAG, "Error in getting server scopes: " + responseCode, ConstantVariables.LOG_TYPE.ERROR);
                }

            } catch (ClientProtocolException e) {
                //Log.e(TAG, "Error in getting server scopes.", e);
                CUtils.printLog(TAG,"Error in getting server scopes."+ e.getMessage(), ConstantVariables.LOG_TYPE.ERROR);
            } catch (IOException e) {
                //Log.e(TAG, "Error in getting server scopes.", e);
                CUtils.printLog(TAG, "Error in getting server scopes." + e.getMessage(), ConstantVariables.LOG_TYPE.ERROR);
            }

            // This tells GoogleApiClient that the server needs a new serverAuthCode with
            // access to the scopes in serverScopeSet.  Note that we are not asking the server
            // if it already has such a token because this is a sample application.  In reality,
            // you should only do this on the first user sign-in or if the server loses or deletes
            // the refresh token.
            return CheckResult.newAuthRequiredResult(serverScopeSet);
        } else {
            // Server already has a valid refresh token with the correct scopes, no need to
            // ask the user for offline access again.
            return CheckResult.newAuthNotRequiredResult();
        }
    }

    @Override
    public void onValidationSucceeded() {
        if(CUtils.isNetworkAvailable(mContext)) {
            checkUser();
        } else {
            Toast.makeText(mContext, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
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
    /*@Override
    protected void onStart()
    {
        super.onStart();
        FlurryAgent.onStartSession(mContext, String.valueOf(R.string.flurryKey));
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        FlurryAgent.onEndSession(mContext);
    }*/
}



