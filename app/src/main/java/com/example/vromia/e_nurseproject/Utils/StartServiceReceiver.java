package com.example.vromia.e_nurseproject.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.vromia.e_nurseproject.Utils.NotificationManagerService;


public class StartServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "StartServiceReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context, NotificationManagerService.class);
        Log.d(TAG, "Starting notification manager service...");
        context.startService(service);
    }
}
