package com.example.gametime;

import java.util.ArrayList;

public interface GameMonitor {

    void Update(Game updatedData);
    Boolean IsUpdated (Game updatedGame);
}
