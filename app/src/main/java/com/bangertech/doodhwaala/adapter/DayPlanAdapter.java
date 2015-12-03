package com.bangertech.doodhwaala.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.beans.BeanBrand;
import com.bangertech.doodhwaala.beans.BeanDayPlan;
import com.bangertech.doodhwaala.cinterfaces.IMyMilkDayPlan;
import com.bangertech.doodhwaala.fragment.MyMilkFragment;
import com.bangertech.doodhwaala.utils.CUtils;

import java.util.List;

/**
 * Created by Newdream on 24-Oct-15.
 */
public class DayPlanAdapter extends RecyclerView.Adapter<DayPlanViewHolder>{
    private List<BeanDayPlan> lstDayPlan;
    private BeanDayPlan beanDayPlan;
    private Fragment fragment;
    private IMyMilkDayPlan iMyMilkDayPlan;
    private Context context;

    public DayPlanAdapter(Fragment fragment,List<BeanDayPlan> lstDayPlan)
    {

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
    public void onBindViewHolder(DayPlanViewHolder holder, int position) {
        if(this.lstDayPlan.size()>0) {
           /* if(this.showChangeOrPausePlan)
                holder.llChangeOrPausePlan.setVisibility(View.VISIBLE);
            else
                holder.llChangeOrPausePlan.setVisibility(View.INVISIBLE);
*/            beanDayPlan = this.lstDayPlan.get(position);
            if(beanDayPlan.isShowChangeOrPausePlan())
                holder.llChangeOrPausePlan.setVisibility(View.VISIBLE);
            else
                holder.llChangeOrPausePlan.setVisibility(View.INVISIBLE);

            holder.ProductName.setText(beanDayPlan.getProductName());
            holder.Quantity.setText(beanDayPlan.getQuantity());
            CUtils.downloadImageFromServer(this.fragment.getActivity(), holder.image, beanDayPlan.getImage());
            if(!beanDayPlan.isPaused())
                holder.PausePlan.setText(context.getString(R.string.pause));
            else
                holder.PausePlan.setText(context.getString(R.string.resume));
            holder.ChangePlan.setTag(position);
            holder.PausePlan.setTag(position);

            holder.ChangePlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index=Integer.parseInt(v.getTag().toString());
                    iMyMilkDayPlan.myMilkDayPlanOperation(MyMilkFragment.CHANGE_PLAN, lstDayPlan.get(index),index);
                }
            });
            holder.PausePlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index=Integer.parseInt(v.getTag().toString());
                    iMyMilkDayPlan.myMilkDayPlanOperation(MyMilkFragment.PAUSE_OR_RESUME_PLAN,lstDayPlan.get(index),index);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return lstDayPlan.size();
    }
}
