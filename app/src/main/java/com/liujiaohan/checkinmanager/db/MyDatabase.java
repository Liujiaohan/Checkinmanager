package com.liujiaohan.checkinmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Liu jiaohan on 2017-08-13.
 */

public class MyDatabase extends SQLiteOpenHelper {
    private static final int DB_VERSION=1;
    private static final String DB_NAME="localdatabase.db";
    public static final String TABLE_NAME="message";

    public MyDatabase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table if not exists "+TABLE_NAME+"(_id integer primary key autoincrement, " +
                "numbering text, location text, forest text, time text, longitude text, latitude text, " +
                "altitude text, femaleCount text, maleCount text, total text)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}
