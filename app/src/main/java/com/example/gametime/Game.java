package com.example.gametime;

import java.util.Date;

public class Game {
    private String homeTeam;
    private String awayTeam;

    private int homeScore;
    private int awayScore;

    private int quarter; //gonna use status key from JSON
    //0: Not started
    //1: The first quarter
    //2: The second quarter
    //3: The third quarter
    //4: The fourth quarter
    //5: The first OT
    //6: The second OT
    //7: The third OT
    //50: Half-time
    //-1: Finished
    //-2: TBD
    //-3: Interrupted
    //-4: Cancelled
    //-5: Postponed

    private String latestPlay;

    private String matchTime; //time game started

    private String quarterTime;

    public Game(String ht, String at, int hs, int as, int q, String lp, String mt, String qt) {
        homeTeam = ht;
        awayTeam = at;
        homeScore = hs;
        awayScore = as;
        quarter = q;
        latestPlay = lp;
        matchTime = mt;
        quarterTime = qt;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public int getQuarter() {
        return quarter;
    }

    public String getLastPlay() {
        return latestPlay;
    }

    public String getMatchTime() {
        return matchTime;
    }
    public String getQuarterTime() {
        return quarterTime;
    }

}
