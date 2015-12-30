package com.bangertech.doodhwaala.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangertech.doodhwaala.activity.Home;
import com.bangertech.doodhwaala.beans.BeanBrand;

import java.util.List;

import com.bangertech.doodhwaala.beans.BeanProduct;
import com.bangertech.doodhwaala.cinterfaces.IBrandAllProduct;
import com.bangertech.doodhwaala.cinterfaces.ISelectedProduct;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

/**
 * Created by annutech on 9/30/2015.
 */
public class MostSellingBrandAdapter extends RecyclerView.Adapter<MostSellingBrandViewHolder>{
    private List<BeanBrand> lstMostSellingProducts;
    private Context context;
    private IBrandAllProduct iBrandAllProduct;
    private ISelectedProduct iSelectedProduct;
    public MostSellingBrandAdapter(Context context, Fragment fragment,List<BeanBrand> lstMostSellingProducts)
    {
        this.context = context;
        this.lstMostSellingProducts = lstMostSellingProducts;
        iBrandAllProduct=(IBrandAllProduct)fragment;
        iSelectedProduct=(ISelectedProduct)fragment;

    }
    @Override
    public MostSellingBrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.lay_products, parent, false);
        return new MostSellingBrandViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MostSellingBrandViewHolder holder, int position) {


        BeanBrand beanBrand= this.lstMostSellingProducts.get(position);
        holder.BrandName.setTypeface(CUtils.RegularTypeFace(context));
        holder.BrandName.setText(beanBrand.getBrandName());
        holder.Products.setAdapter(new ProductAdapter(beanBrand.getProducts()));
        holder.ViewAll.setTag(String.valueOf(position));

        holder.ViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.printLog("ViewAll-Clicked",v.getTag().toString(), ConstantVariables.LOG_TYPE.ERROR);
                iBrandAllProduct.onSelectViewAllProduct( MostSellingBrandAdapter.this.lstMostSellingProducts.get(Integer.parseInt(v.getTag().toString())));
            }
        });

    }

    @Override
    public int getItemCount() {
        return  this.lstMostSellingProducts.size();
    }


    class ProductAdapter extends BaseAdapter
    {

        private List<BeanProduct> lstBeanProduct;

        public ProductAdapter(List<BeanProduct> lstBeanProduct)
        {
            this.lstBeanProduct=lstBeanProduct;
        }
        @Override
        public int getCount() {
            return  this.lstBeanProduct.size();
        }

        @Override
        public Object getItem(int position) {
            return  this.lstBeanProduct.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.row_brand_product_card, parent, false);


            TextView textViewProductName = (TextView) view.findViewById(R.id.textViewProductName);
            TextView TextViewProductPrice = (TextView) view.findViewById(R.id.textViewProductPrice);
            ImageView imageViewProduct= (ImageView) view.findViewById(R.id.imageViewProduct);

            textViewProductName.setTypeface(CUtils.LightTypeFace(context));
            TextViewProductPrice.setTypeface(CUtils.LightTypeFace(context));

            BeanProduct beanProduct= this.lstBeanProduct.get(position);
            textViewProductName.setText(beanProduct.getProductName());
            TextViewProductPrice.setText("Rs "+beanProduct.getPrice());
            view.setTag(position);

            CUtils.downloadImageFromServer(parent.getContext(), imageViewProduct, beanProduct.getProductImage());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BeanProduct beanProduct = lstBeanProduct.get(Integer.parseInt(v.getTag().toString()));
                    iSelectedProduct.onSelectProduct(beanProduct.getProductId(), beanProduct.getProductMappingId());
                }
            });


            return view;
        }
    }
}
