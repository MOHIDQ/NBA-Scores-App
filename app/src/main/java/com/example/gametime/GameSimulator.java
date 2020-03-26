package com.example.gametime;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

public class GameSimulator {

    Game g1;
    Game g2;
    Game g3;

    private ArrayList<Game> simulatedGames = new ArrayList<>();

    GameSimulator() {

        Game g1 = new Game("Atlanta Hawks",
                "New York Knicks",
                0, 0, 0,
                "", "Wed Mar 11 19:30:00 EDT 2020",
                "");
        Game g2 = new Game("Dallas Mavericks",
                "Denver Nuggets",
                0, 0, 0,
                "", "Wed Mar 11 20:00:00 EDT 2020",
                "");

        Game g3 = new Game("Sacramento Kings",
                "New Orleans Pelicans",
                0, 0, 0,
                "", "Wed Mar 11 22:30:00 EDT 2020",
                "");

        simulatedGames.add(g1);
        simulatedGames.add(g2);
        simulatedGames.add(g3);
    }

    ArrayList<Game> GetData() {

        for (Game g : simulatedGames) {

        }
        return simulatedGames;
    }

}
