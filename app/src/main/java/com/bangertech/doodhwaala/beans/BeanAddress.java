package com.bangertech.doodhwaala.beans;

/**
 * Created by annutech on 10/14/2015.
 */
public class BeanAddress {
    private String address_id;
    private String full_address;
    boolean default_address=false;
    public String getAddressId() {
        return address_id;
    }

    public void setAddressId(String address_id) {
        this.address_id = address_id;
    }



    public String getFullAddress() {
        return full_address;
    }

    public void setFullAddress(String full_address) {
        this.full_address = full_address;
    }



    public boolean isDefaultAddress() {
        return default_address;
    }

    public void setDefaultAddress(boolean default_address) {
        this.default_address = default_address;
    }


}
