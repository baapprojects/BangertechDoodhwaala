package com.bangertech.doodhwaala.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.adapter.ViewPagerAdapter;
import com.bangertech.doodhwaala.beans.BeanBrand;
import com.bangertech.doodhwaala.beans.BeanDayPlan;
import com.bangertech.doodhwaala.beans.BeanProduct;
import com.bangertech.doodhwaala.beans.BeanProductType;
import com.bangertech.doodhwaala.customcontrols.CustomViewPager;
import com.bangertech.doodhwaala.customcontrols.SlidingTabLayout;
import com.bangertech.doodhwaala.fragment.MeFragment;
import com.bangertech.doodhwaala.fragment.MilkbarFragment;
import com.bangertech.doodhwaala.fragment.MyMilkFragment;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CGlobal;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;
import com.helpshift.D;
import com.helpshift.Helpshift;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 9/22/2015.
 */
public class Home extends AppCompatActivity implements AsyncResponse {
    CustomViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout slidingTabLayout;

    int Numboftabs =3;
    private Toolbar app_bar;
    public MenuItem menuItemSearch=null;
    private int MILK_BAR=0,MY_MILK=1,ME=2;
    private MeFragment meFragment;
    private MilkbarFragment milkbarFragment;
    private MyMilkFragment myMilkFragment;

    private MyFragmentPagerAdapter myPagerAdapter;

    private List<BeanProductType> lstBeanProductType=null;
    private List<BeanBrand> lstMostSellingProducts=null;
    private List<BeanProductType> lstBeanProductTypeOnToolBar=null;
    private LinearLayout llFilterMilkBarOnToolbar;
    private TextView txtViewMilkBarOnToolbarTitle;
    private HorizontalScrollView hsFilterMilkBarOnToolbar;
    private int pauseOrResumeIndex=-1;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstBeanProductType=new ArrayList<BeanProductType>();
        lstMostSellingProducts=new ArrayList<BeanBrand>();
        lstBeanProductTypeOnToolBar=new ArrayList<BeanProductType>();
        //INSTALLING HELPSHIFT SDK TO USE IN CHAT SUPPORT
        Helpshift.install(getApplication(), ConstantVariables.ACCESS_KEY_HELP_SHIFT, "bash.helpshift.com", ConstantVariables.APP_ID_HELP_SHIFT);
        //END
        setContentView(R.layout.lay_home);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mToolbar.setMinimumHeight(CUtils.getStatusBarHeight(Home.this));
        mToolbar.setVisibility(View.GONE);

        //txtViewMilkBarOnToolbarTitle = (TextView) app_bar.findViewById(R.id.txtViewMilkBarOnToolbarTitle);
        llFilterMilkBarOnToolbar = (LinearLayout)findViewById(R.id.llFilterMilkBarOnToolbar);
        hsFilterMilkBarOnToolbar = (HorizontalScrollView) findViewById(R.id.hsFilterMilkBarOnToolbar);

        // txtViewMilkBarOnToolbarTitle.setText(R.string.app_name);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.

        myPagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),getResources().getStringArray(R.array.home_tab_array));
        // Assigning ViewPager View and setting the adapter
        pager = (CustomViewPager) findViewById(R.id.pager);
        //pager.setAdapter(adapter);
        pager.setPagingEnabled(true);
        pager.setAdapter(myPagerAdapter);

        // Assigning the Sliding Tab Layout View
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        slidingTabLayout.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width



        // Setting the ViewPager For the SlidingTabsLayout
        slidingTabLayout.setViewPager(pager);

        fetchProductType();

       pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
               hideShowSearchOption(position);
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });




    }

    public void addFilterProductTypeOnToolBar(BeanProductType beanProductType)
    {
       lstBeanProductTypeOnToolBar.add(beanProductType);
        Toast.makeText(this, String.valueOf(lstBeanProductTypeOnToolBar.size()),Toast.LENGTH_SHORT).show();
        showFilterProductTypeOnToolBar();

    }
    public void gotoTagsAndProductsScreen(BeanProductType beanProductType)
    {
        Bundle  bundle=new Bundle();
        bundle.putString("tag_id", beanProductType.getTagId());
        bundle.putString("tag_name", beanProductType.getTagName());
        bundle.putString("tag_type", beanProductType.getTagType());
        Intent intent=new Intent(this, FilteresAppliedByTagsAndProducts.class);
        intent.putExtras(bundle);

        startActivity(intent);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

    }

    private void showFilterProductTypeOnToolBar()
    {

        int itemInBucket=lstBeanProductTypeOnToolBar.size();
        if((itemInBucket)==0)
           hsFilterMilkBarOnToolbar.setVisibility(View.GONE);
        if((itemInBucket)==1)
            hsFilterMilkBarOnToolbar.setVisibility(View.VISIBLE);

        if(itemInBucket>0) {
            int childCount=llFilterMilkBarOnToolbar.getChildCount();
            if(childCount>0)
            llFilterMilkBarOnToolbar.removeAllViews();

           /* int size=lstBeanProductTypeOnToolBar.size();*/
            int size=itemInBucket;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for(int i =0;i<size;i++)
            {

               View view=inflater.inflate(R.layout.item_filter_toolbar_milk_bar, null);//txtViewFilterToobar
                TextView txtFilter=(TextView)view.findViewById(R.id.txtViewFilterToobar);
                txtFilter.setText(lstBeanProductTypeOnToolBar.get(i).getTagName());

                view.setTag(String.valueOf(i));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFilterProductTypeOnToolBar(Integer.parseInt(v.getTag().toString()));
                    }
                });
                llFilterMilkBarOnToolbar.addView(view,i);

              /*  TextView txtFilter=(TextView)inflater.inflate(R.layout.item_filter_toolbar_milk_bar, null);
                txtFilter.setText(lstBeanProductTypeOnToolBar.get(i).getTagName());

                txtFilter.setTag(String.valueOf(i));
                txtFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFilterProductTypeOnToolBar(Integer.parseInt(v.getTag().toString()));
                    }
                });
                llFilterMilkBarOnToolbar.addView(txtFilter,i);*/
            }

        }
       /* else
            hsFilterMilkBarOnToolbar.setVisibility(View.GONE);*/
    }

    private void removeFilterProductTypeOnToolBar(int position)
    {
        if(lstBeanProductTypeOnToolBar.size()>0) {
            BeanProductType beanProductType = lstBeanProductTypeOnToolBar.get(position);
            milkbarFragment.addFilterProductTypeAgainRemoveFromToolBar(beanProductType);
            lstBeanProductTypeOnToolBar.remove(position);
            showFilterProductTypeOnToolBar();
        }

        //addFilterProductTypeAgainRemoveFromToolBar
    }

    /**
     * This function is used to hide and show search option on action bar
     * @param position
     */
    private void hideShowSearchOption(int position)
    {
       if(position==MILK_BAR) {

       }
        if(position==MY_MILK) {
            MyMilkFragment.textViewDate.setText(MyMilkFragment.date_string);
            MyMilkFragment.textViewDayHeading.setVisibility(View.VISIBLE);
        }
        if(position==ME) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menuItemSearch = menu.findItem(R.id.action_search);
        /*LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_menu_item_save, null);
       menuItemSearch.setActionView(view);*/
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //startActivity(new Intent(ShowAddress.this, AddUserAddress.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("fetchProductType")) {
            if(!parseAndFormateProductTypeList(output))
                lstBeanProductType.clear();

             fetchMostSellingProducts();

        }
        if(from.equalsIgnoreCase("mostSellingProducts"))
        {
            if(!parseAndFormateMostSellingProductsList(output))
                lstMostSellingProducts.clear();

            milkbarFragment.reDrawFragment(lstBeanProductType,lstMostSellingProducts);
            fetchDayPlanFromServer("", 0);

        }
        if (from.equalsIgnoreCase("fetchDayPlan")) {
            myMilkFragment.reDrawFragment(output);
        }

        if (from.equalsIgnoreCase("updatePauseOrResumePlan"))
        {
            try
            {

                if((new JSONObject(output)).getBoolean("result")) {
                    myMilkFragment.updatePauseOrResumePlanInList(pauseOrResumeIndex);

                }
            }
            catch(Exception e)
            {

            }
            pauseOrResumeIndex=-1;
        }
        meFragment.reDrawFragment();

    }


    private boolean parseAndFormateProductTypeList(String productTypeList)
    {
        boolean isSuccess=true;

        try {
            JSONObject jsonObject = new JSONObject(productTypeList);
            //if(jsonObject.getString("result").equalsIgnoreCase("true"))
            if(jsonObject.getBoolean("result"))
            {

                lstBeanProductType.clear();
                JSONArray array=jsonObject.getJSONArray("product_types");
                if(array.length()>0) {
                    BeanProductType beanProductType;
                    JSONObject obj;
                    for(int i=0;i<array.length();i++) {

                        obj=array.getJSONObject(i);
                        if(obj!=null) {

                            beanProductType = new BeanProductType();
                            beanProductType.setTagId(obj.getString("tag_id"));
                            beanProductType.setTagName(obj.getString("tag_name"));
                            beanProductType.setTagType(obj.getString("tag_type"));
                            lstBeanProductType.add(beanProductType);
                        }
                    }

                }
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            isSuccess=false;
        }

        return isSuccess;
    }
    private boolean parseAndFormateMostSellingProductsList(String mostSellingProducts)
    {
        boolean isSuccess=true;
        try {
            //Toast.makeText(this,mostSellingProducts,Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = new JSONObject(mostSellingProducts);
            //if(jsonObject.getString("result").equalsIgnoreCase("true"))
            if(jsonObject.getBoolean("result"))
            {
               // Toast.makeText(this,"true",Toast.LENGTH_SHORT).show();

                lstMostSellingProducts.clear();
                JSONArray arrayBrand=jsonObject.getJSONArray("details");//BRAND

                if(arrayBrand.length()>0) {
                    BeanBrand  beanBrand=null;
                    JSONObject objJsonBrand=null;

                    for(int brandIndex=0;brandIndex<arrayBrand.length();brandIndex++)//PARSING BRAND
                    {
                        JSONArray arrayProduct=null;

                       // objJsonBrand=arrayBrand.getJSONObject(brandIndex);
                        //HERE CALL BRAND FUNCTION
                        //beanBrand=getParsedMostSellingBrand(objJsonBrand);
                        beanBrand=getParsedMostSellingBrand(arrayBrand.getJSONObject(brandIndex));
                        if(beanBrand!=null)
                         lstMostSellingProducts.add(beanBrand);//ADDING BRAND OBJECT IN COLLECTION

                    }


                }
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            isSuccess=false;
        }

        return isSuccess;
    }

    private BeanBrand getParsedMostSellingBrand(JSONObject jsonBrand)
    {
        BeanBrand beanBrand=null;
        try {
            if (jsonBrand != null) {
                beanBrand = new BeanBrand();
                beanBrand.setBrandId(jsonBrand.getString("brand_id"));
                beanBrand.setBrandType(jsonBrand.getString("brand_type"));
                beanBrand.setBrandName(jsonBrand.getString("brand_name"));

                JSONObject jsonProductsInformation=jsonBrand.getJSONObject("products_information");
                if(jsonProductsInformation.getString("result").equalsIgnoreCase("true")) {
                    JSONArray jsonProductsArray=jsonProductsInformation.getJSONArray("products");
                    JSONObject objJsonProduct=null;
                    BeanProduct beanProduct=null;

                    for(int productIndex=0;productIndex<jsonProductsArray.length();productIndex++)//PARSING BRAND
                    {
                        beanProduct=getParsedMostSellingProduct(jsonProductsArray.getJSONObject(productIndex));
                        if(beanProduct!=null)
                            beanBrand.addProduct(beanProduct);
                    }


                }
            }
        }
        catch(Exception e)
        {

        }

        return beanBrand;

    }
    private BeanProduct getParsedMostSellingProduct(JSONObject jsonProduct)
    {
        BeanProduct beanProduct=null;
        try {
            if (jsonProduct != null) {
                beanProduct = new BeanProduct();
                beanProduct.setPrice(jsonProduct.getString("price"));
                beanProduct.setProductId(jsonProduct.getString("product_id"));
                beanProduct.setProductImage(jsonProduct.getString("product_image"));
                beanProduct.setProductMappingId(jsonProduct.getString("product_mapping_id"));
                beanProduct.setProductName(jsonProduct.getString("product_name"));
                //beanBrand.addProduct(beanProduct);//ADDING PRODUCT VIN BRAND OBJECT
            }
        }
        catch(Exception e)
        {

        }
        return beanProduct;

    }


    public  class MyFragmentPagerAdapter extends FragmentPagerAdapter {

       List<Fragment> fragments;
        String[] _titles;

        public MyFragmentPagerAdapter(FragmentManager fragmentManager, String[] titles) {
            super(fragmentManager);
            _titles=titles;
            fragments=new ArrayList<Fragment>();
            Home.this.milkbarFragment=MilkbarFragment.newInstance();
            Home.this.myMilkFragment=MyMilkFragment.newInstance();
            Home.this.meFragment=MeFragment.newInstance();
            fragments.add( Home.this.milkbarFragment);
            fragments.add(Home.this.myMilkFragment);
            fragments.add(Home.this.meFragment);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }
      @Override
        public CharSequence getPageTitle(int position) {
            return _titles[ position];
        }

    }

    private void fetchProductType()
    {

        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("fetchProductType", Home.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action"},
                new String[]{"products", "fetchProductType"});
        myAsyncTask.execute();

    }
    private void fetchMostSellingProducts()
    {
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("mostSellingProducts", Home.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action"},
                new String[]{"products", "mostSellingProducts"});
        myAsyncTask.execute();
    }
    private void fetchDayPlanFromServer(String strDate,int moveIndex)
    {
        MyAsynTaskManager myAsyncTask = new MyAsynTaskManager();
        myAsyncTask.delegate = this;
        myAsyncTask.setupParamsAndUrl("fetchDayPlan", Home.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action","user_id","move","move_date"},
                new String[]{"plans", "fetchDayPlan", PreferenceManager.getInstance().getUserId(),String.valueOf(moveIndex),strDate});
        myAsyncTask.execute();
    }

    public void fetchPreviousOrNextDayMyPlan(String strDate,int moveIndex)
    {
       /* String _date=CUtils.getFormatedDateForMyPlan(strDate,moveIndex);
        CUtils.printLog("MY_DATE",_date, ConstantVariables.LOG_TYPE.ERROR);
        if(TextUtils.isEmpty(_date))
            fetchDayPlanFromServer("",ConstantVariables.MY_PLAN_TOMORROW);
        else
            fetchDayPlanFromServer(_date,moveIndex);*/

        if(TextUtils.isEmpty(strDate))
            fetchDayPlanFromServer("",ConstantVariables.MY_PLAN_TOMORROW);
        else
            fetchDayPlanFromServer(strDate,moveIndex);
    }

    public void updatePauseOrResumePlanOnServer(BeanDayPlan beanDayPlan,int prIndex)
    {
        pauseOrResumeIndex=prIndex;
        String action=beanDayPlan.isPaused()?"resumeUserPlan":"pauseUserPlan2";
        MyAsynTaskManager myAsyncTask = new MyAsynTaskManager();
        myAsyncTask.delegate = this;
        myAsyncTask.setupParamsAndUrl("updatePauseOrResumePlan", Home.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action","date_id"},
                new String[]{"plans", action, beanDayPlan.getDateId()});
        myAsyncTask.execute();
    }


}
