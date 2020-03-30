package com.example.gametime;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameSimulator {

    Game g1;
    Game g2;
    Game g3;

    private ArrayList<Game> simulatedGames = new ArrayList<>();
    private ArrayList<Game> copy = new ArrayList<>();

    private boolean dataChanged = false;

    GameSimulator() {

        Game g1 = new Game("Atlanta Hawks",
                "New York Knicks",
                0, 0, 0,
                "", "Wed Mar 11 19:30:00 EDT 2020",
                "0:00");
        Game g2 = new Game("Dallas Mavericks",
                "Denver Nuggets",
                0, 0, 0,
                "", "Wed Mar 11 20:00:00 EDT 2020",
                "0:00");

        Game g3 = new Game("Sacramento Kings",
                "New Orleans Pelicans",
                0, 0, 0,
                "", "Wed Mar 11 22:30:00 EDT 2020",
                "0:00");
        simulatedGames.add(g1);
        simulatedGames.add(g2);
        simulatedGames.add(g3);
        copy = (ArrayList<Game>) simulatedGames.clone();

    }

    ArrayList<Game> GetData() {
        if (dataChanged) {
            simulatedGames = getGames();
            dataChanged = false;
        }
        return simulatedGames;
    }


    public void UpdateData(String homeTeam, String awayTeam, int homeScore, int awayScore, int q, String lp, String qt) {
        if (!copy.isEmpty()) {
            copy.remove(0);
            copy.remove(1);
        }
        g1 = new Game("Atlanta Hawks",
                "New York Knicks",
                3, 4, 1,
                "TimeOut", "Wed Mar 11 19:30:00 EDT 2020",
                "00:50");
        copy.add(0,g1);

        g2 = new Game("Dallas Mavericks",
                "Denver Nuggets",
                1, 20, 1,
                "", "Wed Mar 11 20:00:00 EDT 2020",
                "01:20");
        copy.add(1, g2);


        dataChanged = true;
    }

    public void UpdateData() {
//        copy.remove(0);
        g1 = new Game("Atlanta Hawks",
                "New York Knicks",
                10, 5, 1,
                "TimeOut", "Wed Mar 11 19:30:00 EDT 2020",
                "00:50");
        copy.add(0,g1);

//        copy.remove(1);
        g2 = new Game("Dallas Mavericks",
                "Denver Nuggets",
                15, 20, 1,
                "", "Wed Mar 11 20:00:00 EDT 2020",
                "01:20");
        copy.add(1, g2);


        dataChanged = true;
    }

    private ArrayList<Game> getGames() {
        return copy;
    }
}
