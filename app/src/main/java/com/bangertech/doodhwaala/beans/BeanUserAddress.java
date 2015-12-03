package com.bangertech.doodhwaala.beans;

/**
 * Created by annutech on 9/23/2015.
 */
public class BeanUserAddress {
    private  String address_id="";
    private String user_id="";
    private String city="";
    private String locality="";
    private String flat_number="";
    private String building_or_society_name="";
    private String street_details="";
    private String landmark="";
    private boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getAddressId() {
        return address_id;
    }

    public void setAddressId(String address_id) {
        this.address_id = address_id;
    }



    public String getUserId() {

        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }



    public String getFlatNumber() {
        return flat_number;
    }

    public void setFlatNumber(String flat_number) {
        this.flat_number = flat_number;
    }



    public String getBuildingOrSocietyName() {
        return building_or_society_name;
    }

    public void setBuildingOrSocietyName(String building_or_society_name) {
        this.building_or_society_name = building_or_society_name;
    }



    public String getStreetDetails() {
        return street_details;
    }

    public void setStreetDetails(String street_details) {
        this.street_details = street_details;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }




}
