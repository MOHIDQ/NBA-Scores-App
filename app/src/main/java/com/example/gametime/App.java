package com.example.gametime;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

public class App extends Application {
    public static final String GAMESCORES_CHANNEL_ID = "gameScoresChannel";
    public static final int GAMESCORES__ID = 15;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
        startService(new Intent(this, BackgroundService.class));
    }

    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel scoreNotificationChannel = new NotificationChannel(
                    GAMESCORES_CHANNEL_ID,
                    "Scores Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            scoreNotificationChannel.setDescription("This channel sends the Game score updates");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(scoreNotificationChannel);
        }
    }
}
