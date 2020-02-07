package com.example.gametime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String STRING_URL = "http://api.isportsapi.com/sport/basketball/livescores?api_key=e2inHJ71Nvpk49T8";
    private ArrayList<Game> currentGameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        currentGameList = new ArrayList<>();

        if(isNetworkAvailable()) {
            APICall call = new APICall();
            call.execute();
        }
        //condition for when network connection is not available
        else {
            Log.i("TEST", "NO INTERNET");
        }

    }

    //if network connection is available run async task for getting scores data
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }








    //async task for GET requests to REST API
    private class APICall extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
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


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            currentGameList = ScoreParser.parseGames(result);

            //for (int i = 0; i < currentGameList.size(); i++) {
              //  Log.i("TEST", currentGameList.get(i).getAwayTeam());
            //}
            //Log.i("TEST", result);
        }
    }
}
