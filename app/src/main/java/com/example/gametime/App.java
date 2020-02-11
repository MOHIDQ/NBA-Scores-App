package com.example.gametime;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

public class App extends Application {
    public static final String GAMESCORES_CHANNEL_ID = "gameScoresChannel";
    public static final int GAMESCORES__ID = 2;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
        startService(new Intent(this, BackgroundService.class));
    }

    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    GAMESCORES_CHANNEL_ID,
                    "Scores Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription(" this channels sends the scores of the games");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
