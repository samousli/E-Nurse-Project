package com.example.vromia.e_nurseproject.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.vromia.e_nurseproject.Data.DietItem;
import com.example.vromia.e_nurseproject.Data.GridItem;
import com.example.vromia.e_nurseproject.Data.HealthDatabase;
import com.example.vromia.e_nurseproject.Data.WorkoutItem;
import com.example.vromia.e_nurseproject.R;
import com.example.vromia.e_nurseproject.Utils.GridAdapter;
import com.example.vromia.e_nurseproject.Utils.HttpHandler;
import com.example.vromia.e_nurseproject.Utils.SharedPrefsManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Vromia on 2/6/2015.
 */
public class HomeActivity extends Activity {

    private GridView gridView;
    private ArrayList<GridItem> items;

    private HealthDatabase db;

    private static String url = "http://nikozisi.webpages.auth.gr/enurse/get_data.php";
    private HttpHandler httpHandler;
    private ArrayList<DietItem> dietItems;
    private ArrayList<WorkoutItem> workoutItems;

    private boolean updateDiet = false, updateWorkout = false;

    private static final String TAG_DIET = "nutrition";
    private static final String TAG_MEALTIME = "mealTime";
    private static final String TAG_DATE = "date";
    private static final String TAG_MEAL = "meal";

    private static final String TAG_WORKOUT = "exercise";
    private static final String TAG_TYPE = "type";
    private static final String TAG_DURATION = "duration";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (haveNetworkConnection()) {

            httpHandler = new HttpHandler();
            dietItems = new ArrayList<>();
            workoutItems = new ArrayList<>();
            db = new HealthDatabase(HomeActivity.this);

            new SyncWebData().execute();


        }


        gridView = (GridView) findViewById(R.id.gridview);
        items = new ArrayList<>();

        items.add(new GridItem(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_food), getString(R.string.diet)));
        items.add(new GridItem(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_excercise), getString(R.string.workout)));
        items.add(new GridItem(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_drugs), getString(R.string.drugs)));
        items.add(new GridItem(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_profile), getString(R.string.profile)));
        items.add(new GridItem(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_history), getString(R.string.history)));
        items.add(new GridItem(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_drugs_history), getString(R.string.drug_history)));
        items.add(new GridItem(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_settings), getString(R.string.settings)));
        items.add(new GridItem(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_exit), getString(R.string.exit)));


        GridAdapter adapter = new GridAdapter(HomeActivity.this, R.layout.grid_item_menu, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(HomeActivity.this, DietActivity.class));
                    if (PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getBoolean("key_animations", false))
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else if (position == 1) {
                    startActivity(new Intent(HomeActivity.this, WorkoutActivity.class));
                    if (PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getBoolean("key_animations", false))
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else if (position == 2) {
                    startActivity(new Intent(HomeActivity.this, DrugsActivity.class));
                    if (PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getBoolean("key_animations", false))
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else if (position == 3) {
                    Intent intent = new Intent(HomeActivity.this, UserDetailsActivity.class);
                    intent.putExtra("Menu", "menu");
                    startActivity(intent);
                    if (PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getBoolean("key_animations", false))
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else if (position == 4) {
                    startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
                    if (PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getBoolean("key_animations", false))
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else if (position == 5) {
                    startActivity(new Intent(HomeActivity.this, DrugsHistoryActivity.class));
                    if (PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getBoolean("key_animations", false))
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else if (position == 6) {
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                    if (PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getBoolean("key_animations", false))
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else if (position == 7) {
                    finish();
                }
            }
        });

    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;

    }


    class SyncWebData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            SharedPrefsManager manager = new SharedPrefsManager(HomeActivity.this);
            int userID = manager.getPrefsUserID();

            RequestParams params = new RequestParams("userID", userID);

            HttpHandler.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    try {
                        JSONArray dietArray = response.getJSONArray(TAG_DIET);
                        JSONArray workoutArray = response.getJSONArray(TAG_WORKOUT);

                        Cursor dietCursor = db.getAllDietItems();
                        Cursor workoutCursor = db.getAllWorkoutItems();
                        if (dietCursor.getCount() != dietArray.length()) {

                            updateDiet = true;
                            for (int i = 0; i < dietArray.length(); i++) {
                                JSONObject c = dietArray.getJSONObject(i);
                                String meal = c.getString(TAG_MEAL);
                                String date = c.getString(TAG_DATE);
                                String mealTime = c.getString(TAG_MEALTIME);

                                String dateTokens[] = date.split(" ");
                                String mealTimeTokens[] = mealTime.split(":");

                                DietItem dietItem = new DietItem(meal, dateTokens[0], 1,
                                        mealTimeTokens[0] + ":" + mealTimeTokens[1]);
                                dietItems.add(dietItem);
                            }
                        }

                        if (workoutCursor.getCount() != workoutArray.length()) {
                            updateWorkout = true;
                            for (int i = 0; i < workoutArray.length(); i++) {
                                JSONObject c = workoutArray.getJSONObject(i);

                                double duration = c.getDouble(TAG_DURATION);
                                String date = c.getString(TAG_DATE);
                                String type = c.getString(TAG_TYPE);

                                String dateTokens[] = date.split(" ");
                                WorkoutItem item = new WorkoutItem(type, dateTokens[0],
                                        duration, dateTokens[1]);
                                workoutItems.add(item);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            boolean update = false;
            if (updateDiet) {
                for (int i = 0; i < dietItems.size(); i++) {
                    DietItem item = dietItems.get(i);
                    if (!db.dietTupleExists(item.getCategory(), item.getTime())) {
                        db.InsertDiet(item);
                        update = true;
                    }
                }
            }

            if (updateWorkout) {
                for (int i = 0; i < workoutItems.size(); i++) {
                    WorkoutItem workoutItem = workoutItems.get(i);
                    if (!db.workoutTupleExists(workoutItem.getCategory(), workoutItem.getWorkTime(), workoutItem.getDate())) {
                        db.InsertWorkout(workoutItem);
                        update = true;
                    }
                }
            }

            if (update) {
                Toast.makeText(HomeActivity.this, getString(R.string.dataUpdated), Toast.LENGTH_LONG).show();
            }

            db.close();
        }
    }


}
