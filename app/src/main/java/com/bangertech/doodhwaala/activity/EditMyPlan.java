package com.bangertech.doodhwaala.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.general.General;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
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

    private String product_name,quantity,frequency_name,frequency_id,duration_id,duration_name,plan_id,imageUrl, price;
    private TextView textViewProductName;
    private ImageView imageViewProduct;
    private int selectedQuantity=1;
    private LinearLayout llEditPlan;
    private String selectedQtySpinnerValue, selectedDurationSpinnerValue, selectedFreqSpinnerValue;
    private Button tvSave;
    private Toolbar toolbar;
    private int webqty;
    private int durationPos, freqPos;
    private int qtyPos;
    private ImageView ivCancellationScreen;
    private General general;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        general = new General();
        setContentView(R.layout.activity_edit_my_plan);
        llEditPlan=(LinearLayout)findViewById(R.id.llEditPlan);

        textViewProductName=(TextView)findViewById(R.id.textViewProductName);
        imageViewProduct=(ImageView)findViewById(R.id.imageViewProduct);
        ivCancellationScreen = (ImageView) findViewById(R.id.ivCancellationScreen);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // toolbar.setPadding(0, CUtils.getStatusBarHeight(SigninActivity.this), 0, 0);
        getSupportActionBar().setTitle("View plan");

        tvSave = (Button) toolbar.findViewById(R.id.tvSave);

        plan_id=getIntent().getStringExtra("PLAN_ID");
        if(!TextUtils.isEmpty(plan_id)) {
            if (general.isNetworkAvailable(EditMyPlan.this)) {
                fetchPlanDetailsFromServer(plan_id);
            } else {
                DialogManager.showDialog(EditMyPlan.this, "Please Check your internet connection.");
            }
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
    private void saveViewPlan(String planId, String price)
    {
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("editUserPlan", EditMyPlan.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action","plan_id","quantity","frequency_id","duration_id", "price"},
                new String[]{"plans", "editUserPlan",planId,String.valueOf(qtyPos+1),String.valueOf(freqPos+1),String.valueOf(durationPos+1), price});
        myAsyncTask.execute();
    }
    private void cancelMyPlan()
    {
        // CUtils.printLog("dateId",planId, ConstantVariables.LOG_TYPE.ERROR);
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("cancelUserPlan", EditMyPlan.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "plan_id"},
                new String[]{"plans", "cancelUserPlan", plan_id});
        myAsyncTask.execute();
        //setResult(RESULT_OK);// PAUSE PLAN


    }
    public void gotoCancel(View view)
    {
        showOptionsDialog(EditMyPlan.this, "Are you sure you want to cancel the plan?");
    }
    public void gotoSaveChanges(View view)
    {
        this.finish();
    }

    private void showPlanDetail()
    {
        textViewProductName.setText(product_name);
        String summery="I want my milk delivered <u>"+frequency_name+"</u> for the next <u>"+duration_name+"</u>";
       // textViewPlanSummery.setText(Html.fromHtml(summery));
        CUtils.downloadImageFromServer(this, imageViewProduct, imageUrl);
        llEditPlan.setVisibility(View.VISIBLE);

        /*String[] Qty = getResources().getStringArray(R.array.Qty);

        String qtuNumber= "";

        qtuNumber = Qty[webqty]; //pass webservice value

        String[] separated = qtuNumber.split(" ");
        String number =separated[0]; // this will contain "Numbers"
        String packets = separated[1]; // this will contain " packets"*/

        Spinner qtySpinner = (Spinner) findViewById(R.id.spquantity);
        Spinner durationSpinner = (Spinner) findViewById(R.id.spduration);
        Spinner freqSpinner = (Spinner) findViewById(R.id.spfrequency);
        freqSpinner.setEnabled(false);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> qtyAdapter = ArrayAdapter.createFromResource(this,
                R.array.Qty, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        qtySpinner.setAdapter(qtyAdapter);
        qtyPos = webqty - 1;
        qtySpinner.setSelection(qtyPos);

        qtySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedQtySpinnerValue = parent.getItemAtPosition(position).toString();

                if(qtyPos == position) {
                    tvSave.setVisibility(View.GONE);
                }
                else {
                    qtyPos = position;
                    tvSave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(this,
                R.array.DurationName, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        durationSpinner.setAdapter(durationAdapter);

        if(duration_id.equals("1")) {
            durationPos = 0;
        }
        if(duration_id.equals("2")) {
            durationPos = 1;
        }
        if(duration_id.equals("3")) {
            durationPos = 2;
        }

        durationSpinner.setSelection(durationPos);

        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDurationSpinnerValue = parent.getItemAtPosition(position).toString();

                if(durationPos == position) {
                    tvSave.setVisibility(View.GONE);
                }
                else {
                    durationPos = position;
                    tvSave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> freqAdapter = ArrayAdapter.createFromResource(this,
                R.array.FreqName, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        freqSpinner.setAdapter(freqAdapter);

        if(frequency_id.equals("1")) {
            freqPos = 0;
        }
        if(frequency_id.equals("2")) {
            freqPos = 1;
        }
        if(frequency_id.equals("3")) {
            freqPos = 2;
        }

        freqSpinner.setSelection(freqPos);

        freqSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFreqSpinnerValue = parent.getItemAtPosition(position).toString();

                if(freqPos == position) {
                    tvSave.setVisibility(View.GONE);
                }
                else {
                    freqPos = position;
                    tvSave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void parseOutputValues(String output)
    {
        if(output!=null) {
            try {

                JSONObject jsonObject = new JSONObject(output);
                if (jsonObject.getBoolean("result")) {
                    JSONArray arrayPlan = jsonObject.getJSONArray("plan_details");//BRAND
                    if (arrayPlan.length() > 0) {
                        JSONObject jsonPlan = arrayPlan.getJSONObject(0);
                        product_name = jsonPlan.getString("product_name");
                        quantity = jsonPlan.getString("quantity");
                        frequency_name = jsonPlan.getString("frequency_name");
                        frequency_id = jsonPlan.getString("frequency_id");
                        duration_id = jsonPlan.getString("duration_id");
                        duration_name = jsonPlan.getString("duration_name");
                        selectedQuantity = Integer.parseInt(quantity);
                        webqty = Integer.parseInt(quantity);
                        imageUrl = jsonPlan.getString("image");
                        price = jsonPlan.getString("price");

                        tvSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!TextUtils.isEmpty(plan_id)) {
                                    if (general.isNetworkAvailable(EditMyPlan.this)) {
                                        saveViewPlan(plan_id, price);
                                    } else {
                                        DialogManager.showDialog(EditMyPlan.this, "Please Check your internet connection.");
                                    }
                                }
                            }
                        });

                        if (this.imageUrl.length() > 0)
                            this.imageUrl = this.imageUrl.replace("\\/", "/");
                        showPlanDetail();
                    }
                }
            } catch (Exception e) {

            }
        } else {
            DialogManager.showDialog(EditMyPlan.this, "Server Error Occurred! Try Again!");
        }
    }


    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("fetchPlanDetails"))//cancelUserPlan
        {
            parseOutputValues(output);
            CUtils.printLog("fetchPlanDetails",output, ConstantVariables.LOG_TYPE.ERROR);
        }
        if(from.equalsIgnoreCase("cancelUserPlan"))
        {
            //setResult(RESULT_OK);
            parseCancelMilk(output);

        }
        if(from.equalsIgnoreCase("editUserPlan")) {
            parseEditUserPlan(output);
        }

    }

    public void showOptionsDialog(final Activity parentActivity, String message)
    {

        final Dialog dialog = new Dialog(parentActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_dialog_option);

        TextView msgText=(TextView)dialog.findViewById(R.id.body);
        msgText.setText(message);
        Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);
        Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(general.isNetworkAvailable(EditMyPlan.this)) {
                    cancelMyPlan();
                } else {
                    DialogManager.showDialog(EditMyPlan.this, "Please Check your internet connection.");
                }

                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

   public void parseCancelMilk(String output) {
       if(output!=null) {
           try {
               JSONObject jsonObject = new JSONObject(output);
               if (jsonObject.getBoolean("result")) {
                   CUtils.printLog("cancelUserPlan-bijendra", output, ConstantVariables.LOG_TYPE.ERROR);
                   //CUtils.showUserMessage(EditMyPlan.this, "Plan Cancel Successfully");
                   ivCancellationScreen.setVisibility(View.VISIBLE);
                   ivCancellationScreen.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           startActivity(new Intent(EditMyPlan.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                           overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                       }
                   });

               } else {
                   DialogManager.showDialog(EditMyPlan.this, "Failed to cancel your plan. Try again!");
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       } else {
           DialogManager.showDialog(EditMyPlan.this, "Server Error Occurred! Try Again!");
       }
    }

    public void parseEditUserPlan(String output) {
        if(output!=null) {
            try {
                JSONObject jsonObject = new JSONObject(output);
                if (jsonObject.getBoolean("result")) {
                    CUtils.showUserMessage(EditMyPlan.this, "Plan Updated Successfully");
                    startActivity(new Intent(EditMyPlan.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                } else {
                    DialogManager.showDialog(EditMyPlan.this, "Failed to update your plan. Try again!");
                    //CUtils.showUserMessage(EditMyPlan.this, "Failed to update your plan. Try again!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DialogManager.showDialog(EditMyPlan.this, "Server Error Occurred! Try Again!");
        }
    }

}