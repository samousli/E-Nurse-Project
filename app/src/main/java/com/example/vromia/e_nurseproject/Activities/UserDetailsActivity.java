package com.example.vromia.e_nurseproject.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vromia.e_nurseproject.Data.HealthDatabase;
import com.example.vromia.e_nurseproject.R;
import com.example.vromia.e_nurseproject.Utils.SharedPrefsManager;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;



/**
 * Created by Vromia on 17/12/2014.
 */
public class UserDetailsActivity extends Activity {

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_AGE = "age";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_MALE = "male";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_CUSUCCESS = "success";
    private static final String TAG_ID = "userID";
    private static final String TAG_HEIGHT = "height";
    private static String user_details_url = "http://nikozisi.webpages.auth.gr/enurse/get_user_details.php";
    private static String create_user_url = "http://nikozisi.webpages.auth.gr/enurse/create_user.php";
    private EditText onoma, ilikia, ypsos, baros, istorikoPathiseon, email, etSurname, etUsername, etPassword;
    private LinearLayout llAccount, llDiseases;
    private ListView listview;
    private RadioGroup fylo;
    private Button btOk;
    private ImageButton btAdd;
    private RadioButton rb_male, rb_female;
    private String Sfylo = "";
    private HealthDatabase hdb;
    private Spinner sDoctors;
    private CheckBox ckRoutine;
    private TimePicker tpRoutine;
    private ArrayList<String> diseases;
    private int userID = -1;
    private int cuSuccess = -1;
    private int sex = -1;
    private String userName, userSurname, history;
    private int age, male, weight, height;
    private String menu;
    private ProgressDialog pDialog;

    private SharedPrefsManager manager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        manager = new SharedPrefsManager(UserDetailsActivity.this);
        diseases = new ArrayList<>();

        initUI();
        userID = getIntent().getIntExtra("userID", -1);
        menu = getIntent().getStringExtra("Menu");
        if (userID != -1) {
            userName = getIntent().getStringExtra("userName");
            userSurname = getIntent().getStringExtra("userSurname");
            manager.startEditing();
            manager.setPrefsUserID(userID);
            manager.commit();
            //Log.i("Surname",userSurname);
        } else {
            if (manager.getPrefsUserID() != -1) {
                userName = manager.getPrefsUsername();
                userSurname = manager.getPrefsSurname();
            }
        }


        if (menu != null) {
            fillUIWithValues();
        }

        setUpUI();
        initListeners();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initUI() {
        llAccount = (LinearLayout) findViewById(R.id.llAccount);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        llDiseases = (LinearLayout) findViewById(R.id.llDiseases);

        onoma = (EditText) findViewById(R.id.onoma);
        etSurname = (EditText) findViewById(R.id.etsurname);
        ilikia = (EditText) findViewById(R.id.ilikia);

        ypsos = (EditText) findViewById(R.id.ypsos);
        baros = (EditText) findViewById(R.id.baros);
//      istorikoPathiseon = (EditText) findViewById(R.id.istorikoPathiseon);
        fylo = (RadioGroup) findViewById(R.id.fylo);
        sDoctors = (Spinner) findViewById(R.id.spDoctors);

        btOk = (Button) findViewById(R.id.btOk);
        btAdd = (ImageButton) findViewById(R.id.ibAddHistory);

        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);

        ckRoutine = (CheckBox) findViewById(R.id.checkBoxRoutine);
        tpRoutine = (TimePicker) findViewById(R.id.timePicker);
    }


    private void fillUIWithValues() {

        llAccount.setVisibility(View.GONE);

        onoma.setText(manager.getPrefsOnoma());
        etSurname.setText(manager.getPrefsSurname());
        ilikia.setText(manager.getPrefsIlikia() + "");
        ypsos.setText(manager.getYpsos() + "");
        baros.setText(manager.getPrefsBaros() + "");
        if (manager.getPrefsFylo().equals("Άνδρας")) {
            rb_male.setChecked(true);
        } else {
            rb_female.setChecked(true);
        }

        String d[] = manager.getPrefsIstorikoPathiseon().split("-");
        for (String i : d) {
            diseases.add(i);
        }
        addDiseases();

        long r = manager.getPrefsRoutine();
        if (r == 0) {
            ckRoutine.setChecked(false);
            tpRoutine.setVisibility(View.INVISIBLE);
        } else {
            ckRoutine.setChecked(true);
            tpRoutine.setVisibility(View.VISIBLE);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(r);
            tpRoutine.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
            tpRoutine.setCurrentMinute(c.get(Calendar.MINUTE));
        }


    }

    private void setUpUI() {
        hdb = new HealthDatabase(UserDetailsActivity.this);
        ArrayList<String> full_names = hdb.getDoctorsFullName();
        hdb.close();

        final ArrayAdapter adapter = new ArrayAdapter(UserDetailsActivity.this, R.layout.spinner_item, R.id.tvSpinnerCategories, full_names);
        sDoctors.setAdapter(adapter);


        if (userID != -1) {
            llAccount.setVisibility(View.GONE);

            onoma.setText(userName);
            etSurname.setText(userSurname);
            //TODO firebase impl
            // new GetUser().execute();

        }


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                diseases.add("Nikos");
                diseases.add("");
                addDiseases();
            }
        });
//        listview.setAdapter(new DiseaseAdapter(UserDetailsActivity.this, diseases));


    }


    private void addDiseases() {
        llDiseases.removeAllViewsInLayout();
        for (int i = 0; i < diseases.size(); i++) {

            final int pos = i;
            /**
             * inflate items/ add items in linear layout instead of listview
             */
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.list_item_disease_history, null);
            /**
             * getting id of row.xml
             */
            EditText diseaseName = (EditText) mLinearView
                    .findViewById(R.id.etDiseaseName);
            diseaseName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    diseases.set(pos, s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            ImageButton imageButton = (ImageButton) mLinearView
                    .findViewById(R.id.ibDelete);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    diseases.remove(pos);
                    addDiseases();
                }
            });
            /**
             * set item into row
             */
            final String fName = diseases.get(i);
            diseaseName.setText(fName);

            /**
             * add view in top linear
             */

            llDiseases.addView(mLinearView);

            /**
             * get item row on click
             *
             */
            mLinearView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(UserDetailsActivity.this, "Clicked item;" + fName,
                    //        Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initListeners() {
        fylo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (rb_male.isChecked()) {
                    Sfylo = getString(R.string.male);
                    sex = 1;
                } else if (rb_female.isChecked()) {
                    Sfylo = getString(R.string.female);
                    sex = 0;
                }
            }
        });



        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Sonoma, Silikia, Sypsos, Sbaros, SistorikoPathiseon, SUsername, SPassword, Ssurname;
                boolean flag = true;

                Sonoma = String.valueOf(onoma.getText());
                Silikia = String.valueOf(ilikia.getText());
                Sypsos = String.valueOf(ypsos.getText());
                Sbaros = String.valueOf(baros.getText());
                Ssurname = String.valueOf(etSurname.getText());
                SistorikoPathiseon = "";
                if (menu != null && SistorikoPathiseon.length() > 0) {
                    for (int i = 0; i < diseases.size(); i++) {
                        if (!diseases.get(i).trim().equals("")) {
                            SistorikoPathiseon += diseases.get(i) + "-";
                        }
                    }

                    SistorikoPathiseon = SistorikoPathiseon.substring(0, SistorikoPathiseon.length() - 1);
                }

                SharedPrefsManager spmanager = new SharedPrefsManager(UserDetailsActivity.this);

                Log.i("nikos", "userid = " + userID + "   prefs=" + manager.getPrefsUserID());
                if (userID == -1 && manager.getPrefsUserID() == -1) {
                    SUsername = String.valueOf(etUsername.getText());
                    SPassword = String.valueOf(etPassword.getText());

                    if (SUsername.equals("")) {
                        //"Παρακαλώ γράψτε τα πεδία Όνομα Λογαριασμού - Κωδικός"
                        Toast.makeText(UserDetailsActivity.this,
                                getString(R.string.error_insertUsernamePassword), Toast.LENGTH_LONG).show();
                        flag = false;
                    } else {
                        flag = true;
                        spmanager.startEditing();
                        spmanager.setPrefsUsername(SUsername);
                        spmanager.setPrefsPassword(SPassword);
                        spmanager.commit();

                        // Get all data from the UI and pass to the async runner, so the call to the
                        // server can be made.
                        String username = etUsername.getText().toString();
                        String password = etPassword.getText().toString();
                        String name = onoma.getText().toString();
                        String surname = etSurname.getText().toString();
                        String age = ilikia.getText().toString();
                        String weight = baros.getText().toString();
                        String height = ypsos.getText().toString();
                        //TODO firebase impl
//                        new createUser().execute(username, password, name,
//                                surname, age, Integer.toString(sex), weight, height);

                    }
                }

                spmanager.startEditing();

                spmanager.setPrefsOnoma(Sonoma);
                spmanager.setPrefsSurname(Ssurname);
                spmanager.setPrefsIlikia(Integer.parseInt(Silikia));
//                spmanager.setPrefsYpsos(Float.parseFloat(Sypsos));
                spmanager.setPrefsBaros(Float.parseFloat(Sbaros));
                spmanager.setPrefsYpsos(Float.parseFloat(Sypsos));
                spmanager.setPrefsIstorikoPathiseon(SistorikoPathiseon);
                spmanager.setPrefsFylo(Sfylo);

                if(ckRoutine.isChecked()){
                    final Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, tpRoutine.getCurrentHour());
                    calendar.set(Calendar.MINUTE, tpRoutine.getCurrentMinute());
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    long millis = calendar.getTimeInMillis();


                    spmanager.setPrefsRoutine(millis);
                }else{

                    spmanager.setPrefsRoutine(0);
                }


                spmanager.commit();



                if (!spmanager.getPrefsStart()) {
                    spmanager.startEditing();
                    spmanager.setPrefsStart(true);
                    spmanager.commit();
                }

                if (flag) {
                    startActivity(new Intent(UserDetailsActivity.this, HomeActivity.class));
                    finish();
                }


            }
        });


        ckRoutine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    tpRoutine.setVisibility(View.VISIBLE);
                }else{
                    tpRoutine.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    /*
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "UserDetails Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.vromia.e_nurseproject.Activities/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }*/
    /*
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "UserDetails Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.vromia.e_nurseproject.Activities/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/

//TODO get user firebase
//    //AsyncTack < params,progress,result
//    class GetUser extends AsyncTask<String, String, String> {
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(UserDetailsActivity.this);
//            pDialog.setMessage("Αντιστοίχηση Στοιχείων. Παρακαλώ Περιμένετε");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        //Check user starting background thread
//        @Override
//        protected String doInBackground(String... args) {
//            RequestParams p = new RequestParams("patientID", userID);
//            HttpHandler.post(user_details_url, p, new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    try {
//                        int success = response.getInt(TAG_SUCCESS);
//
//                        if (success == 1) {
//                            Log.i("Success", "success");
//                            age = response.getInt(TAG_AGE);
//                            male = response.getInt(TAG_MALE);
//                            history = response.getString(TAG_HISTORY);
//                            weight = response.getInt(TAG_WEIGHT);
//                            height = response.getInt(TAG_HEIGHT);
//
//                            Log.i("values", age + " - " + male + " - " + history + " - " + weight);
//
//                        } else {
//                            Log.i("UserSuccess", "Fail");
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            return "";
//        }
//
//        @Override
//        protected void onPostExecute(String o) {
//            super.onPostExecute(o);
//            pDialog.dismiss();
//            ilikia.setText(age + "");
//            if (male == 1) {
//                rb_male.setChecked(true);
//            } else {
//                rb_female.setChecked(true);
//            }
//
////            istorikoPathiseon.setText(history);
//            baros.setText(weight + "");
//            ypsos.setText(height + "");
//
//        }
//
//
//    }
//
//    class createUser extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... args) {
//            /* Request param format:
//            {
//                String username, String password, String name, String surname???,
//                int age, int male,  #!! 1 for male, 0 for female
//                float weight, float height
//            }
//            */
//            String doctor_full_name = sDoctors.getSelectedItem().toString();
//            String tokens[] = doctor_full_name.split(" ");
//            RequestParams p = new RequestParams();
//            p.put("doctor_name", tokens[0]);
//            p.put("doctor_surname", tokens[1]);
//
//            HttpHandler.post(create_user_url, p, new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    try {
//                        cuSuccess = response.getInt(TAG_SUCCESS);
//                        int userID = response.getInt(TAG_ID);
//                        SharedPrefsManager manager = new SharedPrefsManager(UserDetailsActivity.this);
//                        manager.startEditing();
//                        manager.setPrefsUserID(userID);
//                        manager.commit();
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            return "";
//        }
//    }

}
