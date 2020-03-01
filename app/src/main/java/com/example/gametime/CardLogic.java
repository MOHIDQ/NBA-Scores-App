package com.example.gametime;

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

    String getMatchTime() {
        return mMatchTime;
    }

    int getHomeLogo() {
        return mHomeLogo;
    }
    int getAwayLogo() {
        return mAwayLogo;
    }

    //getters
   String getHomeTeam() {
        return mHomeTeam;
    }

    String getAwayTeam() {
        return mAwayTeam;
    }

    String getHomeScore() { return mHomeScore; }

    String getAwayScore() {
        return mAwayScore;
    }

    String getLatestPlay() {
        return mLatestPlay;
    }

    String getQuarter() { return mQuarter; }

    String getQuarterTime() {
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

    boolean isUpdated(Game game)
    {
        boolean rc = false;
        if ((game.getHomeScore() != Integer.parseInt(mHomeScore) |
                (game.getAwayScore() != Integer.parseInt(mAwayScore)) |
                (game.getQuarter() != Integer.parseInt(mQuarter)) |
                !(game.getQuarterTime().equals(mQuarterTime)) |
                !(game.getLastPlay().equals(mLatestPlay))))
            rc = true;
        return rc;
    }
}
