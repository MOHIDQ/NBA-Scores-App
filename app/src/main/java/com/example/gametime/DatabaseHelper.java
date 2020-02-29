package com.example.gametime;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userSettings.db";
    private static final String TABLE_NAME = "UserSettingInfo";
    private static final String COL_1 = "TIME_REM";
    private static final String COL_2 = "POINTS_DIFF";
    private static final String COL_3 = "FAV_TEAM";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ TABLE_NAME + " (TIME_REM varchar(24) DEFAULT '4:00', POINTS_DIFF varchar(24) DEFAULT '5', FAV_TEAM varchar(24) DEFAULT 'N/A')");

        //sets default data for database
        insertDefaultData(sqLiteDatabase);
    }
    private void insertDefaultData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_NAME + " DEFAULT VALUES");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //update time remaining column
    public void updateTimeRemaining(String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_1 + " = " + "'" + time + "'");
    }

    //update score differential column
    public void updateScoreDifferential(String score) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_2 + " = " + "'" + score + "'");
    }

    //update favourite team column
    public void updateFavTeam(String team) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_3 + " = " + "'" + team + "'");
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return result;
    }
}
