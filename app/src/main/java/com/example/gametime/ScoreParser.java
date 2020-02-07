package com.example.gametime;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScoreParser {
    private static ArrayList<Game> allGames = new ArrayList<>();
    //json tags to be retrieved and parsed
    private static String HOME_NAME = "homeName";
    private static String AWAY_NAME = "awayName";
    private static String HOME_SCORE = "homeScore";
    private static String AWAY_SCORE = "awayScore";
    private static String QUARTER = "status";
    private static String LATEST_PLAY = "explain";
    private static String MATCH_TIME = "matchTime";
    private static String QUARTER_TIME = "quarterRemainTime";

    public ScoreParser() {

    }


    public static ArrayList<Game> parseGames(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONArray games = jsonObj.getJSONArray("data");

            for (int i = 0; i < games.length(); i++) {
                JSONObject g = games.getJSONObject(i);

                String homeName = g.getString(HOME_NAME);
                String awayName = g.getString(AWAY_NAME);
                int homeScore = g.getInt(HOME_SCORE);
                int awayScore = g.getInt(AWAY_SCORE);
                int quarter = g.getInt(QUARTER);
                String latestPlay = g.getString(LATEST_PLAY);
                int matchTime = g.getInt(MATCH_TIME);
                String quarterTime = g.getString(QUARTER_TIME);

                allGames.add(new Game(homeName, awayName, homeScore, awayScore, quarter, latestPlay, matchTime, quarterTime));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return allGames;
    }


}
