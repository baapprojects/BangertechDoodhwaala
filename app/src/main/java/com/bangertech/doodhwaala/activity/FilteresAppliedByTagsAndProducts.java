package com.bangertech.doodhwaala.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangertech.doodhwaala.adapter.FiltersAppliedProductAdapter;
import com.bangertech.doodhwaala.beans.BeanFilteredProduct;
import com.bangertech.doodhwaala.beans.BeanProduct;
import com.bangertech.doodhwaala.beans.BeanProductType;
import com.bangertech.doodhwaala.cinterfaces.ISelectedProduct;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 10/6/2015.
 */
public class FilteresAppliedByTagsAndProducts extends AppCompatActivity implements AsyncResponse,ISelectedProduct {
    private List<BeanProductType> filterBucket=new ArrayList<BeanProductType>();
    private List<BeanProductType> filteredTagList =new ArrayList<BeanProductType>();
    private List<BeanFilteredProduct> filterProductList=new ArrayList<BeanFilteredProduct>();
    private Toolbar app_bar;
    private HorizontalScrollView hsSelectedTags;
    private LinearLayout llSelectedTags;
    private GridView gridFilteredTagList,gridFilteredProductsList;

    private FilteredTagAdapter filteredTagAdapter;
    private ImageView imageViewBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagsandproducts);
        llSelectedTags = (LinearLayout)findViewById(R.id.llSelectedTags);
        hsSelectedTags = (HorizontalScrollView) findViewById(R.id.hsSelectedTags);
        gridFilteredTagList = (GridView) findViewById(R.id.gridFilteredTagList);
        gridFilteredProductsList = (GridView) findViewById(R.id.gridFilteredProductsList);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        filteredTagAdapter=new FilteredTagAdapter();


        filterBucket.clear();
        BeanProductType beanProductType=new BeanProductType();

        Bundle bundle=getIntent().getExtras();
        beanProductType.setTagId(bundle.getString("tag_id"));
        beanProductType.setTagName(bundle.getString("tag_name"));
        beanProductType.setTagType(bundle.getString("tag_type"));


        filterBucket.add(beanProductType);
        //CUtils.printLog("ActivityTagsAndProducts",beanProductType.getTagId()+"<>"+beanProductType.getTagName()+"<>"+beanProductType.getTagType(), ConstantVariables.LOG_TYPE.ERROR);
        showSelectedTagOnToolBar();

        //CUtils.printLog("getJsonArrayToFetchProduct()",getJsonArrayToFetchProduct(), ConstantVariables.LOG_TYPE.ERROR);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilteresAppliedByTagsAndProducts.this.finish();
            }
        });
        gridFilteredTagList.setAdapter(filteredTagAdapter);
    }


    private  String getInputInformationToFetchTagListFromServer()
    {
        JSONArray array=new JSONArray();
        JSONObject obj=null;
        int size=filterBucket.size();
        BeanProductType beanProductType=null;
        try {
            for (int index = 0; index < size; index++) {
                beanProductType = filterBucket.get(index);
                obj = new JSONObject();
                obj.put("tag_id", beanProductType.getTagId());
                obj.put("tag_type", beanProductType.getTagType());
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
    private void showSelectedTagOnToolBar()
    {

        int itemInBucket=filterBucket.size();

        if(itemInBucket>0) {
            int childCount=llSelectedTags.getChildCount();
            if(childCount>0)
                llSelectedTags.removeAllViews();

            int size=itemInBucket;
           LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for(int i =0;i<size;i++)
            {

                View view=inflater.inflate(R.layout.item_filter_product_on_toobar, null);
                TextView txtFilter=(TextView)view.findViewById(R.id.txtViewFilterToobar);
                txtFilter.setText(filterBucket.get(i).getTagName()+"  X");

                view.setTag(String.valueOf(i));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeSelectedTagFromToolBar(Integer.parseInt(v.getTag().toString()));
                    }
                });
                llSelectedTags.addView(view,i);
            }
            if(itemInBucket>0)
                fetchFilteredTags();

        }
        filteredTagAdapter.notifyDataSetChanged();

    }
    private void removeSelectedTagFromToolBar(int position)
    {
        if(filterBucket.size()>0) {

            filterBucket.remove(position);
            showSelectedTagOnToolBar();
        }
       if(filterBucket.size()==0)
            this.finish();

    }
    private void fetchFilteredTags()
    {


        String pType=getInputInformationToFetchTagListFromServer();
        if(pType!=null) {
            MyAsynTaskManager myAsyncTask = new MyAsynTaskManager();
            myAsyncTask.delegate = this;
            myAsyncTask.setupParamsAndUrl("fetchTagsandProducts", FilteresAppliedByTagsAndProducts.this, AppUrlList.ACTION_URL,
                    new String[]{"module", "action","tag_information"},
                    new String[]{"products", "fetchTagsandProducts",pType});
            myAsyncTask.execute();
        }

    }

    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("fetchTagsandProducts")) {
            parseAndFormateFilteredTagList(output);
        }


    }
    private boolean parseAndFormateFilteredTagList(String productTypeList)
    {
        boolean isSuccess=true;

        try {
            JSONObject jsonObject = new JSONObject(productTypeList);
            //if(jsonObject.getString("result").equalsIgnoreCase("true"))
            if(jsonObject.getBoolean("result"))
            {

                filteredTagList.clear();
                if(Integer.parseInt(jsonObject.getString("no_of_tags"))>0) {
                    JSONArray array = jsonObject.getJSONArray("tags");
                    if (array.length() > 0) {
                        BeanProductType beanProductType;
                        JSONObject obj;
                        for (int i = 0; i < array.length(); i++) {

                            obj = array.getJSONObject(i);
                            if (obj != null) {

                                beanProductType = new BeanProductType();
                                beanProductType.setTagId(obj.getString("tag_id"));
                                beanProductType.setTagName(obj.getString("tag_name"));
                                beanProductType.setTagType(obj.getString("tag_type"));
                                filteredTagList.add(beanProductType);
                            }
                        }
                        //gridFilterList.setAdapter(productTypeAdapter);

                    }
                }
                filteredTagAdapter.notifyDataSetChanged();
                if(filteredTagList.size()>0) {
                    parseAndFormateFilteredProductList(productTypeList);
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
    private boolean parseAndFormateFilteredProductList(String productTypeList)
    {
        boolean isSuccess=true;

        try {
            JSONObject jsonObject = new JSONObject(productTypeList);


                filterProductList.clear();
                if(Integer.parseInt(jsonObject.getString("no_of_products"))>0) {
                    JSONArray array = jsonObject.getJSONArray("products");
                    if (array.length() > 0) {
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

                                filterProductList.add(beanFilteredProduct);
                            }
                        }
                        gridFilteredProductsList.setAdapter(new FiltersAppliedProductAdapter(FilteresAppliedByTagsAndProducts.this,
                                filterProductList));

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

    @Override
    public void onSelectProduct(String productId,String productMappingId) {
        Intent intent=new Intent(FilteresAppliedByTagsAndProducts.this, ProductDetail.class);
        intent.putExtra(ConstantVariables.PRODUCT_ID_KEY,productId);
        intent.putExtra(ConstantVariables.PRODUCT_MAPPING_ID_KEY,productMappingId);
        startActivity(intent);
    }

    class FilteredTagAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return filteredTagList.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredTagList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            grid = inflater.inflate(R.layout.row_filtered_tag, null);
            TextView textViewTag = (TextView) grid.findViewById(R.id.textViewTag);
            textViewTag.setText(filteredTagList.get(position).getTagName());
            textViewTag.setTag(position);
            textViewTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(), String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
                    //setOnFilter(Integer.valueOf(v.getTag().toString()));
                    reFilterProduct(Integer.valueOf(v.getTag().toString()));
                }
            });
            return grid;
        }
    }

    private void reFilterProduct(int position) {
        filterBucket.add(filteredTagList.get(position));
        filteredTagList.clear();
        showSelectedTagOnToolBar();
    }


    /*class FilteredProductAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return filterProductList.size();
        }

        @Override
        public Object getItem(int position) {
            return filterProductList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.row_filtered_product, parent, false);

            TextView textViewProductName = (TextView) view.findViewById(R.id.textViewProductName);
            TextView TextViewProductPrice = (TextView) view.findViewById(R.id.textViewProductPrice);
            ImageView imageViewProduct= (ImageView) view.findViewById(R.id.imageViewProduct);

            BeanFilteredProduct beanFilteredProduct= filterProductList.get(position);
            textViewProductName.setText(beanFilteredProduct.getProductName());
            TextViewProductPrice.setText(beanFilteredProduct.getPrice());

            CUtils.donwloadImageFromServer(parent.getContext(), imageViewProduct, beanFilteredProduct.getProductImage());
            *//*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iSelectedProduct.onSelectProduct();
                }
            });*//*

            return view;
        }
    }*/
}
