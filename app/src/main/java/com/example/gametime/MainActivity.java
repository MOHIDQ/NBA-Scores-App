package com.example.gametime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

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
    public ArrayList<Game> currentGameList = new ArrayList<>(); //contain all game data
    private NotificationManagerCompat notificationManager;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //currentGameList = new ArrayList<>();
        notificationManager = NotificationManagerCompat.from(this);

        dataBaseTester();

        //if user has active internet connection get live scores
       // if(isNetworkAvailable()) {
            //callAsynchronousTask();
        //}
        //condition for when network connection is not available
        //else {
           // Log.i("TEST", "NO INTERNET");
        //}
    }

    private void dataBaseTester() {
        db = new DatabaseHelper(this);

        Cursor result = db.getData();
        if (result.getCount() == 0) {
            //show message
            Log.i("CURSOR", "NO DATA");
        }
        else {
            StringBuffer buffer = new StringBuffer();
            while (result.moveToNext()) {
                buffer.append("time: " + result.getString(0) + "\n");
                buffer.append("points: " + result.getString(1) + "\n");
                buffer.append("team: " + result.getString(2) + "\n");
            }
            Log.i("CURSOR", buffer.toString());
        }
    }


    public void sendOnScoreUpdate(View v) {
        // notification test function
        createNotification("Raptors", "Lakers", 50,23, "Lebron James made a 3point play");
    }

    public void createNotification( String homeTeam, String awayTeam, int homeScore, int awayScore, String latestPlay)
    {
        String teams = homeTeam + " " + homeScore + " - " + awayScore + " " + awayTeam;
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, GAMESCORES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle(teams)
                .setContentText(latestPlay)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(GAMESCORES__ID, notification);
    }

    //if network connection is available run async task for getting scores data
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //UPDATES TO GAME CARDS HAVE TO BE DONE HERE
    private void updateUI() {
        //added log messages for test purposes
        for (int i = 0; i < currentGameList.size(); i++) {
            Log.i("TEST", currentGameList.get(i).getAwayTeam() + " | "+ currentGameList.get(i).getAwayScore());
        }

    }





    //executes async task to update live scores every timer interval, running on ui thread
    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask asynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            //only fetch scores when network connection is available
                            if(isNetworkAvailable()) {
                                APICall apiScoreGetter = new APICall();
                                // PerformBackgroundTask this class is the class that extends AsynchTask
                                apiScoreGetter.execute();
                            }
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
