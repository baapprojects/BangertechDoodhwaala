package com.bangertech.doodhwaala.adapter;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MenuItem;

import com.bangertech.doodhwaala.cinterfaces.IHome;
import com.bangertech.doodhwaala.fragment.MeFragment;
import com.bangertech.doodhwaala.fragment.MilkbarFragment;
import com.bangertech.doodhwaala.fragment.MyMilkFragment;

/**
 * Created by annutech on 9/22/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb,Activity activity) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;


    }



    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

       /* if(position == 0) // if the position is 0 we are returning the First tab
        {
            Tab1 tab1 = new Tab1();
            return tab1;
        }
        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            Tab2 tab2 = new Tab2();
            return tab2;
        }*/
        if(position == 0) {

            return new MilkbarFragment();
        }
        if(position == 1) {
           // if(this.menuItemSearch!=null)
            //this.menuItemSearch.setVisible(false);

            return new MyMilkFragment();
        }

        if(position == 2) {
            /*if(this.menuItemSearch!=null)
             this.menuItemSearch.setVisible(false);*/

            return new MeFragment();
        }

        return null;


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}