package com.bangertech.doodhwaala.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bangertech.doodhwaala.R;

/**
 * Created by annutech on 9/22/2015.
 */
public class UserAddressViewHolder extends RecyclerView.ViewHolder {
    protected RadioButton address;


    public UserAddressViewHolder(View v) {
        super(v);

        address =  (RadioButton) v.findViewById(R.id.rbAddress);
    }
}

