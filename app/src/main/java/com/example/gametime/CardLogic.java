package com.example.gametime;

public class CardLogic extends Game{
    private int mHomeLogo;
    private int mAwayLogo;
    private int mHomeScore;
    private int mAwayScore;
    private String mLatestPlay;
    private int mQuarter;
    private String mQuarterTime;

    public CardLogic(Game gameData, int homeLogo, int awayLogo ) {

        super(
                gameData.getHomeTeam(),
                gameData.getAwayTeam(),
                gameData.getHomeScore(),
                gameData.getAwayScore(),
                gameData.getQuarter(),
                gameData.getLastPlay(),
                gameData.getMatchTime(),
                gameData.getQuarterTime());
        mHomeLogo = homeLogo;
        mAwayLogo = awayLogo;
    }

    int getHomeLogo() {
        return mHomeLogo;
    }
    int getAwayLogo() {
        return mAwayLogo;
    }

    void UpdateCard(Game updatedData)
    {
        mHomeScore = updatedData.getHomeScore();
        mAwayScore = updatedData.getAwayScore();
        mLatestPlay = updatedData.getLastPlay();
        mQuarter = updatedData.getQuarter();
        mQuarterTime = updatedData.getQuarterTime();
    }

    boolean isUpdated(Game game)
    {
        boolean rc = false;
        if ((game.getHomeScore() != mHomeScore) |
                (game.getAwayScore() != mAwayScore) |
                (game.getQuarter() != mQuarter) |
                !(game.getQuarterTime().equals(mQuarterTime)) |
                !(game.getLastPlay().equals(mLatestPlay)))
            rc = true;
        return rc;
    }
}
