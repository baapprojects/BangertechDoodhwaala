package com.bangertech.doodhwaala.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 10/12/2015.
 */
public class ShowDuration extends AppCompatActivity implements AsyncResponse {
    //private  RadioButton  rb1Week,rb2Weeks,rb4Weeks;
    String previousValue="";
    private List<BeanDuration> bucketDuration=new ArrayList<BeanDuration>();
    private RadioGroup radioGroupDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration);
      /*  rb1Week=(RadioButton)findViewById(R.id.rb1Week);
        rb2Weeks=(RadioButton)findViewById(R.id.rb2Weeks);
        rb4Weeks=(RadioButton)findViewById(R.id.rb4Weeks);*/
        radioGroupDuration=(RadioGroup)findViewById(R.id.radioGroupDuration);

        previousValue=getIntent().getStringExtra(ConstantVariables.SELECTED_USER_PLAN_KEY);
        //CUtils.printLog("Duration_SELECTED_DETAIL", getIntent().getStringExtra("SELECTED_PRODUCT"), ConstantVariables.LOG_TYPE.ERROR);
        fetchDurationsFromServer();

    }
    public void gotoContinueDuration(View view)
    {
       /* String duration="",durationName="";*/
        int size=radioGroupDuration.getChildCount();
        int selectedIndex=-1;
        for(int i=0;i<size;i++)
            if(((RadioButton)radioGroupDuration.getChildAt(i)).isChecked())
                selectedIndex=i;
        if(selectedIndex>-1) {
            try {
                JSONObject obj = new JSONObject(previousValue);
                obj.put("duration_id", bucketDuration.get(selectedIndex).getDurationId());
                obj.put("duration_name",  bucketDuration.get(selectedIndex).getDurationName());
                obj.put("duration_weightage",  bucketDuration.get(selectedIndex).getDurationWeightage());

                startActivity(new Intent(ShowDuration.this, ShowConfirmation.class).putExtra(ConstantVariables.SELECTED_USER_PLAN_KEY, obj.toString()));
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
            } catch (Exception e) {

            }
        }
        else
            CUtils.showUserMessage(this,"Please select duration");

    }
    private void fetchDurationsFromServer() {

        bucketDuration.clear();
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("getDurations", ShowDuration.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action"},
                new String[]{"user", "listDurations"});
        myAsyncTask.execute();

    }

    private void parseDuration(String addressList)
    {
        try {
            JSONObject jsonObject = new JSONObject(addressList);
            if(jsonObject.getBoolean("result"))
            {

                JSONArray array=jsonObject.getJSONArray("durations");
                BeanDuration beanDuration;
                if(array.length()>0) {
                    JSONObject obj;
                    for(int i=0;i<array.length();i++) {
                        obj=array.getJSONObject(i);
                        if(obj!=null) {
                            beanDuration=new BeanDuration();
                            beanDuration.setDurationId(obj.getString("duration_id"));
                            beanDuration.setDurationName(obj.getString("duration_name"));
                            beanDuration.setDurationWeightage(obj.getString("weightage"));
                            bucketDuration.add(beanDuration);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
        if(bucketDuration.size()>0)
            showDurationRadioButton();

    }
    private void showDurationRadioButton() {
        try {
            radioGroupDuration.removeAllViews();
        }
        catch(Exception e)
        {

        }
        float density = getResources().getDisplayMetrics().density;
        RadioGroup.LayoutParams rprms;
        rprms= new RadioGroup.LayoutParams((int)(216*density),
                (int)(50*density));
        int size=bucketDuration.size();
        for(int index=0;index<size;index++)
        {

            RadioButton radioButton = new RadioButton(this);
            radioButton.setTextAppearance(this,android.R.style.TextAppearance_Medium);
            radioButton.setPadding(10, 10, 10, 10);
            radioButton.setText(bucketDuration.get(index).getDurationName());
            radioButton.setTag(index);

            radioGroupDuration.addView(radioButton,index,rprms);

        }

    }


    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("getDurations"))
        {
            parseDuration(output);
            //CUtils.printLog("FREQUENCY",output, ConstantVariables.LOG_TYPE.ERROR);
        }

    }

    class BeanDuration
    {
        private String duration_id;
        private String duration_name="0";
        private String duration_weightage="0";
        public String getDurationId() {
            return duration_id;
        }

        public void setDurationId(String duration_id) {
            this.duration_id = duration_id;
        }



        public String getDurationName() {
            return duration_name;
        }

        public void setDurationName(String frequency_name) {
            this.duration_name = frequency_name;
        }



        public String getDurationWeightage() {
            return duration_weightage;
        }

        public void setDurationWeightage(String duration_weightage) {
            this.duration_weightage = duration_weightage;
        }




    }


}