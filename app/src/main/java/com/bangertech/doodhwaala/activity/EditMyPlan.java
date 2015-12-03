package com.bangertech.doodhwaala.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CGlobal;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 9/23/2015.
 */
public class EditMyPlan extends AppCompatActivity  implements AsyncResponse{
    private Toolbar app_bar;

    private String product_name,quantity,frequency_name,frequency_id,duration_id,duration_name,plan_id,imageUrl;
    private TextView textViewProductName,textViewPlanSummery,txtViewSelectedQuantity;
    private ImageView imageViewProduct;
    private int selectedQuantity=1;
    private LinearLayout llEditPlan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_plan);
        llEditPlan=(LinearLayout)findViewById(R.id.llEditPlan);

        textViewProductName=(TextView)findViewById(R.id.textViewProductName);
        textViewPlanSummery=(TextView)findViewById(R.id.textViewPlanSummery);
        txtViewSelectedQuantity=(TextView)findViewById(R.id.txtViewSelectedQuantity);
        imageViewProduct=(ImageView)findViewById(R.id.imageViewProduct);


        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.edit_plan));
       plan_id=getIntent().getStringExtra("PLAN_ID");
        if(!TextUtils.isEmpty(plan_id)) {
            fetchPlanDetailsFromServer(plan_id);
            ((Button) findViewById(R.id.butDecreaseQty)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedQuantity>1) {
                        --selectedQuantity;
                        txtViewSelectedQuantity.setText(String.valueOf(selectedQuantity));
                    }

                }
            });
            ((Button) findViewById(R.id.butIncreaseQty)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ++selectedQuantity;
                    txtViewSelectedQuantity.setText(String.valueOf(selectedQuantity));
                }
            });
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    private void fetchPlanDetailsFromServer( String planId)
    {
       // CUtils.printLog("dateId",planId, ConstantVariables.LOG_TYPE.ERROR);
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("fetchPlanDetails", EditMyPlan.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action","plan_id"},
                new String[]{"plans", "fetchPlanDetails",planId});
        myAsyncTask.execute();

    }
    private void cancelMyPlan()
    {
        // CUtils.printLog("dateId",planId, ConstantVariables.LOG_TYPE.ERROR);
        /*MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("cancelUserPlan", EditMyPlan.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action","plan_id"},
                new String[]{"plans", "cancelUserPlan",plan_id});
        myAsyncTask.execute();*/
        setResult(RESULT_OK);// PAUSE PLAN
        this.finish();

    }
    public void gotoCancel(View view)
    {
        cancelMyPlan();
    }
    public void gotoSaveChanges(View view)
    {
        this.finish();
    }

    private void showPlanDetail()
    {
        textViewProductName.setText(product_name);
        String summery="I want my milk delivered <u>"+frequency_name+"</u> for the next <u>"+duration_name+"</u>";
        textViewPlanSummery.setText(Html.fromHtml(summery));
        txtViewSelectedQuantity.setText(quantity);
        CUtils.downloadImageFromServer(this, imageViewProduct, imageUrl);
        llEditPlan.setVisibility(View.VISIBLE);
    }
private void parseOutputValues(String output)
{

    try {

            JSONObject jsonObject = new JSONObject(output);
            if(jsonObject.getBoolean("result")) {
                JSONArray arrayPlan=jsonObject.getJSONArray("plan_details");//BRAND
                if(arrayPlan.length()>0) {
                    JSONObject jsonPlan=arrayPlan.getJSONObject(0);
                    product_name = jsonPlan.getString("product_name");
                    quantity = jsonPlan.getString("quantity");
                    frequency_name = jsonPlan.getString("frequency_name");
                    frequency_id = jsonPlan.getString("frequency_id");
                    duration_id = jsonPlan.getString("duration_id");
                    duration_name = jsonPlan.getString("duration_name");
                    selectedQuantity=Integer.parseInt(quantity);
                    imageUrl = jsonPlan.getString("image");
                    if (this.imageUrl.length() > 0)
                        this.imageUrl = this.imageUrl.replace("\\/", "/");
                    showPlanDetail();
                }
            }
        }
    catch(Exception e )
    {

    }
}


    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("fetchPlanDetails"))//cancelUserPlan
        {
            parseOutputValues(output);
            CUtils.printLog("fetchPlanDetails",output, ConstantVariables.LOG_TYPE.ERROR);
        }
        else
        if(from.equalsIgnoreCase("cancelUserPlan"))
        {
            setResult(RESULT_OK);
            this.finish();
           /* CUtils.printLog("cancelUserPlan-bijendra",output, ConstantVariables.LOG_TYPE.ERROR);
            try {
                JSONObject jsonObject = new JSONObject(output);
                if(jsonObject.getBoolean("result")) {
                     CUtils.showUserMessage(EditMyPlan.this, "Plan Cancel Successfully");
                    this.finish();
                }
            }
            catch (Exception e)
            {

            }*/

        }

    }
}
