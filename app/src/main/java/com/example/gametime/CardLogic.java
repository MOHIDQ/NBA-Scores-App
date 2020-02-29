package com.example.gametime;

public class CardLogic {
    private int mHomeLogo;
    private int mAwayLogo;
    private String mHomeTeam;
    private String mAwayTeam;
    private int mHomeScore;
    private int mAwayScore;
    private String mLatestPlay;
    private int mQuarter;
    private String mQuarterTime;
    private String mMatchTime;

    public CardLogic(String matchTime, int homeLogo, int awayLogo, String homeTeam, String awayTeam, int homeScore, int awayScore, String latestPlay, int quarter, String quarterTime) {
        mHomeLogo = homeLogo;
        mAwayLogo = awayLogo;
        mHomeTeam = homeTeam;
        mAwayTeam = awayTeam;
        mHomeScore = homeScore;
        mAwayScore = awayScore;
        mLatestPlay = latestPlay;
        mQuarter = quarter;
        mQuarterTime = quarterTime;
        mMatchTime = matchTime;
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

    public int getHomeScore() {
        return mHomeScore;
    }

    public int getAwayScore() {
        return mAwayScore;
    }

    public String getLatestPlay() {
        return mLatestPlay;
    }

    public int getQuarter() {
        return mQuarter;
    }

    public String getQuarterTime() {
        return mQuarterTime;
    }

    //setters
    public void setHomeTeam(String newHT) { this.mHomeTeam = newHT; }

    public void setAwayTeam(String newAT) { this.mAwayTeam = newAT; }

    public void setHomeScore(int newHS) { this.mHomeScore= newHS; }

    public void setAwayScore(int newAS) { this.mAwayScore = newAS; }

    public void setLatestPlay(String newLP) { this.mLatestPlay = newLP; }

    public void setQuarter(int newQuarter) { this.mQuarter = newQuarter; }

    public void setQuarterTime(String newQT) { this.mQuarterTime = newQT; }

}
