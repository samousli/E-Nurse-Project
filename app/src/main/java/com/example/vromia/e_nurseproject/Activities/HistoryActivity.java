package com.example.vromia.e_nurseproject.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.vromia.e_nurseproject.Data.HealthDatabase;
import com.example.vromia.e_nurseproject.R;
import com.example.vromia.e_nurseproject.Utils.HistoryAdapter;

import java.util.Calendar;


/**
 * Created by Vromia on 17/12/2014.
 */
public class HistoryActivity extends FragmentActivity {

    FragmentManager manager;
    private ListView lv;
    private HealthDatabase db;
    private Cursor cursor;
    private HistoryAdapter adapter;
    private AlertDialog dialog = null;
    private Menu menu;
    private boolean isDiet = true;
    private CalendarDatePickerDialogFragment.OnDateSetListener listener = new CalendarDatePickerDialogFragment.OnDateSetListener() {
        @Override
        public void onDateSet(CalendarDatePickerDialogFragment calendarDatePickerDialog, int i, int i2, int i3) {
            String month, day;
            i2++;
            if (i2 < 10)
                month = "0" + i2;
            else
                month = String.valueOf(i2);
            if (i3 < 10)
                day = "0" + i3;
            else
                day = String.valueOf(i3);
            String date = i + "-" + month + "-" + day;
            if (isDiet) {
                cursor = db.getDietByDate(date);
                refreshList(cursor);
            } else {
                cursor = db.getWorkoutByDate(date);
                refreshList(cursor);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lv = (ListView) findViewById(R.id.lvHistory);

        db = new HealthDatabase(HistoryActivity.this);
        cursor = db.getDietByRecent();

//        startManagingCursor(cursor);
        isDiet = true;
        adapter = new HistoryAdapter(getApplicationContext(), cursor, isDiet);
        lv.setAdapter(adapter);

        manager = getSupportFragmentManager();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.diet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] categories;
        Calendar c;
        CalendarDatePickerDialogFragment cdate;

        switch (id) {
            case R.id.toggle:
                isDiet = !isDiet;
                if (isDiet) {
                    menu.clear();
                    getMenuInflater().inflate(R.menu.diet_menu, menu);
                    cursor = db.getDietByRecent();
                    adapter = new HistoryAdapter(HistoryActivity.this, cursor, isDiet);
                    lv.setAdapter(adapter);
                } else {
                    menu.clear();
                    getMenuInflater().inflate(R.menu.workout_menu, menu);
                    cursor = db.getWorkoutByRecent();
                    adapter = new HistoryAdapter(HistoryActivity.this, cursor, isDiet);
                    lv.setAdapter(adapter);
                }
                break;
            case R.id.filtersDietCategory:
                categories = getResources().getStringArray(R.array.foodNames);

                builder.setTitle("Κατηγορίες");

                builder.setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {

                        String chosenCategory = categories[item];
                        cursor = db.getDietByCategory(chosenCategory);
                        refreshList(cursor);
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.filtersDietRecent:
                cursor = db.getDietByRecent();
                refreshList(cursor);
                break;
            case R.id.filtersDietDate:
                c = Calendar.getInstance();
                cdate = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(listener)
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                        .setDoneText("Yes")
                        .setCancelText("No")
                        .setThemeCustom(R.style.BetterPickersStyle);
                cdate.show(manager, "Tag");
                break;
            case R.id.filtersWorkoutCategory:
                categories = getResources().getStringArray(R.array.workoutNames);

                builder.setTitle(getString(R.string.categories));

                builder.setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {

                        String chosenCategory = categories[item];
                        cursor = db.getWorkoutByCategory(chosenCategory);
                        refreshList(cursor);
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.filtersWorkoutDate:
                c = Calendar.getInstance();
                cdate = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(listener)
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                        .setDoneText("Yes")
                        .setCancelText("No");
                cdate.show(manager, "Tag");
                break;
            case R.id.filtersWorkoutPeriod:
                final String periods[] = new String[]{
                        getString(R.string.morning),
                        getString(R.string.midday),
                        getString(R.string.evening)
                };

                builder.setTitle(getString(R.string.time_period));

                builder.setItems(periods, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {

                        String chosenPeriod = periods[item];
                        cursor = db.getWorkoutByPeriod(chosenPeriod);
                        refreshList(cursor);
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.filtersWorkoutRecent:
                cursor = db.getWorkoutByRecent();
                refreshList(cursor);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshList(Cursor cursor) {
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        if (PreferenceManager.getDefaultSharedPreferences(HistoryActivity.this).getBoolean("key_animations", false))
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }


}
