package com.example.gametime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.database.Cursor;
import android.os.Bundle;

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
    public ArrayList<Game> currentGameList; //contain all game data
    private NotificationManagerCompat notificationManager;
    public ArrayList<ScoreNotification> currNotificationList = new ArrayList<>();
    private DatabaseHelper db;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<CardLogic> cardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentGameList = new ArrayList<>();
        notificationManager = NotificationManagerCompat.from(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        dataBaseTester();

        //if user has active internet connection get live scores
       // if (isNetworkAvailable()) {
            callAsynchronousTask();
     //   }
//        condition for when network connection is not available
      //  else {
            Log.i("TEST", "NO INTERNET");
      //  }
    }

    private void dataBaseTester() {
        db = new DatabaseHelper(this);

        Cursor result = db.getData();
        if (result.getCount() == 0) {
            //show message
            Log.i("CURSOR", "NO DATA");
        } else {
            StringBuffer buffer = new StringBuffer();
            while (result.moveToNext()) {
                buffer.append("time: " + result.getString(0) + "\n");
                buffer.append("points: " + result.getString(1) + "\n");
                buffer.append("team: " + result.getString(2) + "\n");
            }
            Log.i("CURSOR", buffer.toString());
        }
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
            Log.i("TEST", currentGameList.get(i).getAwayTeam() + " | " + currentGameList.get(i).getAwayScore());

            // TODO: Test overnight 12 at night
            if (!cardList.isEmpty() && !(cardList.get(0).getHomeTeam().equals(currentGameList.get(0).getHomeTeam())))
                cardList.clear();

            if (cardList.size() < currentGameList.size()) {

                // get the image from the folder and pass it in
                String homeName = currentGameList.get(i).getHomeTeam().replaceAll(" ","_").toLowerCase();
                int homeLogo = getResources().getIdentifier(homeName, "drawable", getPackageName());
                String awayName = currentGameList.get(i).getAwayTeam().replaceAll(" ","_").toLowerCase();
                int awayLogo = getResources().getIdentifier(awayName, "drawable", getPackageName());

                cardList.add(new CardLogic(currentGameList.get(i), homeLogo, awayLogo));

                mAdapter = new Adapter(cardList);
                mRecyclerView.setAdapter(mAdapter);
            }

            if (currentGameList.get(i).getQuarter() > -6) {
                if ((currentGameList.get(i).getHomeScore() != Integer.parseInt(cardList.get(i).getHomeScore())) |
                        (currentGameList.get(i).getAwayScore() != Integer.parseInt(cardList.get(i).getAwayScore())) |
                        (currentGameList.get(i).getQuarter() != Integer.parseInt(cardList.get(i).getQuarter())) |
                        !(currentGameList.get(i).getQuarterTime().equals(cardList.get(i).getQuarterTime())) |
                        !(currentGameList.get(i).getLastPlay().equals(cardList.get(i).getLatestPlay())))
                {
                    cardList.get(i).UpdateCard(currentGameList.get(i));
                    mAdapter.notifyItemChanged(i);
                }
            }
                if (currNotificationList.size() < currentGameList.size()) {
                    ScoreNotification not = new ScoreNotification(this, notificationManager, currentGameList.get(i));

                    // Uncomment if need to be notified of all current games
                    //not.Notify(i);
                    currNotificationList.add(not);
                }

                // TODO: Change if parameters to modify when notifications are sent
                if (currentGameList.get(i).getQuarter() > 0) {

                    // only notifies if the home score, away score or latest play have been updated
                    if (currNotificationList.get(i).GetCurrHomeScore() != currentGameList.get(i).getHomeScore() ||
                            currNotificationList.get(i).GetCurrAwayScore() != currentGameList.get(i).getAwayScore() ||
                            !(currNotificationList.get(i).GetCurrLatestPlay().equals(currentGameList.get(i).getLastPlay()))) {

                        currNotificationList.get(i).UpdateNotification(currentGameList.get(i));
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
                            if (isNetworkAvailable()) {
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


