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
    }

    int getHomeLogo() {
        return mHomeLogo;
    }

    int getAwayLogo() {
        return mAwayLogo;
    }

    void UpdateCard(Game updatedData) {
        mHomeScore = updatedData.getHomeScore();
        mAwayScore = updatedData.getAwayScore();
        mLatestPlay = updatedData.getLastPlay();
        mQuarter = updatedData.getQuarter();
        mQuarterTime = updatedData.getQuarterTime();
    }

    boolean isUpdated(Game game) {
        boolean rc = false;
        if ((game.getHomeScore() != mHomeScore) |
                (game.getAwayScore() != mAwayScore) |
                (game.getQuarter() != mQuarter) |
                !(game.getQuarterTime().equals(mQuarterTime)) |
                !(game.getLastPlay().equals(mLatestPlay)))
            rc = true;
        return rc;
    }

    @Override
    public void Update(Game updatedData) {
        if (mHomeScore != updatedData.getHomeScore())
            mHomeScore = updatedData.getHomeScore();
        if (mAwayScore != updatedData.getAwayScore())
            mAwayScore = updatedData.getAwayScore();
        if (!(updatedData.getLastPlay().equals(mLatestPlay)))
            mLatestPlay = updatedData.getLastPlay();
        if (mQuarter != updatedData.getQuarter())
            mQuarter = updatedData.getQuarter();
        if (updatedData.getQuarterTime().equals(mQuarterTime))
            mQuarterTime = updatedData.getQuarterTime();

    }

    @Override
    public Boolean IsUpdated(Game updatedGame) {
        return !(updatedGame.getHomeTeam().equals(mHomeTeam));
    }
}
