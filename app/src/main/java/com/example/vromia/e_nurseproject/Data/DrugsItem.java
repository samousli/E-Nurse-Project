package com.example.vromia.e_nurseproject.Data;

/**
 * Created by Vromia on 17/12/2014.
 */
public class DrugsItem {

    private String category;
    private String date;
    private String periodOfDay;
    private double quantity;
    private String cause;
    private int id;

    public DrugsItem(String category, String date, double quantity, String periodOfDay, String cause, int id) {

        this.category = category;
        this.date = date;
        this.quantity = quantity;
        this.periodOfDay = periodOfDay;
        this.cause=cause;
        this.id = id;


    }

    public DrugsItem() {
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getPeriodOfDay() {
        return periodOfDay;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getCause(){
        return  cause;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}