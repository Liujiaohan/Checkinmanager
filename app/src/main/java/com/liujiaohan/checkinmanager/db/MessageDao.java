package com.liujiaohan.checkinmanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.liujiaohan.checkinmanager.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu jiaohan on 2017-08-13.
 */

public class MessageDao {
    private Context mContext;
    private MyDatabase mMyDatabase;
    private SQLiteDatabase mSQLiteDatabase;

    public MessageDao(Context context) {
        mContext=context;
        mMyDatabase=new MyDatabase(context);
    }

    public void addMessage(Message message){
        mSQLiteDatabase=mMyDatabase.getWritableDatabase();
        mSQLiteDatabase.beginTransaction();
        ContentValues contentValues=new ContentValues();
        contentValues.put("altitude",message.getAltitude());
        contentValues.put("numbering",message.getNumbering());
        contentValues.put("latitude",message.getLatitude());
        contentValues.put("total",message.getTotal());
        contentValues.put("femaleCount",message.getFemaleCount());
        contentValues.put("maleCount",message.getMaleCount());
        contentValues.put("forest",message.getForest());
        contentValues.put("time",message.getTime());
        contentValues.put("longitude",message.getLongitude());
        contentValues.put("location",message.getLocation());
        mSQLiteDatabase.insert(MyDatabase.TABLE_NAME,null,contentValues);
        Log.i("MessageDao", "addMessage: "+"successful");
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    public void deleteAllMessages(){
        mSQLiteDatabase = mMyDatabase.getWritableDatabase();
        mSQLiteDatabase.beginTransaction();
        mSQLiteDatabase.delete(MyDatabase.TABLE_NAME, null, null);
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    public void deleteMessage(Message message){
        mMyDatabase.getWritableDatabase().
                delete(MyDatabase.TABLE_NAME,"time=?",new String[]{message.getTime()});
    }
    public List<Message> queryAllMessages(){
        List<Message> list=new ArrayList<>();
        SQLiteDatabase mSQLiteDatabase_1=mMyDatabase.getWritableDatabase();

        Cursor cursor = mSQLiteDatabase_1.rawQuery("SELECT * FROM message", null);

        String numbering;
        String location;
        String forest;
        String time;
        String longitude;
        String latitude;
        String altitude;
        String femaleCount;
        String maleCount;
        String total;

        Log.i("MessageDao", "queryAllMessages: ");

        if (cursor.moveToFirst()){
            do {
                numbering=cursor.getString(cursor.getColumnIndex("numbering"));
                location=cursor.getString(cursor.getColumnIndex("location"));
                forest=cursor.getString(cursor.getColumnIndex("forest"));
                time=cursor.getString(cursor.getColumnIndex("time"));
                longitude=cursor.getString(cursor.getColumnIndex("longitude"));
                latitude=cursor.getString(cursor.getColumnIndex("latitude"));
                altitude=cursor.getString(cursor.getColumnIndex("altitude"));
                femaleCount=cursor.getString(cursor.getColumnIndex("femaleCount"));
                maleCount=cursor.getString(cursor.getColumnIndex("maleCount"));
                total=cursor.getString(cursor.getColumnIndex("total"));
                Message message=new Message(numbering,location,forest,time,longitude,latitude,altitude,
                        femaleCount,maleCount,total);
                Log.i("MessageDao", "queryAllMessages: "+message.toString());
                list.add(message);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
