package com.example.vromia.e_nurseproject.Data;

/**
 * Created by chris on 29/9/2016.
 */

public class UserItem {


    private String userSName;
    private String userName;
    private String name;
    private String age;
    private String weight;
    private String height;
    private String doctorMame;
    private String doctorSName;
    private String sex;
    private int d_id;

    public UserItem(String userName, String name, String age, String weight, String height, String doctorMame, String doctorSName, String sex, String userSName, int d_id) {
        this.userName = userName;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.doctorMame = doctorMame;
        this.doctorSName = doctorSName;
        this.sex = sex;
        this.userSName = userSName;
        this.d_id = d_id;
    }

    public UserItem() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDoctorMame() {
        return doctorMame;
    }

    public void setDoctorMame(String doctorMame) {
        this.doctorMame = doctorMame;
    }

    public String getDoctorSName() {
        return doctorSName;
    }

    public void setDoctorSName(String doctorSName) {
        this.doctorSName = doctorSName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserSName() {
        return userSName;
    }

    public void setUserSName(String userSname) {
        this.userSName = userSname;
    }

    public int getD_id() {
        return d_id;
    }

    public void setD_id(int d_id) {
        this.d_id = d_id;
    }
}
