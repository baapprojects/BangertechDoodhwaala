package com.bangertech.doodhwaala.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bangertech.doodhwaala.R;

/**
 * Created by annutech on 9/22/2015.
 */
public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
    protected TextView orderDate,orderItemNames,orderAmount;


    public OrderHistoryViewHolder(View v) {
        super(v);

        orderDate =  (TextView) v.findViewById(R.id.tvDate);
        orderItemNames =  (TextView) v.findViewById(R.id.tvItemNames);
        orderAmount =  (TextView) v.findViewById(R.id.tvAmount);
    }
}

