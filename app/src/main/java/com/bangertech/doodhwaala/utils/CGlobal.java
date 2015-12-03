package com.bangertech.doodhwaala.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 10/1/2015.
 */
public class CGlobal {
    private static CGlobal cGlobal;

    List<String> filterList=new ArrayList<String>();
    private String userId="1";
    private String addressId="1";
    private CGlobal()
    {
        filterList.clear();
    }
    public static CGlobal getCGlobalObject(){
        if(cGlobal==null)
            cGlobal=new CGlobal();

        return cGlobal;
    }

    public List<String> getFilterList()
    {
        return filterList;
    }
    public void setFilterListItem(int listType,String outPut)
    {
        filterList.add(listType,outPut);
    }
    public String getFilterListItem(int listType)
    {
        try {
            return filterList.get(listType);
        }
        catch(Exception e)
        {

        }
        return null;
    }

    public void setAddressId(String addressId)
    {
        this.addressId=addressId;
    }
    public String getAddressId()
    {
        return this.addressId;
    }
    public void setUserId(String uId)
    {
        this.userId=uId;
    }
    public String getUserId()
    {
        return this.userId;
    }
}
