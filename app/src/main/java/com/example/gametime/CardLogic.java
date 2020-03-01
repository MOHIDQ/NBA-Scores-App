package com.example.gametime;

import android.graphics.drawable.Drawable;

public class CardLogic {
    private int mHomeLogo;
    private int mAwayLogo;
    private String mHomeTeam;
    private String mAwayTeam;
    private String mHomeScore;
    private String mAwayScore;
    private String mLatestPlay;
    private String mQuarter;
    private String mQuarterTime;
    private String mMatchTime;

    public CardLogic(Game gameData, int homeLogo, int awayLogo ) {
        mHomeLogo = homeLogo;
        mAwayLogo = awayLogo;
        mHomeTeam = gameData.getHomeTeam();
        mAwayTeam = gameData.getAwayTeam();
        mHomeScore = gameData.getHomeScore() + "";
        mAwayScore = gameData.getAwayScore() + "";
        mLatestPlay = gameData.getLastPlay();
        mQuarter = gameData.getQuarter() + "";
        mQuarterTime = gameData.getQuarterTime() + "";
        mMatchTime = gameData.getMatchTime() + "";
    }

    public String getMatchTime() {
        return mMatchTime;
    }

    public int getHomeLogo() {
        return mHomeLogo;
    }

    public int getAwayLogo() {
        return mAwayLogo;
    }

    //getters
    public String getHomeTeam() {
        return mHomeTeam;
    }

    public String getAwayTeam() {
        return mAwayTeam;
    }

    public String getHomeScore() { return mHomeScore; }

    public String getAwayScore() {
        return mAwayScore;
    }

    public String getLatestPlay() {
        return mLatestPlay;
    }

    public String getQuarter() { return mQuarter; }

    public String getQuarterTime() {
        return mQuarterTime;
    }

    //setters
    public void setHomeTeam(String newHT) { this.mHomeTeam = newHT; }

    public void setAwayTeam(String newAT) { this.mAwayTeam = newAT; }

    void UpdateCard(Game updatedData)
    {
        mHomeTeam = updatedData.getHomeTeam();
        mAwayTeam = updatedData.getAwayTeam();
        mHomeScore = String.valueOf(updatedData.getHomeScore());
        mAwayScore = String.valueOf(updatedData.getAwayScore());
        mLatestPlay = updatedData.getLastPlay();
        mQuarter = String.valueOf(updatedData.getQuarter());
        mQuarterTime = updatedData.getQuarterTime();
    }
}
