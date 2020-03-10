package com.example.gametime;

import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView.Adapter mAdapter;

    public CardLogic(Game gameData, int homeLogo, int awayLogo, RecyclerView.Adapter adapter) {

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
        mAdapter = adapter;
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
    public void Update(Game updatedData, int id) {

        // TODO: redo this to call notify on only items changed
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

        if (itemChanged)
            mAdapter.notifyItemChanged(id);
    }

    @Override
    public boolean IsUpdated(Game updatedGame) {
        return !(updatedGame.getHomeTeam().equals(mHomeTeam));
    }
}
