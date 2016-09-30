package com.example.vromia.e_nurseproject.Activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.example.vromia.e_nurseproject.Data.HealthDatabase;
import com.example.vromia.e_nurseproject.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Nikos on 3/6/2015.
 */
public class SettingsActivity extends PreferenceActivity {

    private static final String TAG_SUCCESS = "success";
    private static String url = "http://nikozisi.webpages.auth.gr/enurse/sync_data.php";
    private boolean hasAnimations;
    private HealthDatabase db;
    private int success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hasAnimations = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).getBoolean("key_animations", true);

        addPreferencesFromResource(R.xml.preferences);

        Preference screen = findPreference("key_about");
        screen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                builder.setMessage("About text")
                        .setTitle("About Us");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });

        screen = findPreference("key_animations");
        screen.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                hasAnimations = (boolean) newValue;
                return true;
            }
        });

        screen = findPreference("key_rate");
        screen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }

                return false;
            }
        });



        screen = findPreference("key_delete_exc");
        screen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                builder.setMessage("????? ???????? ??? ?????? ?? ?????????? ??? ?? ???????? ????????");
                builder.setPositiveButton("??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //TODO delete excercise
                    }
                });
                builder.setNegativeButton("???????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });


        //when user clicks on "delete expense" preference item
        //ask for confirmation and delete the expense data records
        screen = findPreference("key_delete_food");
        screen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                builder.setMessage("????? ???????? ??? ?????? ?? ?????????? ??? ?? ???????? ??????????");
                builder.setPositiveButton("??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //TODO delete food
                    }
                });
                builder.setNegativeButton("???????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }


        screen = findPreference("key_log_out");
        screen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth.getInstance().signOut();


                HealthDatabase db = new HealthDatabase(SettingsActivity.this);
                db.rectreateDB();
                db.close();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
                return true;
            }
        });
    }







    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        if (PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).getBoolean("key_animations", false))
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }


}
