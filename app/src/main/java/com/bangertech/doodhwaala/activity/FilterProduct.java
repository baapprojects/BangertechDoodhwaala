package com.bangertech.doodhwaala.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;

import com.bangertech.doodhwaala.beans.BeanFilter;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CGlobal;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.SwitchCompat;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by annutech on 9/23/2015.
 */
public class FilterProduct extends AppCompatActivity implements AsyncResponse {
    private Toolbar app_bar;
    private List<BeanFilter> lstBeanProductType=null,lstBeanFilterBrand=null, lstBeanFilterPackaging =null;
    private GridView gridProductType,gridBrand,gridPackaging;
    private FilterAdapter filterAdapterProductType,filterAdapterBrand,filterAdapterPackaging;

   /* private  final int PRODUCT_TYPE = 0;
    private  final int BRAND_TYPE = 1;
    private  final int PACKAGING_TYPE = 2;*/
    private int productFilterIndex=-1,brandFilterIndex=-1,packagingFilterIndex=-1;


    private Button butSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filter_product);
        app_bar = (Toolbar) findViewById(R.id.toolbar);//gridProductType
        butSave = (Button)app_bar.findViewById(R.id.tvSave);
        setSupportActionBar(app_bar);
        butSave.setVisibility(View.GONE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //app_bar.setPadding(0, CUtils.getStatusBarHeight(FilterProduct.this), 0, 0);
        getSupportActionBar().setTitle(getString(R.string.filters));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        gridProductType = (GridView) findViewById(R.id.gridProductType);
        gridBrand = (GridView) findViewById(R.id.gridBrand);
        gridPackaging = (GridView) findViewById(R.id.gridPackaging);

        if(getIntent().getStringExtra(ConstantVariables.SELECTED_FILTER_KEY)!=null)
            initSelectedFilterIndex(getIntent().getStringExtra(ConstantVariables.SELECTED_FILTER_KEY));

        if(CGlobal.getCGlobalObject().getFilterList().size()>0)
        {
            String item=CGlobal.getCGlobalObject().getFilterListItem(ConstantVariables.PRODUCT_TYPE);
            if(item!=null)
                showProductTypeList(item);
            else
              fetchProductType();

            item=CGlobal.getCGlobalObject().getFilterListItem(ConstantVariables.BRAND_TYPE);
            if(item!=null)
                showBrandList(item);
            else
                fetchBrandType();

            item=CGlobal.getCGlobalObject().getFilterListItem(ConstantVariables.PACKAGING_TYPE);
            if(item!=null)
                showPackagingList(item);
            else
                fetchPackagingType();


        }
        else
            fetchProductType();

        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFilter=getSelectedFilter();
                CUtils.printLog("setOnClickListener", selectedFilter, ConstantVariables.LOG_TYPE.ERROR);
                        if(!TextUtils.isEmpty(selectedFilter))
                        {
                            Intent output = new Intent();
                            output.putExtra(ConstantVariables.SELECTED_FILTER_KEY,selectedFilter);
                            setResult(RESULT_OK, output);
                        }
                else
                            setResult(RESULT_CANCELED);
                FilterProduct.this.finish();

            }
        });
        enableDisableSaveButton();
    }
    private void initSelectedFilterIndex(String value)
    {
        CUtils.printLog("initSelectedFilterIndex", value, ConstantVariables.LOG_TYPE.ERROR);
        try {
            JSONArray array = new JSONArray(value);
            JSONObject obj=null;
            int length=array.length();
            if(length>0)
            {
                int tag_type=-1;
                for(int index=0;index<length;index++)
                {
                    obj=array.getJSONObject(index);
                    tag_type=Integer.valueOf(obj.get("tag_type").toString());
                    switch(tag_type)
                    {
                        case ConstantVariables.PRODUCT_TAG_TYPE:
                            //productFilterIndex=Integer.valueOf(obj.get("tag_id").toString());
                            //productFilterIndex = PreferenceManager.getInstance().getProductFilterPosition();
                            break;
                        case ConstantVariables.BRAND_TAG_TYPE:
                            //brandFilterIndex=Integer.valueOf(obj.get("tag_id").toString());
                            break;
                        case ConstantVariables.PACKAGING_TAG_TYPE:
                            //packagingFilterIndex=Integer.valueOf(obj.get("tag_id").toString());
                            break;

                    }

                }
            }


        }
        catch(Exception e)
        {

        }

    }

    private String  getSelectedFilter()
    {
        JSONObject obj=null;
        BeanFilter beanFilter=null;
        JSONArray jsonArray=null;
        try {
             jsonArray=new JSONArray();
            if(PreferenceManager.getInstance().getProductFilterPosition()>=0) {
                obj = new JSONObject();//FOR PRODUCT TYPE
                 beanFilter=lstBeanProductType.get(PreferenceManager.getInstance().getProductFilterPosition());
                obj.put("tag_id",beanFilter.getId());
                obj.put("tag_name",beanFilter.getName());
                obj.put("tag_type",beanFilter.getTagType());
                jsonArray.put(obj);

            }
            if(PreferenceManager.getInstance().getBrandFilterPosition()>=0) {
                obj = new JSONObject();//FOR PRODUCT TYPE
                beanFilter=lstBeanFilterBrand.get(PreferenceManager.getInstance().getBrandFilterPosition());
                obj.put("tag_id",beanFilter.getId());
                obj.put("tag_name",beanFilter.getName());
                obj.put("tag_type",beanFilter.getTagType());
                jsonArray.put(obj);

            }
            if(PreferenceManager.getInstance().getPackagingFilterPosition()>=0) {
                obj = new JSONObject();//FOR PRODUCT TYPE
                beanFilter=lstBeanFilterPackaging.get(PreferenceManager.getInstance().getPackagingFilterPosition());
                obj.put("tag_id",beanFilter.getId());
                obj.put("tag_name",beanFilter.getName());
                obj.put("tag_type",beanFilter.getTagType());
                jsonArray.put(obj);

            }


        }
        catch(Exception e)
        {
         return null;
        }
        if(jsonArray.length()>0)
            return jsonArray.toString();
        else
            return null;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                PreferenceManager.getInstance().resetFilterPositions();
                 onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void finishActivityWithFilterSelection()
    {
       finish();

    }

    private void enableDisableSaveButton()
    {

        if(PreferenceManager.getInstance().getProductFilterPosition()>-1||PreferenceManager.getInstance().getBrandFilterPosition()>-1||PreferenceManager.getInstance().getPackagingFilterPosition()>-1)
            butSave.setVisibility(View.VISIBLE);
        else
            butSave.setVisibility(View.GONE);

    }
    private void fetchProductType()
    {
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("fetchProductType", FilterProduct.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action"},
                new String[]{"products", "fetchProductType"});
        myAsyncTask.execute();

    }
    private void fetchBrandType()
    {

        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("fetchBrand", FilterProduct.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action"},
                new String[]{"products", "fetchBrand"});
        myAsyncTask.execute();

    }
    private void fetchPackagingType()
    {

        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("fetchPackaging", FilterProduct.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action"},
                new String[]{"products", "fetchPackaging"});
        myAsyncTask.execute();

    }

    private void showProductTypeList(String output)
    {

        lstBeanProductType=parseAndFormateProductTypeList(output);

        if(lstBeanProductType!=null) {
            filterAdapterProductType = new FilterAdapter(lstBeanProductType,ConstantVariables.PRODUCT_TYPE);
            gridProductType.setAdapter(filterAdapterProductType);
        }
    }
    private void showBrandList(String output)
    {
        lstBeanFilterBrand=parseAndFormateBrandList(output);
        if(lstBeanFilterBrand!=null) {
            filterAdapterBrand=new FilterAdapter(lstBeanFilterBrand,ConstantVariables.BRAND_TYPE);
            gridBrand.setAdapter(filterAdapterBrand);
        }
    }

    private void showPackagingList(String output)
    {
        lstBeanFilterPackaging =parseAndFormatePackagingList(output);
        if(lstBeanFilterPackaging !=null) {
            filterAdapterPackaging=new FilterAdapter(lstBeanFilterPackaging,ConstantVariables.PACKAGING_TYPE);
            gridPackaging.setAdapter(filterAdapterPackaging);
        }
    }
    @Override
    public void backgroundProcessFinish(String from, String output) {


        if(from.equalsIgnoreCase("fetchProductType")) {
            CGlobal.getCGlobalObject().setFilterListItem(ConstantVariables.PRODUCT_TYPE, output);
            showProductTypeList(output);
            fetchBrandType();
            //CUtils.printLog(from, output, ConstantVariables.LOG_TYPE.ERROR);
        }
        if(from.equalsIgnoreCase("fetchBrand")) {
            CGlobal.getCGlobalObject().setFilterListItem(ConstantVariables.BRAND_TYPE, output);
            showBrandList(output);
            fetchPackagingType();
            //CUtils.printLog(from, output, ConstantVariables.LOG_TYPE.ERROR);
        }

        if(from.equalsIgnoreCase("fetchPackaging")) {
            CGlobal.getCGlobalObject().setFilterListItem(ConstantVariables.PACKAGING_TYPE, output);
            showPackagingList(output);
            //CUtils.printLog(from, output, ConstantVariables.LOG_TYPE.ERROR);
        }

    }
    private List<BeanFilter> parseAndFormateProductTypeList(String productTypeList)
    {
        List<BeanFilter> lstFilterProductType=null;

        try {
            JSONObject jsonObject = new JSONObject(productTypeList);
            if(jsonObject.getString("result").equalsIgnoreCase("true"))
            {
                lstFilterProductType=new ArrayList<BeanFilter>();
                JSONArray array=jsonObject.getJSONArray("product_types");
                if(array.length()>0) {
                    BeanFilter beanFilter;
                    JSONObject obj;
                    for(int i=0;i<array.length();i++) {

                        obj=array.getJSONObject(i);
                        if(obj!=null) {

                            beanFilter = new BeanFilter();
                            beanFilter.setId(obj.getString("tag_id"));
                            beanFilter.setName(obj.getString("tag_name"));
                            beanFilter.setTagType(obj.getString("tag_type"));
                            if(i==PreferenceManager.getInstance().getProductFilterPosition())
                                beanFilter.setIsChecked(true);

                            lstFilterProductType.add(beanFilter);
                        }
                    }

                }
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            lstFilterProductType=null;
        }

        return lstFilterProductType;
    }


    private List<BeanFilter> parseAndFormateBrandList(String outPut)
    {
        List<BeanFilter> lstBeanFilterBrand=null;

        try {
            JSONObject jsonObject = new JSONObject(outPut);
            if(jsonObject.getString("result").equalsIgnoreCase("true"))
            {
                lstBeanFilterBrand=new ArrayList<BeanFilter>();
                JSONArray array=jsonObject.getJSONArray("brands");
                if(array.length()>0) {
                    BeanFilter beanFilter;
                    JSONObject obj;
                    for(int i=0;i<array.length();i++) {

                        obj=array.getJSONObject(i);
                        if(obj!=null) {

                            beanFilter = new BeanFilter();
                            beanFilter.setId(obj.getString("tag_id"));
                            beanFilter.setName(obj.getString("tag_name"));
                            beanFilter.setTagType(obj.getString("tag_type"));
                            if(i==PreferenceManager.getInstance().getBrandFilterPosition())
                                beanFilter.setIsChecked(true);

                            lstBeanFilterBrand.add(beanFilter);
                        }
                    }

                }
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            lstBeanProductType=null;
        }

        return lstBeanFilterBrand;
    }

    private List<BeanFilter> parseAndFormatePackagingList(String outPut)
    {
        List<BeanFilter> lstFilterPackaging=null;

        try {
            JSONObject jsonObject = new JSONObject(outPut);
            if(jsonObject.getString("result").equalsIgnoreCase("true"))
            {
                lstFilterPackaging=new ArrayList<BeanFilter>();
                JSONArray array=jsonObject.getJSONArray("packagings");
                if(array.length()>0) {
                    BeanFilter beanFilter;
                    JSONObject obj;
                    for(int i=0;i<array.length();i++) {

                        obj=array.getJSONObject(i);
                        if(obj!=null) {

                            beanFilter = new BeanFilter();
                            beanFilter.setId(obj.getString("tag_id"));
                            beanFilter.setName(obj.getString("tag_name"));
                            beanFilter.setTagType(obj.getString("tag_type"));
                            if(i==PreferenceManager.getInstance().getPackagingFilterPosition())
                               beanFilter.setIsChecked(true);

                            lstFilterPackaging.add(beanFilter);
                        }
                    }

                }
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            lstBeanProductType=null;
        }

        return lstFilterPackaging;
    }

private void parseChoosedFilterOption(String tag,boolean isChecked)
{
    String delims = "[|]";
    String[] tokens = tag.split(delims);
    int listType=Integer.valueOf(tokens[0]);
    int itemPosition=Integer.valueOf(tokens[2]);
    boolean isItemChecked=false;

    switch(listType)
    {
        case ConstantVariables.PRODUCT_TYPE:
            int selectedIndexProduct=-1;
            for(int i=0;i<lstBeanProductType.size();i++)
            {
                if(itemPosition==i)
                  lstBeanProductType.get(i).setIsChecked(isChecked);
                else
                    lstBeanProductType.get(i).setIsChecked(false);

                if(lstBeanProductType.get(i).isChecked())
                    selectedIndexProduct=i;
            }
            PreferenceManager.getInstance().setProductFilterPosition(selectedIndexProduct);
            productFilterIndex=selectedIndexProduct;
            filterAdapterProductType.notifyDataSetChanged();

            break;
        case ConstantVariables.BRAND_TYPE:
            int selectedIndexBrand=-1;
            for(int i=0;i<lstBeanFilterBrand.size();i++)
            {
                if(itemPosition==i)
                    lstBeanFilterBrand.get(i).setIsChecked(isChecked);
                else
                    lstBeanFilterBrand.get(i).setIsChecked(false);
                if(lstBeanFilterBrand.get(i).isChecked())
                    selectedIndexBrand=i;
            }
            PreferenceManager.getInstance().setBrandFilterPosition(selectedIndexBrand);
            brandFilterIndex=selectedIndexBrand;
            filterAdapterBrand.notifyDataSetChanged();
            break;
        case ConstantVariables.PACKAGING_TYPE:
            int selectedIndexPackage=-1;
            for(int i=0;i<lstBeanFilterPackaging.size();i++)
            {
                if(itemPosition==i)
                    lstBeanFilterPackaging.get(i).setIsChecked(isChecked);
                else
                    lstBeanFilterPackaging.get(i).setIsChecked(false);

                if(lstBeanFilterPackaging.get(i).isChecked())
                    selectedIndexPackage=i;
            }
            PreferenceManager.getInstance().setPackagingFilterPosition(selectedIndexPackage);
            packagingFilterIndex=selectedIndexPackage;
            filterAdapterPackaging.notifyDataSetChanged();
            break;

    }
    enableDisableSaveButton();

}

    class FilterAdapter extends BaseAdapter {
        List<BeanFilter> lstFilter;
        int listType=0;

        public FilterAdapter( List<BeanFilter> lstFilter,int listType) {
            this.lstFilter=lstFilter;
            this.listType=listType;
        }

        @Override
        public int getCount() {
            return lstFilter.size();
        }

        @Override
        public Object getItem(int position) {
            return lstFilter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BeanFilter beanFilter;
            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.row_filter_product, parent, false);
            beanFilter = lstFilter.get(position);
            SwitchCompat swicthFilter = (SwitchCompat) view.findViewById(R.id.swicthFilter);
            swicthFilter.setText(beanFilter.getName());
            swicthFilter.setChecked(beanFilter.isChecked());
            final String tag=String.valueOf(listType)+"|"+beanFilter.getId()+"|"+String.valueOf(position);
            swicthFilter.setTag(tag);

            swicthFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    try {
                        parseChoosedFilterOption(tag, isChecked);
                    } catch (Exception e) {

                    }
                }
            });

            gridProductType.deferNotifyDataSetChanged();
            gridBrand.deferNotifyDataSetChanged();
            gridPackaging.deferNotifyDataSetChanged();

            return view;
        }
    }

    @Override
    public void onBackPressed() {
        PreferenceManager.getInstance().resetFilterPositions();
        super.onBackPressed();
    }
}

