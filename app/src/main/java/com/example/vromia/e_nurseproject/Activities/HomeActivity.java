package com.example.vromia.e_nurseproject.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.vromia.e_nurseproject.Data.DietItem;
import com.example.vromia.e_nurseproject.Data.DoctorItem;
import com.example.vromia.e_nurseproject.Data.DrugsItem;
import com.example.vromia.e_nurseproject.Data.GridItem;
import com.example.vromia.e_nurseproject.Data.HealthDatabase;
import com.example.vromia.e_nurseproject.Data.WorkoutItem;
import com.example.vromia.e_nurseproject.R;
import com.example.vromia.e_nurseproject.Utils.GridAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Created by Vromia on 2/6/2015.
 */
public class HomeActivity extends Activity {


    private static final String TAG = "HOME ACTIVITY";
    private GridView gridView;
    private ArrayList<GridItem> items;
    private HealthDatabase db;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        db = new HealthDatabase(this);
        updateDietDatabase();
        updateWorkoutDatabase();
        updateDoctorDatabase();
        updateDrugDatabase();


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


    public void updateDietDatabase() {

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                DietItem item = dataSnapshot.getValue(DietItem.class);
                Log.d(TAG, "diete-item " + item.toString());

                    db.InsertDiet(item);
                db.updateDiet(item);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                DietItem newComment = dataSnapshot.getValue(DietItem.class);
                String commentKey = dataSnapshot.getKey();
                //TODO update item
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String commentKey = dataSnapshot.getKey();
                //TODO remove item from database
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
                //DO NOTHING
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(HomeActivity.this, "Failed to load dietItems.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("user-diet").child(userId).addChildEventListener(childEventListener);
    }

    public void updateWorkoutDatabase() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                WorkoutItem item = dataSnapshot.getValue(WorkoutItem.class);

                db.InsertWorkout(item);
                db.updatWorkOut(item);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                WorkoutItem newComment = dataSnapshot.getValue(WorkoutItem.class);
                String commentKey = dataSnapshot.getKey();
                //TODO update item
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String commentKey = dataSnapshot.getKey();
                //TODO remove item from database
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
                //DO NOTHING
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(HomeActivity.this, "Failed to load WorkoutItems.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("user-workout").child(userId).addChildEventListener(childEventListener);
    }

    public void updateDoctorDatabase() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                DoctorItem item = dataSnapshot.getValue(DoctorItem.class);

                db.InsertDoctor(item);
                db.UpdateDoctor(item);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                DoctorItem item = dataSnapshot.getValue(DoctorItem.class);
                db.UpdateDoctor(item);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String commentKey = dataSnapshot.getKey();
                //TODO remove item from database
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
                //DO NOTHING
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(HomeActivity.this, "Failed to load WorkoutItems.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.child("doctors").addChildEventListener(childEventListener);
    }

    public void updateDrugDatabase() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                DrugsItem item = dataSnapshot.getValue(DrugsItem.class);

                db.InsertDrugs(item);
                db.updatDrugs(item);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                //WorkoutItem newComment = dataSnapshot.getValue(WorkoutItem.class);
                //String commentKey = dataSnapshot.getKey();
                //TODO update item
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String commentKey = dataSnapshot.getKey();
                //TODO remove item from database
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
                //DO NOTHING
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(HomeActivity.this, "Failed to load WorkoutItems.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("user-drugs").child(userId).addChildEventListener(childEventListener);
    }
}
