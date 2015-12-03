package com.bangertech.doodhwaala.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.activity.EditMyPlan;
import com.bangertech.doodhwaala.activity.Home;
import com.bangertech.doodhwaala.adapter.DayPlanAdapter;
import com.bangertech.doodhwaala.beans.BeanDayPlan;
import com.bangertech.doodhwaala.cinterfaces.IMyMilkDayPlan;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CGlobal;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyMilkFragment extends Fragment implements IMyMilkDayPlan {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private List<BeanDayPlan> lstDayPlan=new ArrayList<BeanDayPlan>();
    public static final int CHANGE_PLAN=0;
    public static final int PAUSE_OR_RESUME_PLAN=1;
    private boolean isTomorrow=false,isPreviousDate=false,isNextDate=false;
    String date_string="",plan_date="";
    private TextView textViewDate,textViewDayHeading;
    private ImageView imageViewNext,imageViewPrevious;
    int moveIndex=ConstantVariables.MY_PLAN_TOMORROW;
    private boolean showChangeOrPausePlan=true;
    private int editMyPlanIndex=-1;
    public static MyMilkFragment newInstance() {
        return new MyMilkFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       // fetchAddressDetailFromServer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*super.onActivityResult(requestCode, resultCode, data);
        CUtils.showUserMessage(getActivity(),"Bijendra");*/
        //CUtils.showUserMessage(getActivity(), "Bijendra-1");
        if(requestCode==ConstantVariables.SUB_ACTIVITY_EDIT_MY_PLAN)
        {
           // CUtils.showUserMessage(getActivity(),"Bijendra-2");
            if (resultCode==Activity.RESULT_OK) {
                int ind=editMyPlanIndex;
                ((Home)getActivity()).updatePauseOrResumePlanOnServer(lstDayPlan.get(ind), ind);
                editMyPlanIndex=-1;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_my_milk, container, false);
        textViewDate= (TextView)view.findViewById(R.id.textViewDate);
        textViewDayHeading= (TextView)view.findViewById(R.id.textViewDayHeading);
        imageViewPrevious= (ImageView)view.findViewById(R.id.imageViewPrevious);
        imageViewNext= (ImageView)view.findViewById(R.id.imageViewNext);
        mRecyclerView= (RecyclerView)view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new DayPlanAdapter(this,lstDayPlan);
        mRecyclerView.setAdapter(mAdapter);
        imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CUtils.showUserMessage(getActivity(), "Next");
                if(isTomorrow || isNextDate) {
                    moveIndex = ConstantVariables.MY_PLAN_NEXT;
                    ((Home) getActivity()).fetchPreviousOrNextDayMyPlan(plan_date, moveIndex);
                }
                else
                    CUtils.showUserMessage(getActivity(), "Next does not exists");

            }
        });
        imageViewPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isTomorrow || isPreviousDate) {
                    moveIndex = ConstantVariables.MY_PLAN_PREVIOUS;
                    ((Home) getActivity()).fetchPreviousOrNextDayMyPlan(plan_date, moveIndex);
                }
                else
                    CUtils.showUserMessage(getActivity(), "Previous does not exists");

            }
        });

        return view;

    }


    private void parseDayPlan(String output)
    {

        lstDayPlan.clear();
        BeanDayPlan beanDayPlan;
        try {
            JSONObject jsonObject = new JSONObject(output);
            if (jsonObject.getBoolean("result")) {
                JSONArray jsonArrayProducts=new JSONArray(jsonObject.getString("products"));
                isTomorrow=jsonObject.getBoolean("tomorrow");
                isPreviousDate=jsonObject.getBoolean("previous_date");
                isNextDate=jsonObject.getBoolean("previous_date");
                date_string=jsonObject.getString("date_string");
                plan_date=jsonObject.getString("date");
                showChangeOrPausePlan=isShowChangeOrPausePlan();
                int size=jsonArrayProducts.length();
                if(size>0)
                {
                    JSONObject jsonObjectProduct=null;
                    for(int index=0;index<size;index++)
                    {
                        jsonObjectProduct=jsonArrayProducts.getJSONObject(index);
                        if(jsonObjectProduct!=null)
                        {
                            beanDayPlan=new BeanDayPlan();
                            beanDayPlan.setPlanId(jsonObjectProduct.getString("plan_id"));
                            beanDayPlan.setProductName(jsonObjectProduct.getString("product_name"));
                            beanDayPlan.setQuantity(jsonObjectProduct.getString("quantity"));
                            beanDayPlan.setImage(jsonObjectProduct.getString("image"));
                            beanDayPlan.setPaused(jsonObjectProduct.getBoolean("paused"));
                            beanDayPlan.setDateId(jsonObjectProduct.getString("date_id"));
                            beanDayPlan.setShowChangeOrPausePlan(showChangeOrPausePlan);
                            lstDayPlan.add(beanDayPlan);
                        }
                    }
                }
            }
            else
                CUtils.showUserMessage(getActivity(),jsonObject.getString("msg"));
        }
        catch (Exception e)
        {

        }
     /*   if(lstDayPlan.size()>0) {
            textViewDate.setText(date_string);
            showDateLabelHeading();

            mAdapter.notifyDataSetChanged();
        }*/
        if(lstDayPlan.size()>0) {
            textViewDate.setText(date_string);
            showDateLabelHeading();

            mAdapter.notifyDataSetChanged();
        }

    }

    boolean isShowChangeOrPausePlan()
    {

        if(isTomorrow)
            return true;
        else
        {
            try
            {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calPlanDate=Calendar.getInstance();
                calPlanDate.setTime(format.parse(plan_date));

                Calendar todayCal=Calendar.getInstance();
                Date date=new Date();
                todayCal.setTime(format.parse(format.format(date)));
                if(todayCal.before(calPlanDate))
                    return true;

            } catch (ParseException e) {
                // TODO Auto-generated catch block

            }
        }
        return false;

    }
    public void reDrawFragment(String output)
    {
        parseDayPlan(output);

    }
    private void showDateLabelHeading()
    {
        if(isTomorrow)
            textViewDayHeading.setText(getActivity().getResources().getString(R.string.tomorrow));
        else
        {
            try
            {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calPlanDate=Calendar.getInstance();
                calPlanDate.setTime(format.parse(plan_date));

                Calendar todayCal=Calendar.getInstance();
                Date date=new Date();
                todayCal.setTime(format.parse(format.format(date)));
                if(todayCal.equals(calPlanDate))
                    textViewDayHeading.setText(getActivity().getResources().getString(R.string.today));
                else
                    if(todayCal.after(calPlanDate))
                        textViewDayHeading.setText(getActivity().getResources().getString(R.string.previous_day));
                else
                        textViewDayHeading.setText(getActivity().getResources().getString(R.string.next_day));

            } catch (ParseException e) {
                // TODO Auto-generated catch block

            }
        }

    }

    @Override
    public void myMilkDayPlanOperation(int opType, BeanDayPlan beanDayPlan,int index) {
        if(opType==PAUSE_OR_RESUME_PLAN)
            alertToPauseOrResumeMyPlan(beanDayPlan,index);
        //CUtils.showUserMessage(getActivity(),"PAUSE PLAN");
        if(opType==CHANGE_PLAN)
        {
            editMyPlanIndex=index;
            Intent intent=new Intent(getActivity(),EditMyPlan.class);
            intent.putExtra("PLAN_ID",beanDayPlan.getPlanId());
            startActivityForResult(intent,ConstantVariables.SUB_ACTIVITY_EDIT_MY_PLAN);
        }

    }
    public void updatePauseOrResumePlanInList(int index)
    {
        lstDayPlan.get(index).setPaused(!lstDayPlan.get(index).isPaused());
        mAdapter.notifyDataSetChanged();
    }
    public void alertToPauseOrResumeMyPlan(BeanDayPlan beanDayPlan,final int index) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // saveChangedDefaultAddressOnServer();
                        ((Home)getActivity()).updatePauseOrResumePlanOnServer(lstDayPlan.get(index),index);
                       // setPauseOrResumePlan(index);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                       /* Toast.makeText(ShowAddress.this, "No Clicked",
                                Toast.LENGTH_LONG).show();
                        ShowAddress.this.finish();*/
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(beanDayPlan.isPaused()?getResources().getString(R.string.do_you_want_to_resume_the_plan):
                getResources().getString(R.string.do_you_want_to_pause_the_plan))
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}
