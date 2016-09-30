package com.example.vromia.e_nurseproject.Data;

/**
 * Created by Vromia on 17/12/2014.
 */
public class DietItem {

    private String category;
    private String date;
    private double amount;
    private String time;
    private int id;

    public DietItem() {
    }

    public DietItem(String category, String date, double amount, String time, int id) {
        this.category=category;
        this.date=date;
        this.amount=amount;
        this.time=time;
        this.id = id;

    }

    public String getCategory(){
        return category;
    }

    public String getDate(){
        return date;
    }

    public double getAmount(){
        return amount;
    }

    public String getTime(){
        return time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
