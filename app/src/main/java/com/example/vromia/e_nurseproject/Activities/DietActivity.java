package com.example.vromia.e_nurseproject.Activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.example.vromia.e_nurseproject.Data.DietItem;
import com.example.vromia.e_nurseproject.Data.HealthDatabase;
import com.example.vromia.e_nurseproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Vromia on 17/12/2014.
 */
public class DietActivity extends FragmentActivity {

    private ImageButton bHour;
    private ImageButton bDate;
    private Button bOk;
    private EditText quantField;
    private Spinner spinner;
    private CalendarDatePickerDialogFragment cdate;//gui for showing date
    private RadialTimePickerDialogFragment timeDialog;//gui for showing date
    private String date, hour;
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
            date = i + "-" + month + "-" + day;
        }
    };
    private RadialTimePickerDialogFragment.OnTimeSetListener timelistener = new RadialTimePickerDialogFragment.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialTimePickerDialogFragment radialPickerLayout, int i, int i2) {
            String hours, mins;
            if (i < 10)
                hours = "0" + i;
            else
                hours = String.valueOf(i);
            if (i2 < 10)
                mins = "0" + i2;
            else
                mins = String.valueOf(i2);
            hour = hours + ":" + mins;
            Log.i("msg", hour);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        initUI();
        initListeners();


        String categories[] = getResources().getStringArray(R.array.foodNames);
        ArrayList<String> finalCategories = new ArrayList<>();
        for (int i = 0; i < categories.length; i++) {
            finalCategories.add(categories[i]);
        }
        ArrayAdapter adapter = new ArrayAdapter(DietActivity.this, R.layout.spinner_item,
                                    R.id.tvSpinnerCategories, finalCategories);
        spinner.setAdapter(adapter);

        Calendar c = Calendar.getInstance();
        cdate = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(listener)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setPreselectedDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                .setDoneText("Yes")
                .setCancelText("No")
                .setThemeCustom(R.style.BetterPickersStyle);

        //Initialize variable date to current date
        String day = c.get(Calendar.DAY_OF_MONTH) + "";
        String month = (c.get(Calendar.MONTH) + 1) + "";
        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + c.get(Calendar.DAY_OF_MONTH);
        }
        if (c.get(Calendar.MONTH) + 1 < 10) {
            month = "0" + (c.get(Calendar.MONTH) + 1);
        }

        date = c.get(Calendar.YEAR) + "-" + month + "-" + day;

        timeDialog = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(timelistener)
                .setStartTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))
                .setDoneText("Yes")
                .setCancelText("No")
                .setThemeCustom(R.style.BetterPickersStyle);

        // Initialize variable hour to current hour
        int temp_hour = c.get(Calendar.HOUR_OF_DAY);
        int temp_min = c.get(Calendar.MINUTE);
        String temp_hour_fixed, temp_min_fixed;
        // old comment mTODO This block actually does nothing atm...?
        if (temp_hour < 10) {
            temp_hour_fixed = "0" + String.valueOf(temp_hour);
        } else {
            temp_hour_fixed = String.valueOf(temp_hour);
        }
        if (temp_min < 10) {
            temp_min_fixed = "0" + String.valueOf(temp_min);
        } else {
            temp_min_fixed = String.valueOf(temp_min);
        }
        hour = temp_hour_fixed + ":" + temp_min_fixed;

    }

    public void initUI() {
        bHour = (ImageButton) findViewById(R.id.imbtHour);
        bDate = (ImageButton) findViewById(R.id.imbtDate);
        bOk = (Button) findViewById(R.id.btOk);
        quantField = (EditText) findViewById(R.id.etQuant);
        spinner = (Spinner) findViewById(R.id.spChooseFood);

        bHour.setImageResource(R.drawable.clock);
        bDate.setImageResource(R.drawable.calendar);


    }

    public void initListeners() {
        bHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog.show(getSupportFragmentManager(), "Nikos");
            }
        });
        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdate.show(getSupportFragmentManager(), "Calendar Dialog");
            }
        });
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double quantity = 1;
                try {
                    quantity = Double.valueOf(quantField.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(DietActivity.this,
                            getString(R.string.invalidEntry_Numeric), Toast.LENGTH_LONG);

                }
                String foodName = spinner.getSelectedItem().toString();

                HealthDatabase db = new HealthDatabase(DietActivity.this);//instance of current database

                DietItem item = new DietItem(foodName, date, quantity, hour);
                db.InsertDiet(item);
                db.close();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("user-diet").child(uid).push().setValue(item);

                Toast.makeText(DietActivity.this,
                        getString(R.string.successfulEntry), Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        if (PreferenceManager.getDefaultSharedPreferences(DietActivity.this).getBoolean("key_animations", false))
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
