package com.bangertech.doodhwaala.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.bangertech.doodhwaala.R;

/**
 * Created by annutech on 9/29/2015.
 */
public class MostSellingBrandViewHolder extends RecyclerView.ViewHolder {
    //protected TextView orderDate,orderItemNames,orderAmount;
    protected TextView BrandName,ViewAll;
    protected GridView Products;


    public MostSellingBrandViewHolder(View v) {
        super(v);

        BrandName =  (TextView) v.findViewById(R.id.textViewBrandName);
        ViewAll =  (TextView) v.findViewById(R.id.textViewAll);
        Products =  (GridView) v.findViewById(R.id.gridProducts);
    }
}
