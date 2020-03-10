package com.example.gametime;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.gametime.App.GAMESCORES_CHANNEL_ID;

class ScoreNotification extends Game implements GameMonitor {

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
                .setGroup("scores_group")
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent);

        mSummaryNotification = new NotificationCompat.Builder(mainActivity, GAMESCORES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setStyle(new NotificationCompat.InboxStyle()
                        .setSummaryText("Summary"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup("scores_group")
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build();
    }

    private void Notify(int id) {
        // update the notification content
        mNotification.setContentTitle(mHomeTeam + " " + mHomeScore + " - " + mAwayScore + " " + mAwayTeam);
        mNotification.setContentText(mLatestPlay);

        mManager.notify(id + 20, mNotification.build());
        mManager.notify(10, mSummaryNotification);
    }

//    void UpdateNotification (Game updatedData, int id, String timeRemaining, String pointD, String quater)
//    {
//        if (mHomeScore != updatedData.getHomeScore())
//            mHomeScore = updatedData.getHomeScore();
//        if (mAwayScore!= updatedData.getAwayScore())
//            mAwayScore = updatedData.getAwayScore();
//        if (!(updatedData.getLastPlay().equals(mLatestPlay)))
//            mLatestPlay = updatedData.getLastPlay();
//
//        int pointDiff = Math.abs(updatedData.getHomeScore() - updatedData.getAwayScore());
//
////        if (updatedData.getQuarter() == Integer.parseInt(quater)) {
//            if (Integer.parseInt(pointD) > pointDiff || Integer.parseInt(pointD) == pointDiff) {
//                Notify(id);
//            }
////        }
//
//        //TODO: Add fav team criteria
//        if (updatedData.getQuarter() == 4)
//        {
////            if (Integer.parseInt(timeRemaining.replace(":00","")) < Integer.parseInt(updatedData.getQuarterTime()))
////            {
//                Notify(id);
////            }
//        }
//    }

    @Override
    public void Update(Game updatedData, int id) {

        if (mHomeScore != updatedData.getHomeScore())
            mHomeScore = updatedData.getHomeScore();
        if (mAwayScore != updatedData.getAwayScore())
            mAwayScore = updatedData.getAwayScore();
        if (!(updatedData.getLastPlay().equals(mLatestPlay)))
            mLatestPlay = updatedData.getLastPlay();

        int pointDiff = Math.abs(updatedData.getHomeScore() - updatedData.getAwayScore());

//        if (updatedData.getQuarter() == Integer.parseInt(quater)) {
        if (mdb.getScoreDifferential() < pointDiff || mdb.getScoreDifferential() == pointDiff) {
            Notify(id);
        }
//        }

        //TODO: Add fav team criteria
        if (updatedData.getQuarter() > 0) {
//            if (Integer.parseInt(timeRemaining.replace(":00","")) < Integer.parseInt(updatedData.getQuarterTime()))
//            {
            Notify(id);
//            }
        }
    }

    @Override
    public boolean IsUpdated(Game updatedGame) {
        return !(updatedGame.getHomeTeam().equals(mHomeTeam));
    }
}