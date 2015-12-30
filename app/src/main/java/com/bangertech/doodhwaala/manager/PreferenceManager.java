package com.bangertech.doodhwaala.manager;

import android.content.Context;

import com.bangertech.doodhwaala.application.DoodhwaalaApplication;
import com.bangertech.doodhwaala.application.DoodhwaalaApplication;
import com.bangertech.doodhwaala.utils.AppConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sarvesh on 9/11/2015.
 */
public class PreferenceManager {

    static PreferenceManager preferenceInstance;
    public static void initInstance(){
        if(preferenceInstance == null)
            preferenceInstance = new PreferenceManager(DoodhwaalaApplication.mContext);
    }

    private PreferenceManager(Context context){
        super();
    }

    public static PreferenceManager getInstance() {
        return preferenceInstance;
    }

    public String getUserId()
    {
        return DoodhwaalaApplication.getSharedPreferences().getString(AppConstants.USER_ID, null);
    }

    public void setUserId(String UserId){
        DoodhwaalaApplication.getSharedPreferences().edit().putString(AppConstants.USER_ID, UserId).commit();
    }

    public void setUserEmailId(String emailId){
        DoodhwaalaApplication.getSharedPreferences().edit().putString(AppConstants.USER_EMAILID, emailId).commit();
    }

    public String getUserEmailId(){
        return DoodhwaalaApplication.getSharedPreferences().getString(AppConstants.USER_EMAILID, null);
    }

    public void setUserDetails(String userId, String userEmailId)
    {
        DoodhwaalaApplication.getSharedPreferences().edit().putString(AppConstants.USER_ID, userId).commit();
        DoodhwaalaApplication.getSharedPreferences().edit().putString(AppConstants.USER_EMAILID, userEmailId).commit();
    }

    public void resetUserDetails(){
        DoodhwaalaApplication.getSharedPreferences().edit().putString(AppConstants.USER_ID, null).commit();
        DoodhwaalaApplication.getSharedPreferences().edit().putString(AppConstants.USER_EMAILID, null).commit();
    }

    /*public List<String> getFilterList()
    {
        return DoodhwaalaApplication.getSharedPreferences().getStringSet(AppConstants.USER_FILTER, null);
    }
    public void setFilterListItem(int listType,String outPut)
    {
        Set<String> filterList=new HashSet<String>();
        filterList.addAll(outPut);
        DoodhwaalaApplication.getSharedPreferences().edit().putStringSet(AppConstants.USER_FILTER, filterList).commit();
    }*/
}
