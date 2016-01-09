package com.bangertech.doodhwaala.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangertech.doodhwaala.adapter.FiltersAppliedProductAdapter;
import com.bangertech.doodhwaala.beans.BeanFilter;
import com.bangertech.doodhwaala.beans.BeanFilteredProduct;
import com.bangertech.doodhwaala.cinterfaces.ISelectedProduct;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 9/23/2015.
 */
public class FiltersAppliedByFilterProduct extends AppCompatActivity implements AsyncResponse,ISelectedProduct {
    private Toolbar app_bar;
    private List<BeanFilter> lstSelectedFilter=new ArrayList<BeanFilter>();

    private List<BeanFilteredProduct> lstProductsFiltersApplied=new ArrayList<BeanFilteredProduct>();
    private LinearLayout llSelectedFilter;
    private GridView gridFiltersAppliedProductList;
    private FiltersAppliedProductAdapter filtersAppliedProductAdapter;
    private String selectedFiltersKey=null;
    private TextView no_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_by_filter_product);
        app_bar = (Toolbar) findViewById(R.id.app_bar);
        //app_bar.setPadding(0, CUtils.getStatusBarHeight(FiltersAppliedByFilterProduct.this), 0, 0);
        llSelectedFilter = (LinearLayout) findViewById(R.id.llSelectedFilter);
        gridFiltersAppliedProductList = (GridView) findViewById(R.id.gridFiltersAppliedProductList);

        no_result = (TextView) findViewById(R.id.no_result);

        setSupportActionBar(app_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.filters_applied));
        filtersAppliedProductAdapter=new FiltersAppliedProductAdapter(FiltersAppliedByFilterProduct.this,
                lstProductsFiltersApplied);

        if(getIntent().getStringExtra(ConstantVariables.SELECTED_FILTER_KEY)!=null)
            parseIncomingSelectedFilter( getIntent().getStringExtra(ConstantVariables.SELECTED_FILTER_KEY));

        ((FloatingActionButton) findViewById(R.id.fabFiltersApplied)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FiltersAppliedByFilterProduct.this, FilterProduct.class);

                if(!TextUtils.isEmpty(selectedFiltersKey)) {
                    Intent input = new Intent();
                    input.putExtra(ConstantVariables.SELECTED_FILTER_KEY,selectedFiltersKey);
                    input.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtras(input);

                }
                     startActivityForResult(intent,ConstantVariables.SUB_ACTIVITY_FILTER_OPENED_ON_FILTERS_APPLIED);
                     overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//parseIncomingSelectedFilter
         /*if(requestCode==ConstantVariables.SUB_ACTIVITY_FILTER_OPENED_ON_FILTERS_APPLIED && resultCode== Activity.RESULT_OK)
            startActivity(new Intent(FiltersAppliedByFilterProduct.this, FiltersAppliedByFilterProduct.class).putExtras(data));*/
        if(requestCode==ConstantVariables.SUB_ACTIVITY_FILTER_OPENED_ON_FILTERS_APPLIED && resultCode== Activity.RESULT_OK)
            parseIncomingSelectedFilter(data.getStringExtra(ConstantVariables.SELECTED_FILTER_KEY));

    }

    private void parseIncomingSelectedFilter(String filters)
    {

        CUtils.printLog("BIJENDRA-parseIncomingSelectedFilter",filters.toString(), ConstantVariables.LOG_TYPE.ERROR);
        try {
            selectedFiltersKey=filters;
            lstSelectedFilter.clear();
            JSONArray jsonArray=new JSONArray(filters);
            BeanFilter beanFilter;
            JSONObject obj=null;
            int length=jsonArray.length();
            //CUtils.printLog("LENGTH", String.valueOf(length), ConstantVariables.LOG_TYPE.ERROR);
            if(length>0)
            {
                for(int index=0;index<length;index++)
                {
                    obj=jsonArray.getJSONObject(index);
                    if(obj!=null)
                    {
                        beanFilter=new BeanFilter();
                        beanFilter.setId(obj.getString("tag_id"));
                        beanFilter.setName(obj.getString("tag_name"));
                        beanFilter.setTagType(obj.getString("tag_type"));
                        lstSelectedFilter.add(beanFilter);
                    }
                   //CUtils.printLog("SELECTED_FILTER-values", obj.toString(), ConstantVariables.LOG_TYPE.ERROR);

                }
                showIncomingSelectedFilterOnToolBar();
                fetchFiltersAppliedProductList();
            }
        }
        catch(Exception e)
        {
            CUtils.printLog("SELECTED_FILTER-ERROR", e.getMessage(), ConstantVariables.LOG_TYPE.ERROR);
        }

    }
    private void showIncomingSelectedFilterOnToolBar()
    {
            int itemInBucket=lstSelectedFilter.size();

            if(itemInBucket>0) {
                int childCount=llSelectedFilter.getChildCount();
                if(childCount>0)
                    llSelectedFilter.removeAllViews();

                int size=itemInBucket;
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for(int i =0;i<size;i++)
                {

                    View view=inflater.inflate(R.layout.row_filters_appliedby_filterproduct_toolbar, null);
                    TextView txtFilter=(TextView)view.findViewById(R.id.txtViewFilterToobar);
                    txtFilter.setText(lstSelectedFilter.get(i).getName());

                    view.setTag(String.valueOf(i));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    llSelectedFilter.addView(view,i);
                }

            }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void fetchFiltersAppliedProductList()
    {

        String pType=getInputInformationToFetchProductListFromServer();
        if(pType!=null) {
            MyAsynTaskManager myAsyncTask = new MyAsynTaskManager();
            myAsyncTask.delegate = this;
            myAsyncTask.setupParamsAndUrl("fetchProductsBasedFilter", FiltersAppliedByFilterProduct.this, AppUrlList.ACTION_URL,
                    new String[]{"module", "action","filter_information"},
                    new String[]{"products", "fetchProductsBasedFilter",pType});
            myAsyncTask.execute();
        }

    }
    private  String getInputInformationToFetchProductListFromServer()
    {
        JSONArray array=new JSONArray();
        JSONObject obj=null;
        int size=lstSelectedFilter.size();
        BeanFilter beanFilter=null;
        try {
            for (int index = 0; index < size; index++) {
                beanFilter = lstSelectedFilter.get(index);
                obj = new JSONObject();
                obj.put("tag_id", beanFilter.getId());
                obj.put("tag_type", beanFilter.getTagType());
                array.put(obj);
            }
            return array.toString();
        }
        catch(Exception e)
        {

        }
        //array.put()
        return null;
    }
    @Override
    public void backgroundProcessFinish(String from, String output) {

        if(from.equalsIgnoreCase("fetchProductsBasedFilter")) {
            lstProductsFiltersApplied.clear();
            if(isProductListExists(output))
                parseAndFormateFilteredProductList(output);
        }
        filtersAppliedProductAdapter.notifyDataSetChanged();

    }
    private boolean isProductListExists(String filtersAppliedProductList)
    {
        boolean isSuccess = false;
        if(filtersAppliedProductList!=null) {

            try {
                JSONObject jsonObject = new JSONObject(filtersAppliedProductList);
                //if(jsonObject.getString("result").equalsIgnoreCase("true"))
                if (jsonObject.getBoolean("result")) {
                    if (Integer.parseInt(jsonObject.getString("no_of_products")) > 0)
                        return true;
                }
                else {
                    gridFiltersAppliedProductList.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                isSuccess = false;
            }
        } else {
            isSuccess = false;
            DialogManager.showDialog(FiltersAppliedByFilterProduct.this, "Server Error Occurred! Try Again!");
        }

        return false;
    }
    private void parseAndFormateFilteredProductList(String filtersAppliedProductList)
    {
        if(filtersAppliedProductList!=null) {
            try {
                JSONObject jsonObject = new JSONObject(filtersAppliedProductList);
                if(jsonObject.getBoolean("result")) {
                    JSONArray array = jsonObject.getJSONArray("products");
                    if (array.length() > 0) {
                        gridFiltersAppliedProductList.setVisibility(View.VISIBLE);
                        no_result.setVisibility(View.GONE);
                        BeanFilteredProduct beanFilteredProduct;
                        JSONObject obj;
                        for (int i = 0; i < array.length(); i++) {

                            obj = array.getJSONObject(i);
                            if (obj != null) {

                                beanFilteredProduct = new BeanFilteredProduct();
                                beanFilteredProduct.setPrice(obj.getString("price"));
                                beanFilteredProduct.setAvailable(obj.getString("available"));
                                beanFilteredProduct.setBrandId(obj.getString("brand_id"));
                                beanFilteredProduct.setDescription(obj.getString("description"));
                                beanFilteredProduct.setNoOfPurchases(obj.getString("no_of_purchases"));
                                beanFilteredProduct.setPackagingId(obj.getString("packaging_id"));
                                beanFilteredProduct.setProductId(obj.getString("product_id"));
                                beanFilteredProduct.setProductImage(obj.getString("product_image"));
                                beanFilteredProduct.setProductMappingId(obj.getString("product_mapping_id"));
                                beanFilteredProduct.setProductName(obj.getString("product_name"));
                                beanFilteredProduct.setQuantityId(obj.getString("quantity_id"));
                                beanFilteredProduct.setProductTypeId(obj.getString("product_type_id"));

                                lstProductsFiltersApplied.add(beanFilteredProduct);
                            }
                        }
                        gridFiltersAppliedProductList.setAdapter(filtersAppliedProductAdapter);

                    } else {
                        gridFiltersAppliedProductList.setVisibility(View.GONE);
                        no_result.setVisibility(View.VISIBLE);
                    }
                } else {
                    gridFiltersAppliedProductList.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();

            }
        } else {
            DialogManager.showDialog(FiltersAppliedByFilterProduct.this, "Server Error Occurred! Try Again!");
        }


    }

    @Override
    public void onSelectProduct(String productId, String productMappingId) {
        Intent intent=new Intent(FiltersAppliedByFilterProduct.this, ProductDetail.class);
        intent.putExtra(ConstantVariables.PRODUCT_ID_KEY,productId);
        intent.putExtra(ConstantVariables.PRODUCT_MAPPING_ID_KEY,productMappingId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        PreferenceManager.getInstance().resetFilterPositions();
        super.onBackPressed();
    }
}
