package com.example.gametime;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.gametime.App.GAMESCORES_CHANNEL_ID;
import static com.example.gametime.App.GAMESCORES__ID;

class ScoreNotification {

private Notification mNotification;
private Notification mSummaryNotification;
private NotificationManagerCompat mManager;
private int mId;

    ScoreNotification(MainActivity main, NotificationManagerCompat manager, String homeTeam, String awayTeam, int homeScore, int awayScore, String latestPlay, int id)
    {
        mId = id;
        mManager = manager;
        String teams = homeTeam + " " + homeScore + " - " + awayScore + " " + awayTeam;

        Intent activityIntent = new Intent(main, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(main,
                0, activityIntent, 0);

        mNotification = new NotificationCompat.Builder(main, GAMESCORES_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle(teams)
                .setContentText(latestPlay)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup("scores_group")
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

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

    void Notify()
    {
        mManager.notify(mId, mNotification);
        mManager.notify(7,mSummaryNotification);
    }
}
