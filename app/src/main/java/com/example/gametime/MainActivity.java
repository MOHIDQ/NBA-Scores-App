package com.example.gametime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;


import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Game> currentGameList = new ArrayList<>(); //contain all game data
    private NotificationManagerCompat notificationManager;
    public ArrayList<ScoreNotification> currNotificationList = new ArrayList<>();
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentGameList = new ArrayList<>();
        notificationManager = NotificationManagerCompat.from(this);

        dataBaseTester();

        //if user has active internet connection get live scores
//        if(isNetworkAvailable()) {
            callAsynchronousTask();
//        }
//        condition for when network connection is not available
//        else {
//            Log.i("TEST", "NO INTERNET");
//        }
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
        //createNotification("Raptors", "Lakers", 50,23, "Lebron James made a 3point play");
        ScoreNotification check = currNotificationList.get(0);
        ScoreNotification check2 = currNotificationList.get(1);
        check.Notify(0);
        //check2.Notify(5);
    }

    public void createNotification( String homeTeam, String awayTeam, int homeScore, int awayScore, String latestPlay)
    {
        //ScoreNotification not = new ScoreNotification(this, notificationManager, homeTeam, awayTeam, homeScore, awayScore, latestPlay, 0);
       // not.Notify();
        //ScoreNotification not2 = new ScoreNotification(this, notificationManager, homeTeam, awayTeam, homeScore, awayScore, latestPlay, 1);
        //not2.Notify();
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

            if (currNotificationList.size() < currentGameList.size()) {

                ScoreNotification not = new ScoreNotification(this, notificationManager,
                        currentGameList.get(i).getHomeTeam(),
                        currentGameList.get(i).getAwayTeam(),
                        currentGameList.get(i).getHomeScore() ,
                        currentGameList.get(i).getAwayScore(),
                        currentGameList.get(i).getLastPlay());

                // Uncomment if need to be notified of all current games
                //not.Notify(i);
                currNotificationList.add(not);
            }
            // TODO: Change if parameters to modify when notifications are sent
            if (currentGameList.get(i).getQuarter() > 0)
            {
                // only notifies if the home score, away score or latest play have been updated
                if (currNotificationList.get(i).GetCurrHomeScore() != currentGameList.get(i).getHomeScore() ||
                    currNotificationList.get(i).GetCurrAwayScore() != currentGameList.get(i).getAwayScore() ||
                    !(currNotificationList.get(i).GetCurrLatestPlay().equals( currentGameList.get(i).getLastPlay())))
                 {
                     currNotificationList.get(i).SetNotifHomeName(currentGameList.get(i).getHomeTeam());
                     currNotificationList.get(i).SetNotifAwayName(currentGameList.get(i).getAwayTeam());
                     currNotificationList.get(i).SetNotifHomeScore (currentGameList.get(i).getHomeScore());
                     currNotificationList.get(i).SetNotifAwayScore (currentGameList.get(i).getAwayScore());
                     currNotificationList.get(i).SetNotifLatestPlay(currentGameList.get(i).getLastPlay());

                     currNotificationList.get(i).Notify(i);
                }
            }


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
