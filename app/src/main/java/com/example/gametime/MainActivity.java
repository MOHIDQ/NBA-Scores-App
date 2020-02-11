package com.example.gametime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.gametime.App.GAMESCORES_CHANNEL_ID;
import static com.example.gametime.App.GAMESCORES__ID;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Game> currentGameList;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);
    }

    public void sendOnScoreUpdate(View v) {

        RemoteViews collasped = new RemoteViews(getPackageName(),
                R.layout.custom_notification_collasped);
        RemoteViews expanded = new RemoteViews(getPackageName(),
                R.layout.custom_notification_expand);
        String title = "Raptor     43       vs                        Lakers         24";
        String message = "Lebron James made a 3";

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, GAMESCORES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle(title)
                .setContentText(message)
                //.setCustomContentView(collasped)
                .setCustomBigContentView(expanded)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(GAMESCORES__ID, notification);
    }

    //if network connection is available run async task for getting scores data
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //ANY UPDATES TO UI HAVE TO BE DONE HERE
    private void updateUI() {
        //added log messages for test purposes
        for (int i = 0; i < currentGameList.size(); i++) {
            Log.i("TEST", currentGameList.get(i).getAwayTeam() + " | "+ currentGameList.get(i).getAwayScore());
        }

    }





    //executes async task to update live scores every timer interval, running on ui thread
    private void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask asynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            APICall apiScoreGetter = new APICall();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            apiScoreGetter.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(asynchronousTask, 0, 5000); //execute in every 5 seconds
    }



    //async task for GET requests to REST API
    private class APICall extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return ScoreParser.getDataFromAPI();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            currentGameList.clear();
            currentGameList = ScoreParser.parseGames(result);
            updateUI();
        }

    }
}
