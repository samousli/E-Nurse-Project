package com.example.vromia.e_nurseproject.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class StartServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "StartServiceReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context, NotificationManagerService.class);
        Log.d(TAG, "Starting notification manager service...");
        context.startService(service);
    }
}
