package com.example.gametime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.ExampleViewHolder> {
    private ArrayList<CardLogic> mExampleList;
    //private ArrayList<Game> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mHomeLogo;
        public ImageView mAwayLogo;
        public TextView mHomeTeam;
        public TextView mAwayTeam;
        public TextView mHomeScore;
        public TextView mAwayScore;
        public TextView mLatestPlay;
        public TextView mQuarter;
        public TextView mQuarterTime;
        public TextView mMatchTime;

        public ExampleViewHolder(View itemView) {
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

    public Adapter(ArrayList<CardLogic> currentGameList) {
        mExampleList = currentGameList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        CardLogic currentItem = mExampleList.get(position);

        holder.mHomeLogo.setImageResource(currentItem.getHomeLogo());
        holder.mAwayLogo.setImageResource(currentItem.getAwayLogo());
        holder.mHomeTeam.setText(currentItem.getHomeTeam());
        holder.mAwayTeam.setText(currentItem.getAwayTeam());
        holder.mHomeScore.setText(Integer.toString(currentItem.getHomeScore()));
        holder.mAwayScore.setText(Integer.toString(currentItem.getAwayScore()));
        holder.mLatestPlay.setText(currentItem.getLastPlay());
        holder.mQuarter.setText(FormatQuater(currentItem.getQuarter()));
        holder.mQuarterTime.setText(currentItem.getQuarterTime());
        holder.mMatchTime.setText(currentItem.getMatchTime().toString());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    String FormatQuater(int quarter) {

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