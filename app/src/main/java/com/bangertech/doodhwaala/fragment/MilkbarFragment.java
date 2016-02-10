package com.bangertech.doodhwaala.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangertech.doodhwaala.activity.FilterProduct;
import com.bangertech.doodhwaala.activity.FiltersAppliedByFilterProduct;
import com.bangertech.doodhwaala.activity.Home;
import com.bangertech.doodhwaala.activity.ProductDetail;
import com.bangertech.doodhwaala.activity.SearchActivity;
import com.bangertech.doodhwaala.adapter.MostSellingBrandAdapter;
import com.bangertech.doodhwaala.beans.BeanBrand;
import com.bangertech.doodhwaala.beans.BeanProduct;
import com.bangertech.doodhwaala.beans.BeanProductType;
import com.bangertech.doodhwaala.cinterfaces.IBrandAllProduct;
import com.bangertech.doodhwaala.cinterfaces.ISelectedProduct;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import java.util.ArrayList;
import java.util.List;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class MilkbarFragment extends Fragment /*implements AsyncResponse*/ implements IBrandAllProduct,ISelectedProduct {

    private List<BeanProductType> lstBeanProductType=new ArrayList<BeanProductType>();
    private List<BeanBrand> lstProducts=new ArrayList<BeanBrand>();
    private MyAsynTaskManager myAsyncTask;
    private String userId="1";
    private GridView gridProductType;
    private View view;
    private ProductTypeAdapter productTypeAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton     fabFilterProduct;
    private ImageView searchProductType, milkStoreTutorial;
    private List<BeanBrand> lstMostSellingProducts=new ArrayList<BeanBrand>();
    public static MilkbarFragment newInstance() {

        return new MilkbarFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getActivity(),"onActivityResult",Toast.LENGTH_SHORT).show();
        if(requestCode==ConstantVariables.SUB_ACTIVITY_FILTER_OPENED_ON_MILKBAR && resultCode== Activity.RESULT_OK) {
            startActivity(new Intent(getActivity(), FiltersAppliedByFilterProduct.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(data));
            getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        }


    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_milkbar, container, false);

        milkStoreTutorial = (ImageView) view.findViewById(R.id.milkStoreTutorial);
        searchProductType = (ImageView) view.findViewById(R.id.searchProductType);
        gridProductType=(GridView) view.findViewById(R.id.gridProductType);
        fabFilterProduct=(FloatingActionButton)view.findViewById(R.id.fabFilterProduct);
        mRecyclerView= (RecyclerView)view.findViewById(R.id.my_recycler_view);

        if(PreferenceManager.getInstance().getMilkBarTutorial()) {
            milkStoreTutorial.setVisibility(View.GONE);
        } else {
            milkStoreTutorial.setVisibility(View.VISIBLE);
        }

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        milkStoreTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getInstance().setMilkBarTutorial(true);
                milkStoreTutorial.setVisibility(View.GONE);
            }
        });

        searchProductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });


        fabFilterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(getActivity(), FilterProduct.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), ConstantVariables.SUB_ACTIVITY_FILTER_OPENED_ON_MILKBAR);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
             /*  Toast.makeText(getActivity(),"FAB clicked",Toast.LENGTH_SHORT).show();*/
            }
        });

        if(this.lstBeanProductType.size()>0) {
            productTypeAdapter = new ProductTypeAdapter(getActivity(),this,lstBeanProductType);
            gridProductType.setAdapter(productTypeAdapter);
            gridProductType.setVisibility(View.VISIBLE);
        }

        if (this.lstMostSellingProducts.size()>0)
        {

            mAdapter=new MostSellingBrandAdapter(getActivity(),this,lstMostSellingProducts);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //if(gridProductType.getCount()==0)
            //initProductType();
        //initProducts();

    }

    public void reProductType(String output) {
        parseAndFormateProductTypeList(output);
    }

    public void reDrawFragment(String output)
    {
    parseAndFormateMostSellingProductsList(output);
    }

    private void setOnFilter(int position )
    {

        BeanProductType beanProductType=lstBeanProductType.get(position);

        ((Home) getActivity()).gotoTagsAndProductsScreen(beanProductType);

    }

    public void addFilterProductTypeAgainRemoveFromToolBar(BeanProductType beanProductType)
    {
        lstBeanProductType.add(beanProductType);
        productTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectViewAllProduct(BeanBrand beanBrand) {
        String brandParameter=getBrandParameterToSend(beanBrand);
        if(!TextUtils.isEmpty(brandParameter)) {
            Intent data = new Intent();
            data.putExtra(ConstantVariables.SELECTED_FILTER_KEY, brandParameter);
            startActivity(new Intent(getActivity(), FiltersAppliedByFilterProduct.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(data));
            getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        }
    }
    private String  getBrandParameterToSend(BeanBrand beanBrand)
    {
        JSONObject obj=null;

        JSONArray jsonArray=null;
        try {
            jsonArray=new JSONArray();
            obj = new JSONObject();//FOR PRODUCT TYPE

            obj.put("tag_id",beanBrand.getBrandId());
            obj.put("tag_name",beanBrand.getBrandName());
            obj.put("tag_type",beanBrand.getBrandType());
            jsonArray.put(obj);
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
    public void onSelectProduct(String productId,String productMappingId) {
        Intent intent=new Intent(getActivity(), ProductDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ConstantVariables.PRODUCT_ID_KEY,productId);
        intent.putExtra(ConstantVariables.PRODUCT_MAPPING_ID_KEY,productMappingId);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }


    public class ProductTypeAdapter extends BaseAdapter
    {
        private Context context;
        private List<BeanProductType> lstProductsType;
        public ProductTypeAdapter(Context context, Fragment fragment,List<BeanProductType> lstProductsType) {
            this.context = context;
            this.lstProductsType=lstProductsType;
        }
        @Override
        public int getCount() {
            return lstBeanProductType.size();
        }

        @Override
        public Object getItem(int position) {
            return lstBeanProductType.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            grid = inflater.inflate(R.layout.row_product_type, null);
            BeanProductType beanType= this.lstProductsType.get(position);
            LinearLayout lltextViewProduct = (LinearLayout) grid.findViewById(R.id.lltextViewProduct);
            TextView textView = (TextView) grid.findViewById(R.id.textViewProduct);
            textView.setText(beanType.getTagName());
            textView.setTag(position);

            lltextViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnFilter(position);
                }
            });
            return grid;
        }
    }

    private boolean parseAndFormateMostSellingProductsList(String mostSellingProducts)
    {
        boolean isSuccess = true;
        if(mostSellingProducts!= null) {
            try {
                //Toast.makeText(this,mostSellingProducts,Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject(mostSellingProducts);
                //if(jsonObject.getString("result").equalsIgnoreCase("true"))
                    if (jsonObject.getBoolean("result")) {
                        // Toast.makeText(this,"true",Toast.LENGTH_SHORT).show();

                        lstMostSellingProducts.clear();
                        JSONArray arrayBrand = jsonObject.getJSONArray("details");//BRAND

                        if (arrayBrand.length() > 0) {
                            BeanBrand beanBrand = null;
                            JSONObject objJsonBrand = null;

                            for (int brandIndex = 0; brandIndex < arrayBrand.length(); brandIndex++)//PARSING BRAND
                            {
                                JSONArray arrayProduct = null;

                                // objJsonBrand=arrayBrand.getJSONObject(brandIndex);
                                //HERE CALL BRAND FUNCTION
                                //beanBrand=getParsedMostSellingBrand(objJsonBrand);
                                beanBrand = getParsedMostSellingBrand(arrayBrand.getJSONObject(brandIndex));
                                if (beanBrand != null)
                                    lstMostSellingProducts.add(beanBrand);//ADDING BRAND OBJECT IN COLLECTION

                            }

                            if (this.lstMostSellingProducts.size() > 0) {

                                mAdapter = new MostSellingBrandAdapter(getActivity(), this, lstMostSellingProducts);
                                mRecyclerView.setAdapter(mAdapter);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                        }

                        JSONArray arrayPType = jsonObject.getJSONArray("product_types");//ProductType
                        lstBeanProductType.clear();
                        if (arrayPType.length() > 0) {
                            BeanProductType beanProductType;
                            JSONObject obj;
                            for (int i = 0; i < arrayPType.length(); i++) {

                                obj = arrayPType.getJSONObject(i);
                                if (obj != null) {

                                    beanProductType = new BeanProductType();
                                    beanProductType.setTagId(obj.getString("tag_id"));
                                    beanProductType.setTagName(obj.getString("tag_name"));
                                    beanProductType.setTagType(obj.getString("tag_type"));
                                    lstBeanProductType.add(beanProductType);

                                }
                            }
                            if (this.lstBeanProductType.size() > 0) {
                                productTypeAdapter = new ProductTypeAdapter(getActivity(), this, lstBeanProductType);
                                gridProductType.setAdapter(productTypeAdapter);
                                gridProductType.setVisibility(View.VISIBLE);
                            }

                            ((Home)getActivity()).fetchDayPlanFromServer("", 0);
                        }
                    }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                isSuccess = false;
            }
            return isSuccess;
        } else {
            isSuccess = false;
            DialogManager.showDialog(getActivity(), "Server Error Occurred! Try Again!");
            return isSuccess;
        }

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

    private boolean parseAndFormateProductTypeList(String productTypeList)
    {
        boolean isSuccess=true;
        if(productTypeList!=null) {
            try {
                JSONObject jsonObject = new JSONObject(productTypeList);
                //if(jsonObject.getString("result").equalsIgnoreCase("true"))
                if (jsonObject.getBoolean("result")) {

                    lstBeanProductType.clear();
                    JSONArray array = jsonObject.getJSONArray("product_types");
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
                                lstBeanProductType.add(beanProductType);

                            }
                        }
                        if(this.lstBeanProductType.size()>0) {
                            productTypeAdapter = new ProductTypeAdapter(getActivity(),this,lstBeanProductType);
                            gridProductType.setAdapter(productTypeAdapter);
                            gridProductType.setVisibility(View.VISIBLE);
                        }


                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                isSuccess = false;
            }
        }

        return isSuccess;
    }


}
