package com.example.gametime;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.gametime.App.GAMESCORES_CHANNEL_ID;

class ScoreNotification {

    private NotificationCompat.Builder mNotification;
    private Notification mSummaryNotification;
    private NotificationManagerCompat mManager;

    private String mHomeTeam = "";
    private String mAwayTeam = "";
    private int mHomeScore;
    private int mAwayScore;
    private String mLatestPlay;

    ScoreNotification(MainActivity mainActivity, NotificationManagerCompat manager, Game gameData)
    {
        mHomeTeam = gameData.getHomeTeam();
        mAwayTeam = gameData.getAwayTeam();
        mHomeScore = gameData.getHomeScore();
        mAwayScore = gameData.getAwayScore();
        mLatestPlay = gameData.getLastPlay();
        mManager = manager;

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

    private void Notify(int id)
    {
        // update the notification content
        mNotification.setContentTitle(mHomeTeam + " " + mHomeScore + " - " + mAwayScore + " " + mAwayTeam);
        mNotification.setContentText(mLatestPlay);

        //TODO: Myabe add some sort of sleep so that notifications dont come in at the same time. Ask team
        mManager.notify(id+20, mNotification.build());
        mManager.notify(10, mSummaryNotification);
    }

    void UpdateNotification (Game updatedData, int id)
    {
        if (mHomeScore != updatedData.getHomeScore())
            mHomeScore = updatedData.getHomeScore();
        if (mAwayScore!= updatedData.getAwayScore())
            mAwayScore = updatedData.getAwayScore();
        if (!(mLatestPlay.equals(updatedData.getLastPlay())))
            mLatestPlay = updatedData.getLastPlay();
        Notify(id);
    }
}
