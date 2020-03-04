package com.example.gametime;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.gametime.App.GAMESCORES_CHANNEL_ID;

public class BackgroundService extends Service {
    private static final int NOTIF_ID = 12;

    @Override
    public void onCreate(){
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        startForeground(NOTIF_ID, new NotificationCompat.Builder(this, GAMESCORES_CHANNEL_ID)
                .setOngoing(true) // TODO: maybe change after dev
                .setSmallIcon(R.drawable.ic_background_service_logo)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //MainActivity mainActivity = new MainActivity();
        //if(MainActivity.getInstance() != null) {
        //MainActivity.getInstance().callAsynchronousTask();
        //}
        //mainActivity.callAsynchronousTask();


         //if(mainActivity.isNetworkAvailable()) {
            //mainActivity.callAsynchronousTask();
        //}

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
