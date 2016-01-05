package com.bangertech.doodhwaala.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.beans.BeanAddress;
import com.bangertech.doodhwaala.general.General;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CGlobal;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by annutech on 10/12/2015.
 */
public class ShowConfirmation extends AppCompatActivity implements AsyncResponse, Validator.ValidationListener {
    private TextView txtViewProductName,txtViewProductPrice,txtViewFrequency,
            txtViewFrequencyPrice,txtViewDuration, txtViewDurationPrice,txtViewAddress;
    private ImageView imageViewProduct;
    private String   product_id,product_mapping_id,quantity_id,from_date,to_date,address_id="0",paid_amount,status;
    private String product_price,product_image_url,frequency_id,frequency_name,duration_id,duration_name,product_quantity,product_name,quantity;
    private LinearLayout lldiscount, llcoupon;
    private TextView tviCouponText, txtViewDiscountPrice;
    private Dialog dialog;

    @Required(order = 1)
    @TextRule(order = 2, minLength = 4, message = "Enter valid Coupon Code with minimum 4 chars")
    private EditText editCoupon;

    private String couponCode;
    private Validator validator;
    private General general;
    private double  paidAmount;

    private ImageView ivConfirmationScreen;

    private String gross_price, discount_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        general = new General();

        lldiscount = (LinearLayout) findViewById(R.id.lldiscount);
        llcoupon = (LinearLayout) findViewById(R.id.llcoupon);
        tviCouponText = (TextView) findViewById(R.id.tviCouponText);
        txtViewDiscountPrice = (TextView) findViewById(R.id.txtViewDiscountPrice);

        imageViewProduct=(ImageView)findViewById(R.id.imageViewProduct);
        txtViewProductName=(TextView)findViewById(R.id.txtViewProductName);
        txtViewProductPrice=(TextView)findViewById(R.id.txtViewProductPrice);
        txtViewFrequency=(TextView)findViewById(R.id.txtViewFrequency);
        txtViewFrequencyPrice=(TextView)findViewById(R.id.txtViewFrequencyPrice);
        txtViewDuration=(TextView)findViewById(R.id.txtViewDuration);
        txtViewDurationPrice=(TextView)findViewById(R.id.txtViewDurationPrice);
        txtViewAddress=(TextView)findViewById(R.id.txtViewAddress);
        ivConfirmationScreen = (ImageView) findViewById(R.id.ivConfirmationScreen);

        validator = new Validator(this);
        validator.setValidationListener(this);

        tviCouponText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCouponDialogbox(ShowConfirmation.this);
            }
        });

        ((TextView)findViewById(R.id.txtViewEditAddress)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putBoolean("IS_FROM_CONFIRMATION_SCREEN", true);
                startActivityForResult(new Intent(ShowConfirmation.this, ShowAddress.class).putExtras(bundle), ConstantVariables.SUB_ACTIVITY_EDIT_ADDRESS_FROM_CONFIRMATION);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        initValues(getIntent().getStringExtra(ConstantVariables.SELECTED_USER_PLAN_KEY));
        fetchAddressFromServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAddressFromServer();
    }

    private void fetchAddressFromServer() {

        MyAsynTaskManager  myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("getUserDefaultAddress", ShowConfirmation.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "user_id"},
                new String[]{"user", "fetchAddresses", PreferenceManager.getInstance().getUserId()});
        myAsyncTask.execute();


    }

    private void insertUserPlanOnsServer(String paid_amount) {
        String fromDate= CUtils.formatMyDate(new Date());
       // CUtils.printLog("fromDate",fromDate, ConstantVariables.LOG_TYPE.ERROR);

        MyAsynTaskManager  myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("insertUserPlan", ShowConfirmation.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "user_id","product_id","product_mapping_id","quantity","frequency_id","duration_id","subscription_date","address_id","paid_amount"},
                new String[]{"plans", "insertUserPlan", PreferenceManager.getInstance().getUserId(),product_id,product_mapping_id,product_quantity,frequency_id,duration_id,fromDate,
                CGlobal.getCGlobalObject().getAddressId(),paid_amount});
        myAsyncTask.execute();


    }
    @Override
    public void backgroundProcessFinish(String from, String output) {

        CUtils.printLog(from, output, ConstantVariables.LOG_TYPE.ERROR);
        if(from.equalsIgnoreCase("getUserDefaultAddress"))
            parseAddress(output);
        if(from.equalsIgnoreCase("insertUserPlan"))
            parseValuesAfterInsertPlanOnServer(output);

        if(from.equalsIgnoreCase("applyCouponCode"))
            parseValidCoupon(output);
    }
    private void parseValuesAfterInsertPlanOnServer(String addressList)
    {
        try {
            JSONObject jsonObject = new JSONObject(addressList);
            if(jsonObject.getBoolean("result"))
            {
              //CUtils.showUserMessage(ShowConfirmation.this, "Thanks for subscription");
                ivConfirmationScreen.setVisibility(View.VISIBLE);
                ivConfirmationScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ShowConfirmation.this, Home.class));
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                    }
                });

            }
            else
                CUtils.showUserMessage(ShowConfirmation.this,jsonObject.getString("msg"));

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }

    }

    private void parseAddress(String addressList)
    {
          try {
            JSONObject jsonObject = new JSONObject(addressList);
            if(jsonObject.getBoolean("result"))
            {

                JSONArray array=jsonObject.getJSONArray("addresses");
                if(array.length()>0) {
                    JSONObject obj;
                    for(int i=0;i<array.length();i++) {

                        obj=array.getJSONObject(i);
                        if(obj!=null) {
                            //if(obj.getString("address_id").equalsIgnoreCase(address_id))
                                if(obj.getBoolean("default_address")){
                                txtViewAddress.setText(getString(R.string.deliver_at)+obj.getString("address"));
                                break;
                            }

                        }
                    }
                } else {
                    txtViewAddress.setText(getString(R.string.deliver_at_default));
                }
            } else {
                txtViewAddress.setText(getString(R.string.deliver_at_default));
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((requestCode==ConstantVariables.SUB_ACTIVITY_EDIT_ADDRESS_FROM_CONFIRMATION)/*&& (resultCode==RESULT_OK)*/)
        {
            if(resultCode==RESULT_OK) {
                CUtils.printLog("RESULT_OK",CGlobal.getCGlobalObject().getAddressId()+"<>"+address_id, ConstantVariables.LOG_TYPE.ERROR);
                if(!CGlobal.getCGlobalObject().getAddressId().equalsIgnoreCase(address_id)) {
                    address_id = CGlobal.getCGlobalObject().getAddressId();
                    fetchAddressFromServer();
                }
            }
            //CUtils.showUserMessage(this, "Return from Address list<>" + data.getStringExtra("ADDRESS_ID"));
        }
    }

    private void initValues(String value)
    {
        try {
            JSONObject obj = new JSONObject(value);
            product_id=obj.getString("product_id");
            product_name=obj.getString("product_name");
            product_mapping_id=obj.getString("product_mapping_id");
            quantity_id=obj.getString("quantity_id");
            product_price=obj.getString("product_price");
            product_quantity=obj.getString("product_quantity");
            product_image_url=obj.getString("product_image_url");
            frequency_id=obj.getString("frequency_id");
            frequency_name=obj.getString("frequency_name");
            duration_id=obj.getString("duration_id");
            duration_name=obj.getString("duration_name");
            double daysForAWeek=Double.valueOf(obj.getString("frequency_days"));//frequency_days
            //
            double dailyPrice=Double.valueOf(product_price) * Double.valueOf(product_quantity);
            txtViewProductName.setText(product_name +" x "+product_quantity);
            txtViewProductPrice.setText("= Rs "+String.valueOf(dailyPrice));
           // double priceForAWeek=dailyPrice*daysForAWeek;

            txtViewFrequency.setText(frequency_name);

            double frequencyPrice=dailyPrice*daysForAWeek;
            txtViewFrequencyPrice.setText("= Rs "+String.valueOf(frequencyPrice));


            txtViewDuration.setText(duration_name);
           // double  paidAmount=frequencyPrice*Double.valueOf(duration);
            paidAmount=frequencyPrice* Double.valueOf(obj.getString("duration_weightage"));
            paid_amount=String.valueOf(paidAmount);

            txtViewDurationPrice.setText("= Rs "+String.valueOf(paidAmount));
            CUtils.downloadImageFromServer(ShowConfirmation.this, imageViewProduct, CUtils.getFormattedImage(product_image_url));
            ((Button)findViewById(R.id.butPayWithCash)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txtViewAddress.getText().toString().equals(getString(R.string.deliver_at_default)))
                        CUtils.showUserMessage(getApplicationContext(), "Please select your address to checkout.");
                    else
                        insertUserPlanOnsServer(paid_amount);
                }
            });
        }
        catch(Exception e)
        {

        }

    }

    public void customCouponDialogbox(Activity context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coupon_dialog);

        //TextView header = (TextView) dialog.findViewById(R.id.title);
        //header.setText(title);
        //TextView text = (TextView) dialog.findViewById(R.id.text);
        //text.setText(message);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        editCoupon = (EditText) dialog.findViewById(R.id.editCoupon);
        couponCode = editCoupon.getText().toString();
        Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);
        Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editCoupon, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editCoupon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

    public void couponCheckValid(String couponCode) {
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("applyCouponCode", ShowConfirmation.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "coupon_code", "list_price"},
                new String[]{"products_search", "applyCouponCode", couponCode, String.valueOf(paidAmount)});
        myAsyncTask.execute();
    }

    public void parseValidCoupon(String output) {
        try {
            JSONObject jsonObject = new JSONObject(output);
            if(jsonObject.getBoolean("result")) {
                jsonObject.getDouble("list_price");
                jsonObject.getDouble("discount_price");

                jsonObject.getString("coupon_id");
                jsonObject.getString("discount_type");
                jsonObject.getString("discount");

                paidAmount = jsonObject.getDouble("gross_price");

                lldiscount.setVisibility(View.VISIBLE);
                llcoupon.setVisibility(View.GONE);
                txtViewDurationPrice.setText("= Rs " + String.valueOf(paidAmount));
                txtViewDiscountPrice.setText("= Rs "+jsonObject.getDouble("discount_price"));

                ((Button)findViewById(R.id.butPayWithCash)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(txtViewAddress.getText().toString().equals(getString(R.string.deliver_at_default)))
                            CUtils.showUserMessage(getApplicationContext(), "Please select your address to checkout.");
                        else
                            insertUserPlanOnsServer(String.valueOf(paidAmount));
                    }
                });
            }
            else {
                lldiscount.setVisibility(View.GONE);
                llcoupon.setVisibility(View.VISIBLE);
                CUtils.showUserMessage(ShowConfirmation.this, jsonObject.getString("msg"));
            }

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }

    @Override
    public void onValidationSucceeded() {
        if(general.isNetworkAvailable(ShowConfirmation.this)) {
            couponCheckValid(editCoupon.getText().toString());
            dialog.dismiss();
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
}
