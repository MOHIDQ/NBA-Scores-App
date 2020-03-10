package com.example.gametime;

public interface GameMonitor {

    void Update(Game updatedData, int id);

    boolean IsUpdated(Game updatedGame);
}
