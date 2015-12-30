package com.bangertech.doodhwaala.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangertech.doodhwaala.cinterfaces.ISelectedProduct;
import com.bangertech.doodhwaala.beans.BeanFilteredProduct;
import com.bangertech.doodhwaala.cinterfaces.ISelectedProduct;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.utils.CUtils;

import java.util.List;

/**
 * Created by annutech on 10/8/2015.
 */
public class FiltersAppliedProductAdapter extends BaseAdapter {
    private Activity activity;
    private ISelectedProduct iSelectedProduct;

    private List<BeanFilteredProduct> _filtersAppliedProductList=null;
    public FiltersAppliedProductAdapter(Activity activity,List<BeanFilteredProduct> filtersAppliedProductList)
    {
        this.activity = activity;
        iSelectedProduct=(ISelectedProduct)activity;
        this._filtersAppliedProductList=filtersAppliedProductList;
    }

    @Override
    public int getCount() {
        return _filtersAppliedProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return _filtersAppliedProductList.get(position);
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

        BeanFilteredProduct beanFilteredProduct= _filtersAppliedProductList.get(position);
        textViewProductName.setTypeface(CUtils.LightTypeFace(activity));
        TextViewProductPrice.setTypeface(CUtils.LightTypeFace(activity));
        textViewProductName.setText(beanFilteredProduct.getProductName());
        TextViewProductPrice.setText("Rs "+beanFilteredProduct.getPrice());

        view.setTag(position);
        CUtils.downloadImageFromServer(parent.getContext(), imageViewProduct, beanFilteredProduct.getProductImage());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BeanFilteredProduct beanFilteredProduct=_filtersAppliedProductList.get(Integer.parseInt(v.getTag().toString()));
                    iSelectedProduct.onSelectProduct(beanFilteredProduct.getProductId(),beanFilteredProduct.getProductMappingId());
                }
            });

        return view;
    }
}
