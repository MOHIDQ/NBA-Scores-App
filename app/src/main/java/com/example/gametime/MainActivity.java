package com.example.gametime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.os.Bundle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.Spinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.gametime.DatabaseHelper.DEFAULT_FAV_TEAM;

public class MainActivity extends AppCompatActivity implements EventStream {
    public static final String OK = "Ok";
    public static final String DISMISS = "Dismiss";
    public List<Game> currentGameList; //contain all game data
    private NotificationManagerCompat notificationManager;
    public ArrayList<ArrayList<GameMonitor>> monitors = new ArrayList<>();
    public ArrayList<GameMonitor> currNotificationList = new ArrayList<>();
    public ArrayList<GameMonitor> cardList = new ArrayList<>();
    private DatabaseHelper db;

    private Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private GameSimulator simulator;
    Button showAlert;

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

        simulator = new GameSimulator();

        dataBaseTester();
        BuildRecyclerView();

        //prompt when user loads app with no internet connection
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "NO INTERNET", Toast.LENGTH_LONG).show();
        }

        //if user has active internet connection get live scores
        // if (isNetworkAvailable()) {
        callAsynchronousTask();

        //   }
//        condition for when network connection is not available
        //  else {
        // Log.i("TEST", "NO INTERNET");
        //  }

        createSpinner();
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

                cardList.add(new CardLogic(currentGameList.get(i), homeLogo, awayLogo));
                monitors.add(cardList);

                ScoreNotification not = new ScoreNotification(this, notificationManager, currentGameList.get(i),
                        db.getScoreDifferential(),
                        db.getTimeRemaining(),
                        db.getQuarter(),
                        db.getFavouriteTeam());

                // Uncomment if need to be notified of all current games
                //not.Notify(i);
                currNotificationList.add(not);
                mAdapter.notifyDataSetChanged();

                monitors.add(currNotificationList);
                for (ArrayList<GameMonitor> monitors : monitors) {
                    monitors.get(0).Register(this);
                }
            }
            Notify(currentGameList.get(i), i);
        }
    }

    public void Notify(Game currentGame, int index) {
        for (ArrayList<GameMonitor> monitor : monitors) {
            monitor.get(index).UpdateData(currentGame, index);
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

    @Override
    public void PostData(final int id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyItemChanged(id);
            }
        });
    }

    public void BuildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Adapter(cardList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    //async task for GET requests to REST API
    private class APICall extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return ScoreParser.getInstance().getDataFromAPI();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            currentGameList.clear();
            currentGameList = ScoreParser.getInstance().parseGames(result);

            // for simulation when no active games
            if(currentGameList.isEmpty())
                currentGameList = simulator.GetData();

            //condition if there are no games being played
            if (currentGameList.size() <= 0) {
                currentGameList.add(new Game("", "", -1, -1, 0, "", "", ""));
            }
            updateUI();
        }

    }

    private void createSpinner() {
        showAlert = findViewById(R.id.alertbutton);
        showAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this); //creating the alert dialog
                View myView = getLayoutInflater().inflate(R.layout.alert_spinner, null);
                dialogBuilder.setTitle("Set Preferences");

                final Spinner teamsSpinner = myView.findViewById(R.id.favTeamSpinner);
                final TextView favTeamText = myView.findViewById(R.id.favTeamText);
                favTeamText.append("Select Favroite Team");
                ArrayAdapter<String> teamsAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.teamsList));
                teamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                teamsSpinner.setAdapter(teamsAdapter);
                if (!db.getFavouriteTeam().equals(DEFAULT_FAV_TEAM)) {
                    int favTeamPos = teamsAdapter.getPosition(db.getFavouriteTeam());
                    teamsSpinner.setSelection(favTeamPos);
                }

                final Spinner timeRemainSpinner = myView.findViewById(R.id.timeReSpinner);
                final TextView timeRemainText = myView.findViewById(R.id.timeRemainText);
                timeRemainText.append("Select Game Time Remaining");
                ArrayAdapter<String> timeRemainAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.timesList));
                timeRemainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                timeRemainSpinner.setAdapter(timeRemainAdapter);
                int timeRemainPos = timeRemainAdapter.getPosition(String.valueOf(db.getTimeRemaining()).concat(":00"));
                timeRemainSpinner.setSelection(timeRemainPos);

                final EditText pointDiffField = myView.findViewById(R.id.pointDiff);
                final TextView pointDiffText = myView.findViewById(R.id.pointDiffText);
                pointDiffText.append("Enter Point Difference");
                pointDiffField.setText(String.valueOf(db.getScoreDifferential()));


                final Spinner quarterSpinner = myView.findViewById(R.id.quarterSpinner);
                final TextView quarterText = myView.findViewById(R.id.quarterText);
                quarterText.append("Select Quarter");
                ArrayAdapter<String> quarterAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.quarter));
                quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                quarterSpinner.setAdapter(quarterAdapter);
                int quarterPos = quarterAdapter.getPosition(String.valueOf(db.getQuarter()));
                quarterSpinner.setSelection(quarterPos);

                dialogBuilder.setPositiveButton(OK, new DialogInterface.OnClickListener() { //dealing with the user selecting "ok"
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        if (teamsSpinner.getSelectedItem() != null) {
                            String team = teamsSpinner.getSelectedItem().toString();

                            if (!team.equals(db.getFavouriteTeam())) {
                                Toast.makeText(MainActivity.this, "Favroite Team Selected: \n" + team, Toast.LENGTH_SHORT).show();
                                db.updateFavTeam(team);
                            }
                        }

                        if (timeRemainSpinner.getSelectedItem() != null) {
                            String time = timeRemainSpinner.getSelectedItem().toString().replace(":00", "");

                            if (!time.equals(String.valueOf(db.getTimeRemaining()))) {
                                Toast.makeText(MainActivity.this, "Time Difference Selected: " + time + " mins", Toast.LENGTH_SHORT).show();
                                db.updateTimeRemaining(time);
                            }
                        }

                        if (pointDiffField.getText() != null) {
                            int intPointDiffer = -1;
                            String strPointDiff = pointDiffField.getText().toString();

                            if (!strPointDiff.equals("") || !strPointDiff.equals(String.valueOf(db.getScoreDifferential()))) {
                                intPointDiffer = Integer.parseInt(strPointDiff);
                                if (intPointDiffer > 0 && intPointDiffer < 150 &&
                                        intPointDiffer != db.getScoreDifferential()) {
                                    Toast.makeText(MainActivity.this, "Point Difference Selected: " + strPointDiff, Toast.LENGTH_SHORT).show();
                                    db.updateScoreDifferential(strPointDiff);
                                }
                            }
                        }

                        if (quarterSpinner.getSelectedItem() != null) {
                            String quarter = quarterSpinner.getSelectedItem().toString();

                            if (!quarter.equals(String.valueOf(db.getQuarter()))) {
                                Toast.makeText(MainActivity.this, "Quarter Selected: " + quarter, Toast.LENGTH_SHORT).show();
                                db.updateQuarter(quarter);
                            }
                        }
                        dataBaseTester();
                        dialog.dismiss();
                    }
                });

                dialogBuilder.setNegativeButton(DISMISS, new DialogInterface.OnClickListener() { //dealing with the user selecting "dismiss"
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                dialogBuilder.setView(myView);
                AlertDialog myDialog = dialogBuilder.create(); //creating the Alert Dialog
                myDialog.show();
            }
        });
    }
}


