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

    public boolean getMilkBarTutorial() {
        return DoodhwaalaApplication.getSharedPreferences().getBoolean(AppConstants.TUT_MILKBAR, false);
    }

    public void setMilkBarTutorial(boolean flag){
        DoodhwaalaApplication.getSharedPreferences().edit().putBoolean(AppConstants.TUT_MILKBAR, flag).commit();
    }

    public boolean getMyMilkTutorial() {
        return DoodhwaalaApplication.getSharedPreferences().getBoolean(AppConstants.TUT_MYMILK, false);
    }

    public void setMyMilkTutorial(boolean flag){
        DoodhwaalaApplication.getSharedPreferences().edit().putBoolean(AppConstants.TUT_MYMILK, flag).commit();
    }

    public boolean getMyMilkResumeTutorial() {
        return DoodhwaalaApplication.getSharedPreferences().getBoolean(AppConstants.TUT_RESUME_MYMILK, false);
    }

    public void setMyMilkResumeTutorial(boolean flag){
        DoodhwaalaApplication.getSharedPreferences().edit().putBoolean(AppConstants.TUT_RESUME_MYMILK, flag).commit();
    }

    public int getProductFilterPosition() {
        return DoodhwaalaApplication.getSharedPreferences().getInt(AppConstants.PRODUCT_FILTER_POSITION, -1);
    }

    public void setProductFilterPosition(int position){
        DoodhwaalaApplication.getSharedPreferences().edit().putInt(AppConstants.PRODUCT_FILTER_POSITION, position).commit();
    }

    public int getBrandFilterPosition() {
        return DoodhwaalaApplication.getSharedPreferences().getInt(AppConstants.BRAND_FILTER_POSITION, -1);
    }

    public void setBrandFilterPosition(int position){
        DoodhwaalaApplication.getSharedPreferences().edit().putInt(AppConstants.BRAND_FILTER_POSITION, position).commit();
    }

    public int getPackagingFilterPosition() {
        return DoodhwaalaApplication.getSharedPreferences().getInt(AppConstants.PACKAGE_FILTER_POSITION, -1);
    }

    public void setPackagingFilterPosition(int position){
        DoodhwaalaApplication.getSharedPreferences().edit().putInt(AppConstants.PACKAGE_FILTER_POSITION, position).commit();
    }

    public void resetFilterPositions(){
        DoodhwaalaApplication.getSharedPreferences().edit().putInt(AppConstants.PRODUCT_FILTER_POSITION, -1).commit();
        DoodhwaalaApplication.getSharedPreferences().edit().putInt(AppConstants.BRAND_FILTER_POSITION, -1).commit();
        DoodhwaalaApplication.getSharedPreferences().edit().putInt(AppConstants.PACKAGE_FILTER_POSITION, -1).commit();
    }

}
