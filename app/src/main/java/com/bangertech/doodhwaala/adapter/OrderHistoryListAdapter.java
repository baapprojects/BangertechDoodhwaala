package com.bangertech.doodhwaala.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bangertech.doodhwaala.beans.BeanOrderedHistory;
import com.bangertech.doodhwaala.cinterfaces.IUserAddressList;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import java.util.List;

/**
 * Created by annutech on 9/22/2015.
 */
public class OrderHistoryListAdapter extends RecyclerView.Adapter<OrderHistoryViewHolder> {

 private Activity activity;
 private  List<BeanOrderedHistory> _orderedHistory;


public OrderHistoryListAdapter(Activity activity, List<BeanOrderedHistory> orderedHistory) {
        this._orderedHistory = orderedHistory;
        this.activity = activity;


 }



@Override
public int getItemCount() {
        return _orderedHistory.size();
        }

@Override
public void onBindViewHolder(OrderHistoryViewHolder contactViewHolder, int i) {


    BeanOrderedHistory beanOrderedHistory=_orderedHistory.get(i);
    contactViewHolder.orderDate.setText(beanOrderedHistory.getOrderedDate());
    contactViewHolder.orderAmount.setText(ConstantVariables.RUPEE_SIGN+beanOrderedHistory.getOrderedAmount());
    List<String> items=beanOrderedHistory.getOrderedItems();
    StringBuilder sb=new StringBuilder();
    for(String itemName:items)
        sb.append(itemName+System.getProperty("line.separator"));

    contactViewHolder.orderItemNames.setText(sb);


        }


@Override
public OrderHistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.row_order_history, viewGroup, false);



        return new OrderHistoryViewHolder(itemView);
        }


}
