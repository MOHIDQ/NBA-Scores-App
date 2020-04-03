package com.example.gametime;

public class CardLogic extends Game implements GameMonitor {
    private String mHomeTeam;
    private String mAwayTeam;
    private int mHomeLogo;
    private int mAwayLogo;
    private int mHomeScore;
    private int mAwayScore;
    private String mLatestPlay;
    private int mQuarter;
    private String mMatchTime;
    private String mQuarterTime;

    public CardLogic(Game gameData, int homeLogo, int awayLogo) {

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
        mHomeLogo = homeLogo;
        mAwayLogo = awayLogo;
        mMatchTime = gameData.getMatchTime();
    }

    @Override
    public void UpdateData(Game updatedData, int id) {
        boolean itemChanged = false;

        if (mHomeScore != updatedData.getHomeScore()) {
            mHomeScore = updatedData.getHomeScore();
            itemChanged = true;
        }
        if (mAwayScore != updatedData.getAwayScore()) {
            mAwayScore = updatedData.getAwayScore();
            itemChanged = true;
        }
        if (!(updatedData.getLastPlay().equals(mLatestPlay))) {
            mLatestPlay = updatedData.getLastPlay();
            itemChanged = true;
        }
        if (mQuarter != updatedData.getQuarter()) {
            mQuarter = updatedData.getQuarter();
            itemChanged = true;
        }
        if (!(updatedData.getQuarterTime().equals(mQuarterTime))) {
            mQuarterTime = updatedData.getQuarterTime();
            itemChanged = true;
        }

        if (itemChanged) {
            for (EventStream e : listeners) {
                e.PostData(id);
            }
        }
    }

    //getters for the adapter
    String GetMatchTime() {
        return mMatchTime;
    }

    int GetHomeLogo() {
        return mHomeLogo;
    }

    int GetAwayLogo() {
        return mAwayLogo;
    }

    String GetHomeTeam() {
        return mHomeTeam;
    }

    String GetAwayTeam() {
        return mAwayTeam;
    }

    String GetHomeScore() {
        return Integer.toString(mHomeScore);
    }

    String GetAwayScore() {
        return Integer.toString(mAwayScore);
    }

    String GetLatestPlay() {
        return mLatestPlay;
    }

    int GetQuarter() {
        return mQuarter;
    }

    String GetQuarterTime() {
        return mQuarterTime;
    }

    @Override
    public boolean IsUpdated(Game updatedGame) {
        return !(updatedGame.getHomeTeam().equals(mHomeTeam));
    }

    @Override
    public void Register(EventStream e) {
        listeners.add(e);
    }
}
