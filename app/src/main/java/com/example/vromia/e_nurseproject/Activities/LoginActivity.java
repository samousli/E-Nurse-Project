package com.example.vromia.e_nurseproject.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vromia.e_nurseproject.Data.DoctorItem;
import com.example.vromia.e_nurseproject.Data.HealthDatabase;
import com.example.vromia.e_nurseproject.R;
import com.example.vromia.e_nurseproject.Utils.HttpHandler;
import com.example.vromia.e_nurseproject.Utils.SharedPrefsManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends Activity {


    private static final String TAG_CHECKSUCCESS = "success";
    private static final String TAG_USERID = "userid";
    private static final String TAG_DOCTORS = "doctors";
    private static final String TAG_DOCTORSUCCESS = "success";
    private static final String TAG_DOCTORID = "id";
    private static final String TAG_DOCTORNAME = "name";
    private static final String TAG_DOCTORSURNAME = "surname";
    private static String url = "check_user.php";
    private static String doctors_url = "http://nikozisi.webpages.auth.gr/enurse/get_doctors.php";
    private EditText etUsername, etPassword;
    private Button bConnect, bCreateUser;
    private int userID;
    private String userName, userSuname;
    private int success, dsuccess;
    private HealthDatabase hdb;
    private JSONArray jsonArray;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        initBasicVariables();
        initListeners();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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


    private void initBasicVariables() {

        jsonArray = null;
        userID = -1;
        success = -1;
        dsuccess = -1;
        hdb = new HealthDatabase(LoginActivity.this);
        userName = userSuname = "";

        if (haveNetworkConnection()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpHandler.get(doctors_url, new RequestParams(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                dsuccess = response.getInt(TAG_DOCTORSUCCESS);

                                if (dsuccess == 1) {

                                    jsonArray = response.getJSONArray(TAG_DOCTORS);
                                    Log.i("Length", jsonArray.length() + "");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject c = jsonArray.getJSONObject(i);

                                        int id = c.getInt(TAG_DOCTORID);

                                        String name = c.getString(TAG_DOCTORNAME);
                                        String surname = c.getString(TAG_DOCTORSURNAME);
                                        Log.i("DoctorName", name);

                                        DoctorItem doctor = new DoctorItem(id, name, surname);
                                        hdb.InsertDoctor(doctor);
                                    }
                                    // hdb.showDoctors();
                                    hdb.close();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();

        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.no_network), Toast.LENGTH_LONG).show();
        }
    }

    private void initListeners() {
        bConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //execute the php script and see the result via json format
                if (haveNetworkConnection()) {
                    String username = etUsername.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    new CheckUser().execute(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.no_network), Toast.LENGTH_LONG).show();
                }
            }
        });

        bCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveNetworkConnection()) {
                    Intent intent = new Intent(LoginActivity.this, UserDetailsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.no_network), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void initUI() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bConnect = (Button) findViewById(R.id.bConnect);
        bCreateUser = (Button) findViewById(R.id.bCreateUser);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        if (PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getBoolean("key_animations", false))
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.vromia.e_nurseproject.Activities/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.vromia.e_nurseproject.Activities/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //AsyncTack < params,progress,result
    class CheckUser extends AsyncTask<String, String, String> {
        //Check user starting background thread
        @Override
        protected String doInBackground(String... args) {

            RequestParams p = new RequestParams();
            p.put("username", args[0]);
            p.put("password", args[1]);
            HttpHandler.post(url, p, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        success = response.getInt(TAG_CHECKSUCCESS);

                        if (success == 1) {
                            userID = response.getInt(TAG_USERID);
                            SharedPrefsManager manager = new SharedPrefsManager(LoginActivity.this);
                            manager.startEditing();
                            manager.setPrefsUserID(userID);
                            manager.commit();
                            Log.i("UserID", userID + "");
                            userName = response.getString(TAG_DOCTORNAME);
                            userSuname = response.getString(TAG_DOCTORSURNAME);
                        } else {
                            Log.i("UserSuccess", "Fail");
                        }

                        Log.i("UserID", userID + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return "";
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);

            if (success == 1) {
                Intent intent = new Intent(LoginActivity.this, UserDetailsActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userName", userName);
                intent.putExtra("userSurname", userSuname);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show();
            }
        }
    }

}
