package com.bangertech.doodhwaala.beans;

/**
 * Created by annutech on 10/8/2015.
 */
public class BeanFilteredProduct {
    private String product_mapping_id="";
    private String product_id="";
    private String product_name="";
    private String price="";
    private String product_image="";
    //ADDED MORE
    private String brand_id="";
    private String product_type_id="";
    private String description="";
    private String packaging_id="";
    private String quantity_id="";
    private String available="";
    private String no_of_purchases="";

    public String getBrandId() {
        return brand_id;
    }

    public void setBrandId(String brandId) {
        this.brand_id = brandId;
    }

    public String getProductTypeId() {
        return product_type_id;
    }

    public void setProductTypeId(String productTypeId) {
        this.product_type_id = productTypeId;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackagingId() {
        return packaging_id;
    }

    public void setPackagingId(String packagingId) {
        this.packaging_id = packagingId;
    }

    public String getQuantityId() {
        return quantity_id;
    }

    public void setQuantityId(String quantityId) {
        this.quantity_id = quantityId;
    }

    public String getNoOfPurchases() {
        return no_of_purchases;
    }

    public void setNoOfPurchases(String noOfPurchases) {
        this.no_of_purchases = noOfPurchases;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

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
