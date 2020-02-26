package com.example.gametime;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.gametime.App.GAMESCORES_CHANNEL_ID;

class ScoreNotification {

    private NotificationCompat.Builder mNotification;
    private Notification mSummaryNotification;
    private NotificationManagerCompat mManager;

    private String mHomeTeam;
    private String mAwayTeam;
    private int mHomeScore;
    private int mAwayScore;
    private String mLatestPlay;

    ScoreNotification(MainActivity main, NotificationManagerCompat manager, String homeTeam, String awayTeam, int homeScore, int awayScore, String latestPlay)
    {
        mHomeTeam = homeTeam;
        mAwayTeam = awayTeam;
        mHomeScore = homeScore;
        mAwayScore = awayScore;
        mLatestPlay = latestPlay;
        mManager = manager;

        Intent activityIntent = new Intent(main, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(main,
                0, activityIntent, 0);

        mNotification = new NotificationCompat.Builder(main, GAMESCORES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup("scores_group")
                .setColor(Color.BLUE)
//                .setOngoing(true)
                .setContentIntent(pendingIntent);

        mSummaryNotification = new NotificationCompat.Builder(main, GAMESCORES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setStyle(new NotificationCompat.InboxStyle()
                        .setSummaryText("Summary"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup("scores_group")
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build();
    }

    void Notify(int id)
    {
        mNotification.setContentTitle(mHomeTeam + " " + mHomeScore + " - " + mAwayScore + " " + mAwayTeam);
        mNotification.setContentText(mLatestPlay);
        mManager.notify(id+20, mNotification.build());
        mManager.notify(10, mSummaryNotification);
    }

    void SetNotifHomeScore(int score) { mHomeScore = score; };
    void SetNotifAwayScore(int score) { mAwayScore = score; };
    void SetNotifLatestPlay(String play)
    {
        mLatestPlay = play;
    }

    int GetCurrHomeScore() { return mHomeScore; };
    int GetCurrAwayScore() { return mAwayScore; };
    String GetCurrLatestPlay() { return mLatestPlay;};
}
