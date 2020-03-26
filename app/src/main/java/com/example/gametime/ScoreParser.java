package com.example.gametime;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoreParser {
    private static String STRING_URL = "http://api.isportsapi.com/sport/basketball/livescores?api_key=e2inHJ71Nvpk49T8";

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
    private static int numberOfGames;

    private static ScoreParser single_instance = new ScoreParser();

    private ScoreParser() {
        //nothing so far
    }

    public static ScoreParser getInstance() {
        return single_instance;
    }

    protected static List<Game> parseGames(String result) {
        try {
             JSONObject jsonObj = new JSONObject(result);

            JSONArray games = jsonObj.getJSONArray("data");
            numberOfGames = games.length();
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

                Date allMatchTime = new java.util.Date((long)matchTime*1000);
                String fullMatchTime = allMatchTime.toString();
                String matchTimeString = "Today: " + (Integer.parseInt(fullMatchTime.substring(11, 13)) % 12) + fullMatchTime.substring(13, 20) + "PM " + fullMatchTime.substring(20, 24);

                allGames.add(new Game(homeName, awayName, homeScore, awayScore, quarter, latestPlay, matchTimeString, quarterTime));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return allGames.subList(0, numberOfGames);
    }
    //function that retrieves JSON data and returns JSON data from REST API
    public static String getDataFromAPI() {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
        try {
            URL newUrl = new URL(STRING_URL);
            HttpURLConnection connection = (HttpURLConnection)newUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            connection.setRequestProperty("User-agent", userAgent);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
