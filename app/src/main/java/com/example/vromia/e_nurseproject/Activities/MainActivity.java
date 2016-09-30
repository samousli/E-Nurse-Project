package com.example.vromia.e_nurseproject.Activities;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vromia.e_nurseproject.Data.DoctorItem;
import com.example.vromia.e_nurseproject.Data.HealthDatabase;
import com.example.vromia.e_nurseproject.R;
import com.example.vromia.e_nurseproject.Utils.StartServiceReceiver;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;


public class MainActivity extends Activity {
    private static final long REPEAT_TIME = 1000 * 10;
    private HealthDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //boolean isPass = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("PREFS_START_OFF_APP" , false);
//        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(MainActivity.this);
//
//        boolean pass = sharedPrefsManager.getPrefsStart();

        AlarmManager service = (AlarmManager) this
                .getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, StartServiceReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        service.setRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), REPEAT_TIME, pending);

        db = new HealthDatabase(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        updateDoctorDatabase();

                        startActivity(new Intent(MainActivity.this, UserDetailsActivity.class));
                        HealthDatabase db = new HealthDatabase(MainActivity.this);
                        db.rectreateDB();
                        db.close();
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(
                                    AuthUI.EMAIL_PROVIDER,
                                    AuthUI.GOOGLE_PROVIDER)
                            .build(),
                    RC_SIGN_IN);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {


                HealthDatabase db = new HealthDatabase(MainActivity.this);
                db.rectreateDB();
                db.close();

                updateDoctorDatabase();
                //Comment the following if any crush happens
                //from here
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(MainActivity.this, UserDetailsActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //to here
                //comment out the following
//
//                startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                finish();


            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }
    }

    public void updateDoctorDatabase() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("", "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                DoctorItem item = dataSnapshot.getValue(DoctorItem.class);

                db.InsertDoctor(item);
                db.UpdateDoctor(item);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("", "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                DoctorItem item = dataSnapshot.getValue(DoctorItem.class);
                db.UpdateDoctor(item);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("", "onChildRemoved:" + dataSnapshot.getKey());

                String commentKey = dataSnapshot.getKey();
                //TODO remove item from database
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("", "onChildMoved:" + dataSnapshot.getKey());
                //DO NOTHING
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("doctors").addChildEventListener(childEventListener);
    }
}
