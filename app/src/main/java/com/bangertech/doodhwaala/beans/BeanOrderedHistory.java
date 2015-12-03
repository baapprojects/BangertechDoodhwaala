package com.bangertech.doodhwaala.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 9/24/2015.
 */
public class BeanOrderedHistory {

    private String ordered_date;
    private String ordered_amount;
    private List<String> ordered_items=null;
    public BeanOrderedHistory()
    {
        ordered_items=new ArrayList<String>();

    }
    public String getOrderedDate() {
        return ordered_date;
    }

    public void setOrderedDate(String ordered_date) {
        this.ordered_date = ordered_date;
    }


    public String getOrderedAmount() {
        return ordered_amount;
    }

    public void setOrderedAmount(String ordered_amount) {
        this.ordered_amount = ordered_amount;
    }


    public List<String> getOrderedItems() {
        return ordered_items;
    }

    public void setOrderedItems(String ordered_items) {
        this.ordered_items.add(ordered_items);
    }




}
