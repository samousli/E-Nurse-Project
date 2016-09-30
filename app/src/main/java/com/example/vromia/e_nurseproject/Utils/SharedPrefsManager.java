package com.example.vromia.e_nurseproject.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Orion on 18/12/2014.
 */
public class SharedPrefsManager {
    //the Shared Preferences file name
    private static final String SHARED_PREFS = "UserDetailSharedPrefs";

    //Shared Preferences attributes
    private static final String PREFS_ONOMA = "onoma";
    private static final String PREFS_SURNAME = "surname";
    private static final String PREFS_ILIKIA = "ilikia";
    private static final String PREFS_YPSOS = "ypsos";
    private static final String PREFS_BAROS = "baros";
    private static final String PREFS_ISTORIKOPATHISEON = "istorikoPathiseon";
    private static final String PREFS_USERNAME="username";
    private static final String PREFS_PASSWORD="password";
    private static final String PREFS_FYLO = "fylo";
    private static final String PREFS_START_OFF_APP="start of app";
    private static final String PREFS_USERID="userID";
    private static final String PREFS_ROUTINE = "routineworkout";
    private static final String PREFS_D_ID = "selected_doctor_id";

    //the SharedPreferences and Editor objects
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    //constructor
    public SharedPrefsManager(Context context) {
        prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    //commit all changes done to the editor
    public void commit() {
        editor.commit();
    }

    //open the editor object for commiting changes
    public void startEditing() {
        editor = prefs.edit();
    }




    /**
     * Below are the setters and getters for each attribute
     *
     */

    public int getPrefsUserID(){
        return prefs.getInt(PREFS_USERID,-1);
    }

    public void setPrefsUserID(int userID) {
        editor.putInt(PREFS_USERID, userID);
    }

    public boolean getPrefsStart(){
        return prefs.getBoolean(PREFS_START_OFF_APP,false);
    }

    public void setPrefsStart(boolean start) {
        editor.putBoolean(PREFS_START_OFF_APP, start);
    }

    public String getPrefsSurname(){
        return prefs.getString(PREFS_SURNAME, "");
    }

    public void setPrefsSurname(String surname) {
        editor.putString(PREFS_SURNAME, surname);
    }

    public String getPrefsUsername(){
        return prefs.getString(PREFS_USERNAME, "");
    }

    public void setPrefsUsername(String username) {
        editor.putString(PREFS_USERNAME, username);
    }

    public String getPrefsPassword(){
        return prefs.getString(PREFS_PASSWORD,"");
    }

    public void setPrefsPassword(String password) {
        editor.putString(PREFS_PASSWORD, password);
    }

    public String getPrefsOnoma() {
        return prefs.getString(PREFS_ONOMA, "");
    }

    public void setPrefsOnoma(String onoma) {
        editor.putString(PREFS_ONOMA, onoma);
    }

    public int getPrefsIlikia() {
        return prefs.getInt(PREFS_ILIKIA, 0);
    }

    public void setPrefsIlikia(int ilikia) {
        editor.putInt(PREFS_ILIKIA, ilikia);
    }

    public float getYpsos() {
        return prefs.getFloat(PREFS_YPSOS, 0);
    }

    public float getPrefsBaros() {
        return prefs.getFloat(PREFS_BAROS, 0);
    }

    public void setPrefsBaros(float baros) {
        editor.putFloat(PREFS_BAROS, baros);
    }

    public String getPrefsIstorikoPathiseon() {
        return prefs.getString(PREFS_ISTORIKOPATHISEON, "");
    }

    public void setPrefsIstorikoPathiseon(String istorikoPthiseon) {
        editor.putString(PREFS_ISTORIKOPATHISEON, istorikoPthiseon);
    }

    public String getPrefsFylo() {
        return prefs.getString(PREFS_FYLO, "");
    }

    public void setPrefsFylo(String fylo) {
        editor.putString(PREFS_FYLO, fylo);
    }

    public void setPrefsYpsos(float ypsos) {
        editor.putFloat(PREFS_YPSOS, ypsos);
    }

    public Long getPrefsRoutine() {
        return prefs.getLong(PREFS_ROUTINE, 0);
    }

    public void setPrefsRoutine(long wo){
        editor.putLong(PREFS_ROUTINE,wo); }

    public int getPrefsD_id() {
        return prefs.getInt(PREFS_D_ID, 0);
    }

    public void setPrefsD_id(int prefsD_id) {
        editor.putInt(PREFS_D_ID, prefsD_id);
    }
}
