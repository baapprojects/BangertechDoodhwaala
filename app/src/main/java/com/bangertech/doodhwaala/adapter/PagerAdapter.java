package com.bangertech.doodhwaala.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bangertech.doodhwaala.R;

/**
 * Created by annutech on 31/12/2015.
 */
public class PagerAdapter extends android.support.v4.view.PagerAdapter {

    private int[] arrayofData;
    private Context mContext;

    /**
     * ViewPager with the swipable pages for the OnBoarding page.
     *
     * @param context          the context from which the pager is initiated
     * @param arrayOfResources the pages
     */
    public PagerAdapter(Context context, int[] arrayOfResources) {
        arrayofData = arrayOfResources;
        this.mContext = context;
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup, int)
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView page = (ImageView) View.inflate(mContext, R.layout.vp_image_view, null);
        page.setImageResource(arrayofData[position]);
        container.addView(page);
        return page;
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return arrayofData.length;
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.ViewGroup, int, java.lang.Object)
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}