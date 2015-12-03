package com.bangertech.doodhwaala.beans;

/**
 * Created by annutech on 9/29/2015.
 */
public class BeanProduct {
    private String product_mapping_id="";
    private String product_id="";
    private String product_name="";
    private String price="";
    private String product_image="";


    public String getProductMappingId() {
        return product_mapping_id;
    }

    public void setProductMappingId(String product_mapping_id) {
        this.product_mapping_id = product_mapping_id;
    }



    public String getProductId() {
        return product_id;
    }

    public void setProductId(String product_id) {
        this.product_id = product_id;
    }



    public String getProductName() {
        return product_name;
    }

    public void setProductName(String product_name) {
        this.product_name = product_name;
    }



    public String getProductImage() {
        return product_image;
    }

    public void setProductImage(String product_image) {
        this.product_image = product_image;
        if(this.product_image.length()>0)
            this.product_image=this.product_image.replace("\\/","/");
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
