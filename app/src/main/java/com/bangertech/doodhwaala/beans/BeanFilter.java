package com.bangertech.doodhwaala.beans;

/**
 * Created by annutech on 10/1/2015.
 */
public class BeanFilter {
    private String _id="";
    private String _name="";
    private String _tagType="";
    private boolean isChecked=false;

    public String getTagType() {
        return _tagType;
    }

    public void setTagType(String tagType) {
        this._tagType = tagType;
    }



    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
    public String getId() {
        return _id;
    }
    public void setId(String id) {
        this._id = id;
    }


    public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }
}
