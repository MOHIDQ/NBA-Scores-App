package com.example.gametime;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.gametime.App.GAMESCORES_CHANNEL_ID;
import static com.example.gametime.DatabaseHelper.DEFAULT_FAV_TEAM;

class ScoreNotification extends Game implements GameMonitor {

    private static final String SCORES_GROUP = "scores_group";
    private static final int GAME_NOT_STARTED = 0;
    private static final int GAME_FINISHED = -1;
    private static final int GAME_Q4 = 4;
    private static final int MANAGER_ID = 10;
    private static final int NOTIFICATION_INCREMENTAL = 20;

    private NotificationCompat.Builder mNotification;
    private Notification mSummaryNotification;
    private NotificationManagerCompat mManager;

    private String mHomeTeam;
    private String mAwayTeam;
    private int mHomeScore;
    private int mAwayScore;
    private String mLatestPlay;

    private DatabaseHelper mdb;


    ScoreNotification(MainActivity mainActivity, NotificationManagerCompat manager, Game gameData, DatabaseHelper db) {
        super(
                gameData.getHomeTeam(),
                gameData.getAwayTeam(),
                gameData.getHomeScore(),
                gameData.getAwayScore(),
                gameData.getQuarter(),
                gameData.getLastPlay(),
                gameData.getMatchTime(),
                gameData.getQuarterTime());

        mHomeTeam = gameData.getHomeTeam();
        mAwayTeam = gameData.getAwayTeam();
        mManager = manager;

        mdb = db;

        Intent activityIntent = new Intent(mainActivity, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mainActivity,
                0, activityIntent, 0);

        mNotification = new NotificationCompat.Builder(mainActivity, GAMESCORES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup(SCORES_GROUP)
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent);

        mSummaryNotification = new NotificationCompat.Builder(mainActivity, GAMESCORES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setStyle(new NotificationCompat.InboxStyle()
                        .setSummaryText("Summary"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup(SCORES_GROUP)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build();
    }

    private void NotifyUser(int id) {
        // update the notification content
        mNotification.setContentTitle(mHomeTeam + " " + mHomeScore + " - " + mAwayScore + " " + mAwayTeam);
        mNotification.setContentText(mLatestPlay);

        mManager.notify(id + NOTIFICATION_INCREMENTAL, mNotification.build());
        mManager.notify(MANAGER_ID, mSummaryNotification);
    }

    @Override
    public void UpdateData(Game updatedData, int id) {

        int userPointdiff = mdb.getScoreDifferential();
        int userTimeRemaining = mdb.getTimeRemaining();
        int userQuarter = mdb.getQuarter();
        String userTeam = mdb.getFavouriteTeam();

        boolean dataChanged = false;
        boolean notify = false;
        boolean teamSelected = true;

        if (mHomeScore != updatedData.getHomeScore()) {
            mHomeScore = updatedData.getHomeScore();
            dataChanged = true;
        }
        if (mAwayScore != updatedData.getAwayScore()) {
            mAwayScore = updatedData.getAwayScore();
            dataChanged = true;
        }
        if (!(updatedData.getLastPlay().equals(mLatestPlay))) {
            mLatestPlay = updatedData.getLastPlay();
            dataChanged = true;
        }

        int pointDiff = Math.abs(updatedData.getHomeScore() - updatedData.getAwayScore());

        if (userTeam.equals(DEFAULT_FAV_TEAM))
            teamSelected = false;

        // using default values
        if (teamSelected) {
            if (userTeam.equals(updatedData.getHomeTeam()) || userTeam.equals(updatedData.getAwayTeam())) {
                // notify if the fav team selected wins
                if (updatedData.getQuarter() == GAME_NOT_STARTED)
                    notify = true;
                else if (updatedData.getQuarter() == GAME_FINISHED) {
                    if (userTeam.equals(updatedData.getHomeTeam()) && updatedData.getHomeScore() > updatedData.getAwayScore())
                        notify = true;
                    else if (userTeam.equals(updatedData.getAwayTeam()) && updatedData.getAwayScore() > updatedData.getHomeScore())
                        notify = true;
                }
                // specific points in a given quarter
                else if (updatedData.getQuarter() == userQuarter) {
                    if (userPointdiff > pointDiff || userPointdiff == pointDiff) {
                        notify = true;
                    }
                }
            }
        } else if (userQuarter == updatedData.getQuarter()) {
            if (userPointdiff > pointDiff || userPointdiff == pointDiff) {
                notify = true;
            }
        }

        if (updatedData.getQuarter() == GAME_Q4) {
            int timeDiff = 12 - Integer.parseInt(updatedData.getQuarterTime().replace(":00", ""));
            if (timeDiff < userTimeRemaining)
                notify = true;
        }

        if (dataChanged && notify)
            NotifyUser(id);
    }

    @Override
    public boolean IsUpdated(Game updatedGame) {
        return !(updatedGame.getHomeTeam().equals(mHomeTeam));
    }

    @Override
    public void Register(EventStream e) {
    }
}