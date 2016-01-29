package com.bangertech.doodhwaala.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.activity.EditMyPlan;
import com.bangertech.doodhwaala.activity.Home;
import com.bangertech.doodhwaala.adapter.DayPlanAdapter;
import com.bangertech.doodhwaala.beans.BeanDayPlan;
import com.bangertech.doodhwaala.cinterfaces.IMyMilkDayPlan;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.manager.PreferenceManager;
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
import java.util.Locale;


public class MyMilkFragment extends Fragment implements IMyMilkDayPlan {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private List<BeanDayPlan> lstDayPlan=new ArrayList<BeanDayPlan>();
    public static final int CHANGE_PLAN=0;
    public static final int PAUSE_OR_RESUME_PLAN=1;
    private boolean isTomorrow=false,isPreviousDate=false,isNextDate=false;
    public static String date_string="",plan_date="";
    public static TextView textViewDate,textViewDayHeading;
    private ImageView imageViewNext,imageViewPrevious;
    int moveIndex=ConstantVariables.MY_PLAN_TOMORROW;
    private boolean showChangeOrPausePlan=true;
    private int editMyPlanIndex=-1;
    private String flagPause;
    private ImageView datePicker;
    private int year, month, day;
    private Calendar calendar;
    private ImageView myMilkPauseTutorial, myMilkQuantityTutorial, myMilkResumeTutorial;
    private ImageView myMilkPending;
    private TextView tvi_no_order;
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
        tvi_no_order = (TextView) view.findViewById(R.id.tvi_no_order);
        myMilkPending = (ImageView) view.findViewById(R.id.myMilkPending);
        myMilkPauseTutorial = (ImageView) view.findViewById(R.id.myMilkPauseTutorial);
        myMilkQuantityTutorial = (ImageView) view.findViewById(R.id.myMilkQuantityTutorial);
        myMilkResumeTutorial = (ImageView) view.findViewById(R.id.myMilkResumeTutorial);
        textViewDate= (TextView)view.findViewById(R.id.textViewDate);
        textViewDayHeading= (TextView)view.findViewById(R.id.textViewDayHeading);
        imageViewPrevious= (ImageView)view.findViewById(R.id.imageViewPrevious);
        imageViewNext= (ImageView)view.findViewById(R.id.imageViewNext);
        mRecyclerView= (RecyclerView)view.findViewById(R.id.my_recycler_view);
        datePicker = (ImageView) view.findViewById(R.id.datePicker);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new DayPlanAdapter(getActivity(), this, lstDayPlan);
        mRecyclerView.setAdapter(mAdapter);

        imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CUtils.showUserMessage(getActivity(), "Next");
                if(isTomorrow || isNextDate) {
                    moveIndex = ConstantVariables.MY_PLAN_NEXT;
                    ((Home) getActivity()).fetchPreviousOrNextDayMyPlan(plan_date, moveIndex);
                }
                //else
                    //CUtils.showUserMessage(getActivity(), "Next does not exists");

            }
        });

        imageViewPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isTomorrow || isPreviousDate) {
                    moveIndex = ConstantVariables.MY_PLAN_PREVIOUS;
                    ((Home) getActivity()).fetchPreviousOrNextDayMyPlan(plan_date, moveIndex);
                }
                //else
                    //CUtils.showUserMessage(getActivity(), "Previous does not exists");

            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        return view;
    }


    private void parseDayPlan(String output)
    {

        lstDayPlan.clear();
        BeanDayPlan beanDayPlan;
        if(output!=null) {
            try {
                JSONObject jsonObject = new JSONObject(output);
                if (jsonObject.getBoolean("result")) {
                    isTomorrow = jsonObject.getBoolean("tomorrow");
                    isPreviousDate = jsonObject.getBoolean("previous_date");
                    isNextDate = jsonObject.getBoolean("next_date");
                    date_string = jsonObject.getString("date_string");
                    plan_date = jsonObject.getString("date");
                    if(jsonObject.getBoolean("no_plan_for_date")) {
                        textViewDate.setText(date_string);
                        tvi_no_order.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        Home.pager.setCurrentItem(1);
                        showDateLabelHeading();
                    } else {
                        tvi_no_order.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayProducts = new JSONArray(jsonObject.getString("products"));

                        showChangeOrPausePlan = isShowChangeOrPausePlan();
                        myMilkPending.setVisibility(View.GONE);
                        if (!PreferenceManager.getInstance().getMyMilkTutorial()) {
                            pauseTutorial();
                        }
                        if (jsonObject.getBoolean("all_pending")) {
                            myMilkPending.setVisibility(View.VISIBLE);
                            Home.pager.setCurrentItem(1);

                        } else {


                            int size = jsonArrayProducts.length();
                            if (size > 0) {
                                JSONObject jsonObjectProduct = null;

                                for (int index = 0; index < size; index++) {
                                    jsonObjectProduct = jsonArrayProducts.getJSONObject(index);

                                    if (jsonObjectProduct != null) {
                                        beanDayPlan = new BeanDayPlan();
                                        if (jsonObjectProduct.getString("paused").equals(flagPause)) {
                                            beanDayPlan.setFlagPaused("");
                                        } else {
                                            beanDayPlan.setFlagPaused(jsonObjectProduct.getString("paused"));
                                        }
                                        beanDayPlan.setPlanId(jsonObjectProduct.getString("plan_id"));
                                        beanDayPlan.setProductName(jsonObjectProduct.getString("product_name"));
                                        beanDayPlan.setQuantity(jsonObjectProduct.getString("quantity"));
                                        beanDayPlan.setImage(jsonObjectProduct.getString("image"));
                                        beanDayPlan.setPaused(jsonObjectProduct.getBoolean("paused"));
                                        beanDayPlan.setDateId(jsonObjectProduct.getString("date_id"));
                                        beanDayPlan.setFrequencyId(jsonObjectProduct.getString("frequency_id"));
                                        beanDayPlan.setDateAvailable(jsonObjectProduct.getBoolean("date_assigned"));
                                        beanDayPlan.setShowChangeOrPausePlan(showChangeOrPausePlan);
                                        flagPause = jsonObjectProduct.getString("paused");
                                        lstDayPlan.add(beanDayPlan);
                                    }
                                }
                            }
                            Home.pager.setCurrentItem(1);
                        }
                    }

                } else {
                    Home.pager.setCurrentItem(0);
                    textViewDayHeading.setText("No Orders Placed");
                    textViewDate.setText("");
                }
                //CUtils.showUserMessage(getActivity(), jsonObject.getString("msg"));
            } catch (Exception e) {

            }
        } else {
            DialogManager.showDialog(getActivity(), "Server Error Occurred! Try Again!");
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
        if(isTomorrow) {
            textViewDayHeading.setVisibility(View.VISIBLE);
            textViewDayHeading.setText(getActivity().getResources().getString(R.string.tomorrow));

            imageViewPrevious.setVisibility(View.INVISIBLE);
            imageViewNext.setVisibility(View.VISIBLE);
        }
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

                if(todayCal.equals(calPlanDate)) {
                    textViewDayHeading.setVisibility(View.VISIBLE);
                    //textViewDayHeading.setText(""+getActivity().getResources().getString(R.string.today));
                }
                else {
                    if (todayCal.after(calPlanDate)) {
                        //textViewDayHeading.setVisibility(View.GONE);
                        textViewDayHeading.setText(""+getActivity().getResources().getString(R.string.previous_day));
                    }
                    else {
                        textViewDayHeading.setVisibility(View.GONE);
                        //textViewDayHeading.setText(""+getActivity().getResources().getString(R.string.next_day));
                        if(isNextDate){
                            imageViewPrevious.setVisibility(View.VISIBLE);
                            imageViewNext.setVisibility(View.VISIBLE);
                        } else {
                            imageViewPrevious.setVisibility(View.VISIBLE);
                            imageViewNext.setVisibility(View.INVISIBLE);
                        }
                    }
                }

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
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("PLAN_ID",beanDayPlan.getPlanId());
            startActivityForResult(intent, ConstantVariables.SUB_ACTIVITY_EDIT_MY_PLAN);
            getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
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
                        ((Home)getActivity()).updatePauseOrResumePlanOnServer(lstDayPlan.get(index), index);
                        if(!PreferenceManager.getInstance().getMyMilkResumeTutorial()) {
                            resumeTutorial();
                        }
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
        builder.setMessage(beanDayPlan.isPaused()?getResources().getString(R.string.do_you_want_to_resume_the_plan)+" from "+date_string+"?":
                getResources().getString(R.string.do_you_want_to_pause_the_plan)+" from "+date_string+"?")
        .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    /*public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }*/

    public void pauseTutorial() {
        myMilkPauseTutorial.setVisibility(View.VISIBLE);
        myMilkQuantityTutorial.setVisibility(View.GONE);

        myMilkPauseTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityTutorial();
            }
        });
    }

    public void quantityTutorial() {
        myMilkPauseTutorial.setVisibility(View.GONE);
        myMilkQuantityTutorial.setVisibility(View.VISIBLE);

        myMilkQuantityTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getInstance().setMyMilkTutorial(true);
                myMilkPauseTutorial.setVisibility(View.GONE);
                myMilkQuantityTutorial.setVisibility(View.GONE);
            }
        });
    }

    public void resumeTutorial() {
        myMilkResumeTutorial.setVisibility(View.VISIBLE);

        myMilkResumeTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getInstance().setMyMilkResumeTutorial(true);
                myMilkResumeTutorial.setVisibility(View.GONE);
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        static String[] suffixes =
                //    0     1     2     3     4     5     6     7     8     9
                { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    10    11    12    13    14    15    16    17    18    19
                        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                        //    20    21    22    23    24    25    26    27    28    29
                        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    30    31
                        "th", "st" };
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day+1);
            datePickerDialog.getDatePicker().setMinDate((System.currentTimeMillis() - 1000)+1);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            view.setMinDate(System.currentTimeMillis() - 1000);
            String set_date = String.valueOf(day)+"-"+String.valueOf(month+1)+"-"+String.valueOf(year);

            SimpleDateFormat form  = new SimpleDateFormat("dd-MM-yyyy");
            Date ver_date = null;
            String Strver = "";
            try {
                ver_date = form.parse(set_date);
                form = new SimpleDateFormat("yyyy-MM-dd");
                Strver = form.format(ver_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat formatter  = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat formatter3 = new SimpleDateFormat("dd-MM-yyyy");

            if(Strver.equals(plan_date) || Strver.compareTo(plan_date)>0)
            {
                if(!textViewDayHeading.getText().toString().equals("No Orders Placed")) {
                    Date newDate, newDate2, newDate3 = null;
                    String monthStr = "";
                    String dayStr = "";
                    String yearStr = "";
                    try {
                        newDate = formatter.parse(set_date);
                        newDate2 = formatter2.parse(set_date);
                        newDate3 = formatter3.parse(set_date);
                        //formatter = new SimpleDateFormat("MMM d yyyy");
                        formatter = new SimpleDateFormat("d");
                        formatter2 = new SimpleDateFormat("MMM");
                        formatter3 = new SimpleDateFormat("yyyy");
                        int days = Integer.parseInt(formatter.format(newDate));
                        dayStr = days + suffixes[days];
                        monthStr = formatter2.format(newDate2);
                        yearStr = formatter3.format(newDate3);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String sel_date = monthStr + " " + dayStr + " " + yearStr;
                    textViewDate.setText(sel_date);
                    if (!sel_date.equals(date_string)) {
                        textViewDayHeading.setVisibility(View.GONE);
                    } else {
                        textViewDayHeading.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getActivity(),"No Orders available",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getActivity(),"No Orders available",Toast.LENGTH_LONG).show();
            }



        }
    }


}
