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
import android.widget.TextView;

import com.bangertech.doodhwaala.activity.FilterProduct;
import com.bangertech.doodhwaala.activity.FiltersAppliedByFilterProduct;
import com.bangertech.doodhwaala.activity.Home;
import com.bangertech.doodhwaala.activity.ProductDetail;
import com.bangertech.doodhwaala.adapter.MostSellingBrandAdapter;
import com.bangertech.doodhwaala.beans.BeanBrand;
import com.bangertech.doodhwaala.beans.BeanProductType;
import com.bangertech.doodhwaala.cinterfaces.IBrandAllProduct;
import com.bangertech.doodhwaala.cinterfaces.ISelectedProduct;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import java.util.ArrayList;
import java.util.List;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class MilkbarFragment extends Fragment /*implements AsyncResponse*/ implements IBrandAllProduct,ISelectedProduct {

    private List<BeanProductType> lstBeanProductType=null;
    private List<BeanBrand> lstProducts=null;
    private MyAsynTaskManager myAsyncTask;
    private String userId="1";
    private GridView gridProductType;
    private View view;
    private ProductTypeAdapter productTypeAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton     fabFilterProduct;
    private ImageView search;

    public static MilkbarFragment newInstance() {

        return new MilkbarFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getActivity(),"onActivityResult",Toast.LENGTH_SHORT).show();
        if(requestCode==ConstantVariables.SUB_ACTIVITY_FILTER_OPENED_ON_MILKBAR && resultCode== Activity.RESULT_OK) {
            startActivity(new Intent(getActivity(), FiltersAppliedByFilterProduct.class).putExtras(data));
            getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstBeanProductType=new ArrayList<BeanProductType>();
        lstProducts=new ArrayList<BeanBrand>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_milkbar, container, false);
        search=(ImageView) view.findViewById(R.id.searchProductType);
        gridProductType=(GridView) view.findViewById(R.id.gridProductType);
        fabFilterProduct=(FloatingActionButton)view.findViewById(R.id.fabFilterProduct);
        mRecyclerView= (RecyclerView)view.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        fabFilterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivityForResult(new Intent(getActivity(), FilterProduct.class), ConstantVariables.SUB_ACTIVITY_FILTER_OPENED_ON_MILKBAR);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
             /*  Toast.makeText(getActivity(),"FAB clicked",Toast.LENGTH_SHORT).show();*/
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(gridProductType.getCount()==0)
            initProductType();
        //initProducts();

    }
public void reDrawFragment(List<BeanProductType> lstBeanProductType,List<BeanBrand> lstProducts)
{
    this.lstBeanProductType=lstBeanProductType;
    this.lstProducts=lstProducts;

   // Toast.makeText(getActivity(),String.valueOf(this.lstProducts.size()),Toast.LENGTH_SHORT).show();
    initProductType();
    //initProducts();
}

  private void initProducts()
  {

      if (this.lstProducts.size()>0)
      {

          mAdapter=new MostSellingBrandAdapter(getActivity(),this,this.lstProducts);
          mRecyclerView.setAdapter(mAdapter);
          mRecyclerView.setVisibility(View.VISIBLE);
      }
     /* if (this.lstProducts.size()>0)
          mRecyclerView.setAdapter(new BranProductsListAdapter());
*/
      //productsFragment.reDrawFragment(this.lstProducts.get(0));
 }
 private void initProductType()
 {

     if(this.lstBeanProductType.size()>0) {
         productTypeAdapter = new ProductTypeAdapter(getActivity(),this,this.lstBeanProductType);
         gridProductType.setAdapter(productTypeAdapter);
         gridProductType.setVisibility(View.VISIBLE);
     }
     initProducts();
 }
    private void setOnFilter(int position )
    {

        BeanProductType beanProductType=lstBeanProductType.get(position);
      /*
        ((Home) getActivity()).addFilterProductTypeOnToolBar(beanProductType);
        lstBeanProductType.remove(position);
        productTypeAdapter.notifyDataSetChanged();*/

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
            startActivity(new Intent(getActivity(), FiltersAppliedByFilterProduct.class).putExtras(data));
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
        intent.putExtra(ConstantVariables.PRODUCT_ID_KEY,productId);
        intent.putExtra(ConstantVariables.PRODUCT_MAPPING_ID_KEY,productMappingId);
        startActivity(intent);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            grid = inflater.inflate(R.layout.row_product_type, null);
            BeanProductType beanType= this.lstProductsType.get(position);
            TextView textView = (TextView) grid.findViewById(R.id.textViewProduct);
            textView.setText(beanType.getTagName());
            textView.setTag(position);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(), String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
                    setOnFilter(Integer.valueOf(v.getTag().toString()));
                }
            });
            return grid;
        }
    }


}
