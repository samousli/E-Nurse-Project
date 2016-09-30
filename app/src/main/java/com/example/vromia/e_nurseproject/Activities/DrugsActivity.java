package com.example.vromia.e_nurseproject.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.vromia.e_nurseproject.Data.DrugsItem;
import com.example.vromia.e_nurseproject.Data.HealthDatabase;
import com.example.vromia.e_nurseproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Vromia on 17/12/2014.
 */
public class DrugsActivity extends FragmentActivity {
    private static final String FRAG_TAG_DATE_PICKER = "date_picker";
    AutoCompleteTextView textView;
    String[] name = {
            "Πονοκέφαλος", "Ημικρανία", "Πονόλαιμος", "Πόνοι Περιόδου",
            "Πίεση", "Κάψιμο", "Πυρετός", "Ναυτία"};
    private RadioButton bMorn;
    private RadioButton bNoon;
    private RadioButton bNight;
    private RadioGroup rGroup;
    private ImageButton bDate;
    private Button bBack;
    private Button bOk;
    private EditText quantField,etCause;
    private Spinner spinner;
    private CalendarDatePickerDialogFragment cdate;//gui for showing date
    private String date, tod;
    private CalendarDatePickerDialogFragment.OnDateSetListener listener = new CalendarDatePickerDialogFragment.OnDateSetListener() {
        @Override
        public void onDateSet(CalendarDatePickerDialogFragment calendarDatePickerDialog, int i, int i2, int i3) {
            String month, day;
            i2++;
            if (i2 < 10) {
                month = "0" + i2;
            } else {
                month = String.valueOf(i2);
            }
            if (i3 < 10) {
                day = "0" + i3;
            } else {
                day = String.valueOf(i3);
            }
            date = i + "-" + month + "-" + day;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drugs);


        initUI();
        initListeners();
        bMorn.setActivated(true);

        String categories[] = getResources().getStringArray(R.array.drugNames);
        ArrayList<String> finalCategories = new ArrayList<>();
        for (int i = 0; i < categories.length; i++) {
            finalCategories.add(categories[i]);
        }
        ArrayAdapter adapter = new ArrayAdapter(DrugsActivity.this, R.layout.spinner_item, R.id.tvSpinnerCategories, finalCategories);
        spinner.setAdapter(adapter);



        Calendar c = Calendar.getInstance();
        cdate = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(listener)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setPreselectedDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                .setDoneText("Yes")
                .setCancelText("No");


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
        tod = "Πρωί";
        bMorn.setChecked(true);
    }

    public void initUI() {
        bMorn = (RadioButton) findViewById(R.id.rbMorn);
        bNoon = (RadioButton) findViewById(R.id.rbNoon);
        bNight = (RadioButton) findViewById(R.id.rbNight);
        rGroup = (RadioGroup) findViewById(R.id.rdtod);
        bDate = (ImageButton) findViewById(R.id.imbtDate);
        quantField = (EditText) findViewById(R.id.etDuration);
//        etCause=(EditText) findViewById(R.id.etCause);
        textView = (AutoCompleteTextView) findViewById(R.id.etCause);
        spinner = (Spinner) findViewById(R.id.spChooseEx);
        bOk = (Button) findViewById(R.id.btOk);



        ArrayAdapter<String> causesAdapter = new ArrayAdapter<String> (this,android.R.layout.simple_dropdown_item_1line,name);
        textView.setThreshold(3);
        textView.setAdapter(causesAdapter);

        bDate.setImageResource(R.drawable.calendar);

    }

    public void initListeners() {
        bMorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tod = getString(R.string.morning);
            }
        });

        bNoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tod = getString(R.string.midday);
            }
        });

        bNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tod = getString(R.string.evening);
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
                Double quantity = 10.0;
                String exName = spinner.getSelectedItem().toString();
                try {
                    quantity = Double.valueOf(quantField.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(DrugsActivity.this,
                            getString(R.string.invalidEntry_Numeric), Toast.LENGTH_LONG);
                }
//                String cause = etCause.getText().toString();
                String cause = textView.getText().toString();
                HealthDatabase db = new HealthDatabase(DrugsActivity.this);//instance of current database
                DrugsItem item = new DrugsItem(exName, date, quantity, tod, cause, 0);
                Log.i("msg", exName + " " + date + " " + quantity + " " + tod);
                db.InsertDrugs(item);
                item.setId(db.getLastId(HealthDatabase.TABLE_DRUGS));
                db.close();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("user-drugs").child(uid).push().setValue(item);


                Toast.makeText(DrugsActivity.this, getString(R.string.successfulEntry), Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.pull_in_left , R.anim.push_out_right);
    }
}