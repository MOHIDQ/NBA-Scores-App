package com.example.gametime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<CardLogic> exampleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //currentGameList = new ArrayList<>();
        notificationManager = NotificationManagerCompat.from(this);


//        exampleList.add(new CardLogic("6:00pm", R.drawable.toronto_raptors, R.drawable.lakers, "Rapotors", "Lakers", "35", "23", "Lebron made a 3-point play", "Q2", "1:30"));
//        exampleList.add(new CardLogic("8:00pm", R.drawable.milwaukee_bucks, R.drawable.miami_heat, "Bucks", "Heat", "46", "30", "Buttler blocked by Giannis", "Q3", "2:05"));
//        exampleList.add(new CardLogic("9:00pm", R.drawable.celtics, R.drawable.golden_state_warriors, "Celtics", "Warriors", "33", "25", "3 points by Walker", "Q2", "4:11"));
//        exampleList.add(new CardLogic("8:00pm", R.drawable.okc, R.drawable.bulls, "Thunder", "Bulls", "54", "40", "Foul called on Chris Paul", "Q4", "3:40"));

/*

        currentGameList.add(new Game("Raptors", "Lakers", 35, 23, 2, "Lebron made a 3-point play", 700, "1:30"));
        currentGameList.add(new Game("Bucks", "Heat", 45, 40, 4, "Buttler blocked by Giannis", 900, "2:05"));
        currentGameList.add(new Game("Warriors", "Celtics", 12, 15, 1, "3 points by Walker", 1000, "4:11"));
        //currentGameList.add(new Game("8:00pm", R.drawable.okc, R.drawable.bulls, "Thunder", "Bulls", "54", "40", "Foul called on Chris Paul", "Q4", "3:40"));
*/


        dataBaseTester();

        //if user has active internet connection get live scores
        // if(isNetworkAvailable()) {
        callAsynchronousTask();
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
/*
            if (currentGameList.get(i).getQuarter() > -1) {
                mRecyclerView = findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(this);
                //mAdapter = new Adapter(exampleList);
                mAdapter = new Adapter(currentGameList);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }*/

            if (exampleList.size() < currentGameList.size())
            {
                exampleList.add(new CardLogic(currentGameList.get(i).getMatchTime().toString(),
                                                R.drawable.okc, R.drawable.bulls,
                        currentGameList.get(i).getHomeTeam(), currentGameList.get(i).getAwayTeam(),
                        currentGameList.get(i).getHomeScore(), currentGameList.get(i).getAwayScore(),
                        currentGameList.get(i).getLastPlay(),
                        currentGameList.get(i).getQuarter(),
                        currentGameList.get(i).getQuarterTime()));
            }
            if (currentGameList.get(i).getQuarter() > -1) {
                mRecyclerView = findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(this);
                mAdapter = new Adapter(exampleList);
                //mAdapter = new Adapter(currentGameList);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
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