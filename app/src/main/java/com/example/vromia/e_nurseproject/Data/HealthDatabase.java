package com.example.vromia.e_nurseproject.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Vromia on 17/12/2014.
 */
public class HealthDatabase extends SQLiteOpenHelper {

    public static final String TABLE_DIET = "Diet";
    public static final String TABLE_WORKOUT = "Workout";
    public static final String TABLE_DOCTORS = "Doctors";
    public static final String TABLE_DRUGS = "Drugs";
    private static final int Database_Version = 1;
    private static final String Database_Name = "HealthDatabase";
    //Table Diet columns
    private static final String KEY_DIET_ID = "_id";
    private static final String KEY_DIET_AMOUNT = "amount";
    private static final String KEY_DIET_DATE = "date";
    private static final String KEY_DIET_TIME = "time";
    private static final String KEY_DIET_CATEGORY = "category";

    //Table Workout columns
    private static final String KEY_WORKOUT_ID = "_id";
    private static final String KEY_WORKOUT_DATE = "date";
    private static final String KEY_WORKOUT_CATEGORY = "category";
    private static final String KEY_WORKOUT_TIME = "workTime";
    private static final String KEY_WORKOUT_PERIOD = "period";


    //Table Drugs columns
    private static final String KEY_DRUGS_ID = "_id";
    private static final String KEY_DRUGS_CATEGORY = "category";
    private static final String KEY_DRUGS_DATE = "date";
    private static final String KEY_DRUGS_QUANTITY = "quantity";
    private static final String KEY_DRUGS_TIME = "period";
    private static final String KEY_DRUGS_CAUSE = "cause";

    //Table Doctors columns
    private static final String Key_Did = "id";
    private static final String Key_Dname = "name";
    private static final String Key_Dsurname = "surname";


    private static final String Create_Diet_Table = "CREATE TABLE " + TABLE_DIET + "(" + KEY_DIET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_DIET_CATEGORY + " TEXT NOT NULL," + KEY_DIET_DATE + " TEXT NOT NULL," + KEY_DIET_AMOUNT + " DOUBLE," + KEY_DIET_TIME + " TEXT NOT NULL" + ")";

    private static final String Create_Workout_Table = "CREATE TABLE " + TABLE_WORKOUT + "(" + KEY_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_WORKOUT_CATEGORY + " TEXT NOT NULL," + KEY_WORKOUT_DATE + " TEXT NOT NULL," + KEY_WORKOUT_TIME + " DOUBLE," + KEY_WORKOUT_PERIOD + " TEXT NOT NULL" + ")";

    private static final String Create_Doctor_Table = "CREATE TABLE " + TABLE_DOCTORS + "(" + Key_Did + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Key_Dname + " TEXT NOT NULL," + Key_Dsurname + " TEXT NOT NULL" + ")";


    private static final String Create_Drugs_Table = "CREATE TABLE " + TABLE_DRUGS + "(" + KEY_DRUGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_DRUGS_CATEGORY + " TEXT NOT NULL," + KEY_DRUGS_DATE + " TEXT NOT NULL," + KEY_DRUGS_QUANTITY + " DOUBLE," + KEY_DRUGS_TIME + " TEXT NOT NULL," +
            KEY_DRUGS_CAUSE + " TEXT NOT NULL" + ")";


    private SQLiteDatabase db;


    public HealthDatabase(Context context) {
        super(context, Database_Name, null, Database_Version);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Diet_Table);
        db.execSQL(Create_Drugs_Table);
        db.execSQL(Create_Workout_Table);
        db.execSQL(Create_Doctor_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUGS);

        onCreate(db);

    }

    public void InsertDiet(DietItem item) {
        Cursor c = db.query(TABLE_DIET, null, HealthDatabase.KEY_DIET_ID + "=?", new String[]{item.getId() + ""}, null, null, null);

        if (c != null) {
            if (c.getCount() >= 1) {
                c.close();
                return;
            }
            c.close();
        }
        ContentValues cv = new ContentValues();
        cv.put(KEY_DIET_AMOUNT, item.getAmount());
        cv.put(KEY_DIET_CATEGORY, item.getCategory());
        cv.put(KEY_DIET_DATE, item.getDate());
        cv.put(KEY_DIET_TIME, item.getTime());

        Log.i("Category", item.getCategory());
        Log.i("Amount", item.getAmount() + "");
        Log.i("Time", item.getTime());
        Log.i("Date", item.getDate());


        db.insert(TABLE_DIET, null, cv);

    }

    public void InsertWorkout(WorkoutItem item) {
        Cursor c = db.query(TABLE_WORKOUT, null, HealthDatabase.KEY_WORKOUT_ID + "=?", new String[]{item.getId() + ""}, null, null, null);

        if (c != null) {
            if (c.getCount() >= 1) {
                c.close();
                return;
            }
            c.close();
        }
        ContentValues cv = new ContentValues();
        cv.put(KEY_WORKOUT_CATEGORY, item.getCategory());
        cv.put(KEY_WORKOUT_PERIOD, item.getPeriodOfDay());
        cv.put(KEY_WORKOUT_DATE, item.getDate());
        cv.put(KEY_WORKOUT_TIME, item.getWorkTime() + "");

        db.insert(TABLE_WORKOUT, null, cv);
        Log.i("nikos", "workout inserted");
    }

    public void InsertDrugs(DrugsItem item) {
        Cursor c = db.query(TABLE_DRUGS, null, KEY_DRUGS_ID + "=?", new String[]{item.getId() + ""}, null, null, null);

        if (c != null) {
            if (c.getCount() >= 1) {
                c.close();
                return;
            }
            c.close();
        }
        ContentValues cv = new ContentValues();
        cv.put(KEY_DRUGS_CATEGORY, item.getCategory());
        cv.put(KEY_DRUGS_DATE, item.getDate());
        cv.put(KEY_DRUGS_QUANTITY, item.getQuantity());
        cv.put(KEY_DRUGS_TIME, item.getPeriodOfDay());
        cv.put(KEY_DRUGS_CAUSE, item.getCause());

        db.insert(TABLE_DRUGS, null, cv);
        Log.i("nikos", "Drugs inserted");
    }

    public void InsertDoctor(DoctorItem item) {
        Cursor c = db.query(TABLE_DOCTORS, null, HealthDatabase.Key_Did + "=?", new String[]{item.getId() + ""}, null, null, null);

        if (c != null) {
            if (c.getCount() >= 1) {
                c.close();
                return;
            }
            c.close();
        }

        ContentValues cv = new ContentValues();
        cv.put(Key_Did, item.getId());
        cv.put(Key_Dname, item.getName());
        cv.put(Key_Dsurname, item.getSurname());

        db.insert(TABLE_DOCTORS, null, cv);
    }


    public boolean dietTupleExists(String category, String mealTime) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DIET + " WHERE " + KEY_DIET_CATEGORY + " = " + "'" + category + "' AND " + KEY_DIET_TIME + " = '" + mealTime + "'", null);
        return cursor.getCount() > 0;

    }


    public boolean workoutTupleExists(String category, double duration, String date) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_WORKOUT + " WHERE " + KEY_WORKOUT_CATEGORY + " = " + "'" + category + "' AND " + KEY_WORKOUT_DATE + " = '" + date + "'"
                + " AND " + KEY_WORKOUT_TIME + " = " + duration, null);
        return cursor.getCount() > 0;

    }


    public String showDoctors() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DOCTORS, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Log.i("Doctor", cursor.getString(0) + " - " + cursor.getString(1) + " - " + cursor.getString(2));
                return cursor.getString(2);
            }
        }
        return null;
    }


    public ArrayList<String> getDoctorsFullName() {
        ArrayList<String> full_names = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DOCTORS, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String full_name = cursor.getString(1) + " " + cursor.getString(2);
                full_names.add(full_name);
            }
        }

        return full_names;
    }


    public Cursor getAllDietItems() {

        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DIET,
                null);
    }

    public Cursor getAllWorkoutItems() {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_WORKOUT,
                null);
    }


    public Cursor getAllDrugsItems() {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DRUGS,
                null);
    }

    public Cursor getDietByRecent() {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DIET
                        + " ORDER BY " + KEY_DIET_DATE + " DESC , " + KEY_DIET_TIME + " DESC , " + KEY_DIET_ID + " DESC",
                null);

        return cursor;
    }

    public Cursor getWorkoutByRecent() {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_WORKOUT
                        + " ORDER BY " + KEY_WORKOUT_DATE + " DESC , " + KEY_WORKOUT_ID + " DESC",
                null);

        return cursor;
    }

    public Cursor getDietByCategory(String category) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DIET
                        + " WHERE " + KEY_DIET_CATEGORY + " LIKE '" + category
                        + "' ORDER BY " + KEY_DIET_DATE + " DESC , " + KEY_DIET_ID + " DESC",
                null);

        return cursor;
    }

    public Cursor getWorkoutByCategory(String category) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_WORKOUT
                        + " WHERE " + KEY_WORKOUT_CATEGORY + " LIKE '" + category
                        + "' ORDER BY " + KEY_WORKOUT_DATE + " DESC , " + KEY_WORKOUT_ID + " DESC",
                null);

        return cursor;
    }

    //date param must be in format yyyy-mm-dd
    public Cursor getDietByDate(String date) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DIET
                        + " WHERE " + KEY_DIET_DATE + " LIKE '" + date
                        + "' ORDER BY " + KEY_DIET_ID + " DESC",
                null);

        return cursor;
    }

    //date param must be in format yyyy-mm-dd
    public Cursor getWorkoutByDate(String date) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_WORKOUT
                        + " WHERE " + KEY_WORKOUT_DATE + " LIKE '" + date
                        + "' ORDER BY " + KEY_WORKOUT_ID + " DESC",
                null);

        return cursor;
    }

    //date param must be in format yyyy-mm-dd
    public Cursor getWorkoutByPeriod(String period) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_WORKOUT
                        + " WHERE " + KEY_WORKOUT_PERIOD + " LIKE '" + period
                        + "' ORDER BY " + KEY_WORKOUT_ID + " DESC",
                null);

        return cursor;
    }

    public Cursor getDistDrugs() {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DRUGS + " GROUP BY " + KEY_DRUGS_CATEGORY + " , " + KEY_DRUGS_CAUSE,
                null);
    }


    public Cursor getDrugsByCategory(String category) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DRUGS
                        + " WHERE " + KEY_DRUGS_CATEGORY + " LIKE '" + category
                        + "' ORDER BY " + KEY_DRUGS_DATE + " DESC , " + KEY_DRUGS_ID + " DESC",
                null);

        return cursor;
    }

    //date param must be in format yyyy-mm-dd
    public Cursor getDrugsByDate(String date) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DRUGS
                        + " WHERE " + KEY_DRUGS_DATE + " LIKE '" + date
                        + "' ORDER BY " + KEY_DRUGS_ID + " DESC",
                null);

        return cursor;
    }

    //date param must be in format yyyy-mm-dd
    public Cursor getDrugsByPeriod(String period) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DRUGS
                        + " WHERE " + KEY_DRUGS_TIME + " LIKE '" + period
                        + "' ORDER BY " + KEY_DRUGS_ID + " DESC",
                null);

        return cursor;
    }


    public Cursor getDrugsByRecent() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DRUGS
                        + " ORDER BY " + KEY_DRUGS_DATE + " DESC , " + KEY_DRUGS_ID + " DESC",
                null);

        return cursor;
    }

    public void UpdateDoctor(DoctorItem item) {
        ContentValues cv = new ContentValues();
        cv.put(Key_Did, item.getId());
        cv.put(Key_Dname, item.getName());
        cv.put(Key_Dsurname, item.getSurname());
        db.updateWithOnConflict(TABLE_DOCTORS, cv, Key_Did + "=?", new String[]{item.getId() + ""}, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updatWorkOut(WorkoutItem item) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_WORKOUT_ID, item.getId());
        cv.put(KEY_WORKOUT_CATEGORY, item.getCategory());
        cv.put(KEY_WORKOUT_PERIOD, item.getPeriodOfDay());
        cv.put(KEY_WORKOUT_DATE, item.getDate());
        cv.put(KEY_WORKOUT_TIME, item.getWorkTime() + "");
        db.updateWithOnConflict(TABLE_WORKOUT, cv, KEY_WORKOUT_ID + "=?", new String[]{item.getId() + ""}, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateDiet(DietItem item) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_DIET_ID, item.getId());
        cv.put(KEY_DIET_AMOUNT, item.getAmount());
        cv.put(KEY_DIET_CATEGORY, item.getCategory());
        cv.put(KEY_DIET_DATE, item.getDate());
        cv.put(KEY_DIET_TIME, item.getTime());
        db.updateWithOnConflict(TABLE_DIET, cv, KEY_WORKOUT_ID + "=?", new String[]{item.getId() + ""}, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public int getLastId(String tableName) {
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            int id = c.getInt(0);
            c.close();
            return id;
        }
        if (c != null) {
            c.close();
            return -1;
        }
        return -1;
    }

    public void updatDrugs(DrugsItem item) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_DIET_ID, item.getId());
        cv.put(KEY_DRUGS_CATEGORY, item.getCategory());
        cv.put(KEY_DRUGS_DATE, item.getDate());
        cv.put(KEY_DRUGS_QUANTITY, item.getQuantity());
        cv.put(KEY_DRUGS_TIME, item.getPeriodOfDay());
        cv.put(KEY_DRUGS_CAUSE, item.getCause());
        db.updateWithOnConflict(TABLE_DRUGS, cv, KEY_WORKOUT_ID + "=?", new String[]{item.getId() + ""}, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public void rectreateDB() {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUGS);

        onCreate(db);
    }
}


