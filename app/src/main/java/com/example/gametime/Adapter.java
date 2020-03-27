package com.example.gametime;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<GameMonitor> mExampleList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mHomeLogo;
        ImageView mAwayLogo;
        TextView mHomeTeam;
        TextView mAwayTeam;
        TextView mHomeScore;
        TextView mAwayScore;
        TextView mLatestPlay;
        TextView mQuarter;
        TextView mQuarterTime;
        TextView mMatchTime;

        ViewHolder(View itemView) {
            super(itemView);
            mHomeLogo = itemView.findViewById(R.id.homeLogo);
            mAwayLogo = itemView.findViewById(R.id.awayLogo);
            mHomeTeam = itemView.findViewById(R.id.homeTeam);
            mAwayTeam = itemView.findViewById(R.id.awayTeam);
            mHomeScore = itemView.findViewById(R.id.homeScore);
            mAwayScore = itemView.findViewById(R.id.awayScore);
            mLatestPlay = itemView.findViewById(R.id.latestPlay);
            mQuarter = itemView.findViewById(R.id.quarter);
            mQuarterTime = itemView.findViewById(R.id.quarterTime);
            mMatchTime = itemView.findViewById(R.id.matchTime);
        }
    }

    Adapter(ArrayList<GameMonitor> currentGameList) {
        mExampleList = currentGameList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardLogic currentItem = (CardLogic) mExampleList.get(position);

        holder.mHomeLogo.setImageResource(currentItem.GetHomeLogo());
        holder.mAwayLogo.setImageResource(currentItem.GetAwayLogo());
        holder.mHomeTeam.setText(currentItem.GetHomeTeam());
        holder.mAwayTeam.setText(currentItem.GetAwayTeam());
        if(currentItem.GetHomeScore() == "-1") {
            holder.mHomeScore.setText("");
        }
        else {
            holder.mHomeScore.setText(currentItem.GetHomeScore());
        }

        if(currentItem.GetAwayScore() == "-1") {
            holder.mAwayScore.setText("");
        }
        else{
            holder.mAwayScore.setText(currentItem.GetAwayScore());
        }
        if(currentItem.GetAwayScore() == "-1" && currentItem.GetHomeScore() == "-1") {
            holder.mQuarter.setText("NO GAMES");
            holder.mQuarter.setTextColor(Color.BLACK);

        }
        else {
            holder.mQuarter.setText(FormatQuarter(currentItem.GetQuarter()));
        }
        holder.mLatestPlay.setText(currentItem.GetLatestPlay());

        holder.mQuarterTime.setText(currentItem.GetQuarterTime());
        holder.mMatchTime.setText(currentItem.GetMatchTime());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    private String FormatQuarter(int quarter) {

        if (quarter == 0) { return "Not Started"; }

        else if (quarter == 1) { return "Q1"; }

        else if (quarter == 2) { return "Q2"; }

        else if (quarter == 3) { return "Q3"; }

        else if (quarter == 4) { return "Q4"; }

        else if (quarter == 5) { return "1st OT"; }

        else if (quarter == 6) { return "2nd OT"; }

        else if (quarter == 7) { return "3rd OT"; }

        else if (quarter == 50) { return "Half-time"; }

        else if (quarter == -1) { return ""; }

        else if (quarter == -2) { return "TBD"; }

        else if (quarter == -3) { return "Interrupted"; }

        else if (quarter == -4) { return "Cancelled"; }

        else if (quarter == -5) { return "Postponed"; }

        else { return Integer.toString(quarter); }
    }
}