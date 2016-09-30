package com.example.vromia.e_nurseproject.Data;

/**
 * Created by Vromia on 17/12/2014.
 */
public class WorkoutItem {

    private String category;
    private String date;
    private String periodOfDay;
    private double workTime;
    private int id;

    public WorkoutItem() {
    }

    public WorkoutItem(String category, String date, double workTime, String periodOfDay, int id) {

        this.category = category;
        this.date = date;
        this.workTime = workTime;
        this.periodOfDay = periodOfDay;
        this.id = id;

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

    public double getWorkTime() {
        return workTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
