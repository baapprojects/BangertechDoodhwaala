package com.bangertech.doodhwaala.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.activity.Home;
import com.bangertech.doodhwaala.beans.BeanBrand;
import com.bangertech.doodhwaala.beans.BeanDayPlan;
import com.bangertech.doodhwaala.cinterfaces.IMyMilkDayPlan;
import com.bangertech.doodhwaala.fragment.MyMilkFragment;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Newdream on 24-Oct-15.
 */
public class DayPlanAdapter extends RecyclerView.Adapter<DayPlanViewHolder> implements AsyncResponse {
    private List<BeanDayPlan> lstDayPlan;
    private BeanDayPlan beanDayPlan;
    private Fragment fragment;
    private IMyMilkDayPlan iMyMilkDayPlan;
    private Context context;
    private Activity activity;
    private int selectedQuantity=1;
    private int index;
    private DayPlanViewHolder gHolder;
    private String product_name,quantity,frequency_name,frequency_id,duration_id,duration_name,plan_id,imageUrl;

    public DayPlanAdapter(Activity activity, Fragment fragment,List<BeanDayPlan> lstDayPlan)
    {
        this.activity = activity;
        this.lstDayPlan=lstDayPlan;
        this.fragment=fragment;
        iMyMilkDayPlan=(IMyMilkDayPlan)this.fragment;

    }
    @Override
    public DayPlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_my_milk, parent, false);
        return  new DayPlanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DayPlanViewHolder holder, final int position) {

        if(this.lstDayPlan.size()>0) {
                      beanDayPlan = this.lstDayPlan.get(position);
            holder.txtActiveOrPaused.setVisibility(View.GONE);

            if(!lstDayPlan.get(position).getFrequencyId().equals("1")) {
                holder.ChangePlan.setEnabled(false);
            } else {
                holder.ChangePlan.setEnabled(true);
            }

            if(!TextUtils.isEmpty(lstDayPlan.get(position).getPlanId())) {
                //fetchPlanDetailsFromServer(lstDayPlan.get(position).getPlanId());
                holder.ivminus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gHolder = holder;
                        selectedQuantity = Integer.valueOf(holder.Quantity.getText().toString());
                        if (selectedQuantity > 1) {
                            selectedQuantity = selectedQuantity-1;
                            holder.ivminus.setEnabled(false);
                            holder.ivplus.setEnabled(false);
                            updateQuantityDayPlan(lstDayPlan.get(position).getDateId(), selectedQuantity);

                        }

                    }
                });
                holder.ivplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gHolder = holder;
                        selectedQuantity = Integer.valueOf(holder.Quantity.getText().toString());
                        selectedQuantity = selectedQuantity+1;
                        holder.ivminus.setEnabled(false);
                        holder.ivplus.setEnabled(false);
                        updateQuantityDayPlan(lstDayPlan.get(position).getDateId(), selectedQuantity);

                    }
                });
            }
            /*if(beanDayPlan.getFlagPaused().equals(""))
            {
                holder.txtActiveOrPaused.setVisibility(View.GONE);
            }
            else
            {
                holder.txtActiveOrPaused.setVisibility(View.VISIBLE);
            }*/
            if(beanDayPlan.isShowChangeOrPausePlan()) {
                holder.llChangeOrPausePlan.setVisibility(View.VISIBLE);
            }
            else {
                holder.llChangeOrPausePlan.setVisibility(View.INVISIBLE);
            }

            holder.ProductName.setText(beanDayPlan.getProductName());
            holder.Quantity.setText(beanDayPlan.getQuantity());
            CUtils.downloadImageFromServer(this.fragment.getActivity(), holder.image, beanDayPlan.getImage());

            if(beanDayPlan.isDateAvailable()) {
                holder.ChangePlan.setEnabled(true);
                holder.PausePlan.setEnabled(true);
                holder.rlcounter.setVisibility(View.VISIBLE);
                holder.txtActiveOrPaused.setVisibility(View.GONE);
                holder.txtPaused.setVisibility(View.VISIBLE);

                if(!beanDayPlan.isPaused()) {
                    holder.PausePlan.setText(context.getString(R.string.pause));
                    holder.ChangePlan.setText(context.getString(R.string.change_plan));
                    //holder.txtActiveOrPaused.setText(context.getString(R.string.active));
                    holder.txtPaused.setVisibility(View.GONE);
                    holder.rlcounter.setVisibility(View.VISIBLE);
                }
                else {
                    holder.PausePlan.setText(context.getString(R.string.resume));
                    holder.ChangePlan.setText(context.getString(R.string.view_plan));
                    //holder.txtActiveOrPaused.setText(context.getString(R.string.paused));
                    holder.txtPaused.setVisibility(View.VISIBLE);
                    holder.rlcounter.setVisibility(View.GONE);
                }

            } else {
                holder.txtPaused.setVisibility(View.GONE);
                holder.rlcounter.setVisibility(View.GONE);
                holder.ChangePlan.setEnabled(false);
                holder.PausePlan.setEnabled(false);
                holder.txtActiveOrPaused.setVisibility(View.VISIBLE);
            }



            holder.ChangePlan.setTag(position);
            holder.PausePlan.setTag(position);
            holder.ChangePlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = Integer.parseInt(v.getTag().toString());
                    iMyMilkDayPlan.myMilkDayPlanOperation(MyMilkFragment.CHANGE_PLAN, lstDayPlan.get(index), index);
                }
            });
            holder.PausePlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = Integer.parseInt(v.getTag().toString());
                    iMyMilkDayPlan.myMilkDayPlanOperation(MyMilkFragment.PAUSE_OR_RESUME_PLAN, lstDayPlan.get(index), index);
                }
            });

            holder.Quantity.setText(beanDayPlan.getQuantity());

        }
    }

    @Override
    public int getItemCount() {
        return lstDayPlan.size();
    }

    private void fetchPlanDetailsFromServer( String planId)
    {
        // CUtils.printLog("dateId",planId, ConstantVariables.LOG_TYPE.ERROR);
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("fetchPlanDetails", activity, AppUrlList.ACTION_URL,
                new String[]{"module", "action","plan_id"},
                new String[]{"plans", "fetchPlanDetails",planId});
        myAsyncTask.execute();

    }

    private void updateQuantityDayPlan(String dateId, int selectedQuantity) {
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("updateQuantityDayPlan", activity, AppUrlList.ACTION_URL,
                new String[]{"module", "action","date_id","quantity"},
                new String[]{"plans", "updateQuantityDayPlan",dateId,String.valueOf(selectedQuantity)});
        myAsyncTask.execute();
    }

    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("fetchPlanDetails"))//cancelUserPlan
        {
            if (output != null) {
                parseOutputValues(output);
                CUtils.printLog("fetchPlanDetails", output, ConstantVariables.LOG_TYPE.ERROR);
            } else {
                //DialogManager.showDialog(, "Server Error Occurred! Try Again!");
            }

        }

        if(from.equalsIgnoreCase("cancelUserPlan"))
        {
            /*setResult(RESULT_OK);
            this.finish();*/

        }

        if(from.equalsIgnoreCase("updateQuantityDayPlan")) {
            parseUpdateQuantityDayPlan(output);
        }
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
                    //showPlanDetail();
                }
            }
        }
        catch(Exception e )
        {

        }
    }

    private void parseUpdateQuantityDayPlan(String output) {
        try {

            JSONObject jsonObject = new JSONObject(output);

            gHolder.Quantity.setText(String.valueOf(jsonObject.getString("quantity")));
        }
        catch(Exception e )
        {

        }

        gHolder.ivplus.setEnabled(true);
        gHolder.ivminus.setEnabled(true);
    }

}
