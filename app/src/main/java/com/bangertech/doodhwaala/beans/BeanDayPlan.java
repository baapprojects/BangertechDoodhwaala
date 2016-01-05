package com.bangertech.doodhwaala.beans;

/**
 * Created by Newdream on 24-Oct-15.
 */
public class BeanDayPlan {
    private String plan_id="";
    private String date_id="";
    private String product_name="";
    private String quantity="";
    private boolean paused=false;
    private boolean date=false;
    private String image="";
    private String Fpaused="";
    private String freq_id = "";
    private boolean flagPaused=false;

    public boolean isShowChangeOrPausePlan() {
        return showChangeOrPausePlan;
    }

    public void setShowChangeOrPausePlan(boolean showChangeOrPausePlan) {
        this.showChangeOrPausePlan = showChangeOrPausePlan;
    }

    private boolean showChangeOrPausePlan=true;
    public String getPlanId() {
        return plan_id;
    }

    public void setPlanId(String plan_id) {
        this.plan_id = plan_id;
    }
    public String getDateId() {
        return date_id;
    }

    public void setDateId(String date_id) {
        this.date_id = date_id;
    }

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        if(this.image.length()>0)
            this.image=this.image.replace("\\/","/");

    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public String getFrequencyId() {
        return freq_id;
    }

    public void setFrequencyId(String date) {
        this.freq_id = freq_id;
    }

    public boolean isDateAvailable() {
        return date;
    }

    public void setDateAvailable(boolean date) {
        this.date = date;
    }

    public String getFlagPaused() {
        return Fpaused;
    }

    public void setFlagPaused(String Fpaused) {
        this.Fpaused = Fpaused;
    }
}
