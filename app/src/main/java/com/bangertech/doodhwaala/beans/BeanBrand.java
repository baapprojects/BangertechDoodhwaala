package com.bangertech.doodhwaala.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 9/29/2015.
 */
public class BeanBrand {
    private String brand_id="";
    private String brand_name="";

    public String getBrandType() {
        return brand_type;
    }

    public void setBrandType(String brandType) {
        this.brand_type = brandType;
    }

    private String brand_type="";
    private List<BeanProduct> lstBeanProduct=new ArrayList<BeanProduct>();

    public BeanBrand()
    {
        lstBeanProduct.clear();
    }
    public String getBrandId() {
        return brand_id;
    }
    public void setBrandId(String brand_id) {
        this.brand_id = brand_id;
    }


    public String getBrandName() {
        return brand_name;
    }
    public void setBrandName(String brand_name) {
        this.brand_name = brand_name;
    }

    public void addProduct(BeanProduct beanProduct)
    {
        lstBeanProduct.add(beanProduct);
    }
    public List<BeanProduct> getProducts()
    {
        return lstBeanProduct;
    }

}
