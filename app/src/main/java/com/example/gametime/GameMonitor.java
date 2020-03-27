package com.example.gametime;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public interface GameMonitor {

    List<EventStream> listeners = new ArrayList<EventStream>();

    void UpdateData(Game updatedData, int id);

    boolean IsUpdated(Game updatedGame);

    void Register(EventStream e);
}
