package com.example.gametime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public List<Game> currentGameList; //contain all game data
    private NotificationManagerCompat notificationManager;
    public ArrayList<ArrayList<GameMonitor>> monitors = new ArrayList<>();
    public ArrayList<GameMonitor> currNotificationList = new ArrayList<>();
    public ArrayList<GameMonitor> cardList = new ArrayList<>();
    private DatabaseHelper db;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

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
        // Log.i("TEST", "NO INTERNET");
        //  }
    }

    private void dataBaseTester() {
        db = new DatabaseHelper(this);
        Log.i("CURSOR", db.getTimeRemaining() + "     " + db.getScoreDifferential() + "         " + db.getFavouriteTeam() + "      " + db.getQuarter());
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

            // TODO: Test overnight
            if (!cardList.isEmpty() && (cardList.get(0).IsUpdated(currentGameList.get(0)))) {
                cardList.clear();
                currNotificationList.clear();
                monitors.clear();
            }
            if (cardList.size() != currentGameList.size() || currNotificationList.size() != currentGameList.size()) {

                // get the image from the folder and pass it into the card
                String homeName = currentGameList.get(i).getHomeTeam().replaceAll(" ", "_").toLowerCase();
                int homeLogo = getResources().getIdentifier(homeName, "drawable", getPackageName());
                String awayName = currentGameList.get(i).getAwayTeam().replaceAll(" ", "_").toLowerCase();
                int awayLogo = getResources().getIdentifier(awayName, "drawable", getPackageName());

                mAdapter = new Adapter(cardList);

                cardList.add(new CardLogic(currentGameList.get(i), homeLogo, awayLogo, mAdapter));
                monitors.add(cardList);

                mRecyclerView.setAdapter(mAdapter);

                ScoreNotification not = new ScoreNotification(this, notificationManager, currentGameList.get(i), db);

                // Uncomment if need to be notified of all current games
                //not.Notify(i);
                currNotificationList.add(not);

                monitors.add(currNotificationList);
            }

            Notify(currentGameList.get(i), i);
        }
    }

    public void Notify(Game currentGame, int index) {
        for (ArrayList<GameMonitor> monitor : monitors) {
            monitor.get(index).Update(currentGame, index);
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
                                currentGameList.clear();
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


