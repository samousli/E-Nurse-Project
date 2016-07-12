package com.example.vromia.e_nurseproject.Activities;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.vromia.e_nurseproject.R;
import com.example.vromia.e_nurseproject.Utils.SharedPrefsManager;

import java.util.Calendar;


public class NotificationManagerService extends Service {
    private static final String TAG = NotificationManagerService.class.getName();
    private NotificationManager mNotificationManager;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate was called");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPrefsManager shpf = new SharedPrefsManager(this.getApplicationContext());
        long at = shpf.getPrefsRoutine();

        if (at == 0) {

            return START_STICKY;
        }
        Log.d(TAG, "found " + at);

        long cur = Calendar.getInstance().getTimeInMillis();
        if (at < cur) {
            showNotification();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(at);
            c.add(Calendar.HOUR, 24);
        }

        /// mRepository.close();


        return START_STICKY;
    }


    private void showNotification() {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notificationMessage = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.reminder))
                .setContentText(getString(R.string.time_to_workout))
                .setSmallIcon(R.drawable.ic_launcher)
                .setSound(soundUri)
                .setVibrate(new long[]{1000})
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .build();
        notificationMessage.flags |= Notification.FLAG_AUTO_CANCEL;
        //notificationMessage.flags |= Notification.FLAG_INSISTENT;
        mNotificationManager.notify(1000 + 1, notificationMessage);
        Log.d(TAG, "Notification send successfully");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // mRepository.close();
    }

}
