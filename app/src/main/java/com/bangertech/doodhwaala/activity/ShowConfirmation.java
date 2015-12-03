package com.bangertech.doodhwaala.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangertech.doodhwaala.beans.BeanAddress;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CGlobal;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by annutech on 10/12/2015.
 */
public class ShowConfirmation extends AppCompatActivity implements AsyncResponse {
    private TextView txtViewProductName,txtViewProductPrice,txtViewFrequency,
            txtViewFrequencyPrice,txtViewDuration, txtViewDurationPrice,txtViewAddress;
    private ImageView imageViewProduct;
    private String   product_id,product_mapping_id,quantity_id,from_date,to_date,address_id="0",paid_amount,status;
    private String product_price,product_image_url,frequency_id,frequency_name,duration_id,duration_name,product_quantity,product_name,quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        imageViewProduct=(ImageView)findViewById(R.id.imageViewProduct);
        txtViewProductName=(TextView)findViewById(R.id.txtViewProductName);
        txtViewProductPrice=(TextView)findViewById(R.id.txtViewProductPrice);
        txtViewFrequency=(TextView)findViewById(R.id.txtViewFrequency);
        txtViewFrequencyPrice=(TextView)findViewById(R.id.txtViewFrequencyPrice);
        txtViewDuration=(TextView)findViewById(R.id.txtViewDuration);
        txtViewDurationPrice=(TextView)findViewById(R.id.txtViewDurationPrice);
        txtViewAddress=(TextView)findViewById(R.id.txtViewAddress);

        ((TextView)findViewById(R.id.txtViewEditAddress)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putBoolean("IS_FROM_CONFIRMATION_SCREEN", true);
                startActivityForResult(new Intent(ShowConfirmation.this, ShowAddress.class).putExtras(bundle), ConstantVariables.SUB_ACTIVITY_EDIT_ADDRESS_FROM_CONFIRMATION);

            }
        });
        ((Button)findViewById(R.id.butPayWithCash)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUserPlanOnsServer();
            }
        });
        initValues(getIntent().getStringExtra(ConstantVariables.SELECTED_USER_PLAN_KEY));
    }
    private void fetchAddressFromServer() {

        MyAsynTaskManager  myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("getUserDefaultAddress", ShowConfirmation.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "user_id"},
                new String[]{"user", "fetchAddresses", CGlobal.getCGlobalObject().getUserId()});
        myAsyncTask.execute();


    }

    private void insertUserPlanOnsServer() {
        String fromDate= CUtils.formatMyDate(new Date());
       // CUtils.printLog("fromDate",fromDate, ConstantVariables.LOG_TYPE.ERROR);

        MyAsynTaskManager  myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("insertUserPlan", ShowConfirmation.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "user_id","product_id","product_mapping_id","quantity","frequency_id","duration_id","subscription_date","address_id","paid_amount"},
                new String[]{"plans", "insertUserPlan", CGlobal.getCGlobalObject().getUserId(),product_id,product_mapping_id,product_quantity,frequency_id,duration_id,fromDate,
                CGlobal.getCGlobalObject().getAddressId(),paid_amount});
        myAsyncTask.execute();


    }
    @Override
    public void backgroundProcessFinish(String from, String output) {

        CUtils.printLog(from,output, ConstantVariables.LOG_TYPE.ERROR);
        if(from.equalsIgnoreCase("getUserDefaultAddress"))
            parseAddress(output);
        if(from.equalsIgnoreCase("insertUserPlan"))
            parseValuesAfterInsertPlanOnServer(output);
    }
    private void parseValuesAfterInsertPlanOnServer(String addressList)
    {
        try {
            JSONObject jsonObject = new JSONObject(addressList);
            if(jsonObject.getBoolean("result"))
            {
              CUtils.showUserMessage(ShowConfirmation.this,"Thanks for subscription");
              this.finish();
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
                            if(obj.getString("address_id").equalsIgnoreCase(address_id)) {
                                txtViewAddress.setText(getString(R.string.deliver_at)+obj.getString("address"));
                                break;
                            }

                        }
                    }
                }
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
            txtViewProductPrice.setText("= $ "+String.valueOf(dailyPrice));
           // double priceForAWeek=dailyPrice*daysForAWeek;

            txtViewFrequency.setText(frequency_name);

            double frequencyPrice=dailyPrice*daysForAWeek;;
            txtViewFrequencyPrice.setText("= $ "+String.valueOf(frequencyPrice));


            txtViewDuration.setText(duration_name);
           // double  paidAmount=frequencyPrice*Double.valueOf(duration);
            double  paidAmount=frequencyPrice* Double.valueOf(obj.getString("duration_weightage"));
            paid_amount=String.valueOf(paidAmount);

            txtViewDurationPrice.setText("= $ "+String.valueOf(paidAmount));
            CUtils.downloadImageFromServer(ShowConfirmation.this,imageViewProduct,CUtils.getFormattedImage(product_image_url));

        }
        catch(Exception e)
        {

        }

    }
}
