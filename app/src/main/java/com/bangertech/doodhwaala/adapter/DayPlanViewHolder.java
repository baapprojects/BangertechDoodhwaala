package com.bangertech.doodhwaala.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangertech.doodhwaala.R;

/**
 * Created by annutech on 9/29/2015.
 */
public class DayPlanViewHolder extends RecyclerView.ViewHolder {
    //protected TextView orderDate,orderItemNames,orderAmount;
    protected LinearLayout RowMyMilk,llChangeOrPausePlan;//llRowMyMilk
    protected TextView ProductName,Quantity,ChangePlan,PausePlan;
    protected ImageView image;


    public DayPlanViewHolder(View v) {
        super(v);
        RowMyMilk =  (LinearLayout) v.findViewById(R.id.llRowMyMilk);
        llChangeOrPausePlan =  (LinearLayout) v.findViewById(R.id.llChangeOrPausePlan);
        ProductName =  (TextView) v.findViewById(R.id.textViewProductName);
        Quantity =  (TextView) v.findViewById(R.id.textViewQuantity);
        ChangePlan =  (TextView) v.findViewById(R.id.textViewChangePlan);
        PausePlan =  (TextView) v.findViewById(R.id.textViewPausePlan);
        image =  (ImageView) v.findViewById(R.id.imageViewProduct);

    }
}
