package com.bangertech.doodhwaala.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangertech.doodhwaala.R;

/**
 * Created by annutech on 9/29/2015.
 */
public class DayPlanViewHolder extends RecyclerView.ViewHolder {
    //protected TextView orderDate,orderItemNames,orderAmount;
    protected LinearLayout RowMyMilk;//llRowMyMilk
    protected RelativeLayout llChangeOrPausePlan,rlcounter;
    protected TextView ProductName,Quantity,ChangePlan,PausePlan,txtActiveOrPaused,txtPaused;
    protected ImageView image, ivminus, ivplus;


    public DayPlanViewHolder(View v) {
        super(v);
        RowMyMilk =  (LinearLayout) v.findViewById(R.id.llRowMyMilk);
        llChangeOrPausePlan =  (RelativeLayout) v.findViewById(R.id.llChangeOrPausePlan);
        rlcounter = (RelativeLayout) v.findViewById(R.id.rlcounter);
        ProductName =  (TextView) v.findViewById(R.id.textViewProductName);
        Quantity =  (TextView) v.findViewById(R.id.textViewQuantity);
        ChangePlan =  (TextView) v.findViewById(R.id.textViewChangePlan);
        PausePlan =  (TextView) v.findViewById(R.id.textViewPausePlan);
        image =  (ImageView) v.findViewById(R.id.imageViewProduct);
        txtActiveOrPaused = (TextView) v.findViewById(R.id.txtActiveOrPaused);
        txtPaused = (TextView) v.findViewById(R.id.txtPaused);
        ivminus = (ImageView) v.findViewById(R.id.ivminus);
        ivplus = (ImageView) v.findViewById(R.id.ivplus);
    }
}
