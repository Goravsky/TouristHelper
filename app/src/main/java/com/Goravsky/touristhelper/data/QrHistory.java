package com.Goravsky.touristhelper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class QrHistory extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "QrHystoryDb";
    private static final String TABLE_NAME = "history";

    private static final String ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_CONTENT = "content";

    private SQLiteDatabase historyDb;

    public QrHistory(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    /*
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME  +
                " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_CONTENT + " TEXT ) " );
    }
    */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME  +
                " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_CONTENT + " TEXT ) " );
        db.setMaximumSize(50);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void open(){
            try {
                historyDb = this.getWritableDatabase();
            } catch (SQLiteException ex) {
                historyDb = this.getReadableDatabase();
            }
    }

    public void close(){
        if(historyDb != null && historyDb.isOpen()){
            historyDb.close();
        }
    }

    public void insert(String type, String date, String content){
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_TYPE, type);
        newValues.put(COLUMN_DATE, date);
        newValues.put(COLUMN_CONTENT, content);

        historyDb.insert(TABLE_NAME, null, newValues);
    }

    public ArrayList<QrRecord> getRecords() {
        ArrayList<QrRecord> lastCodes = new ArrayList<>();

        System.out.println("Всего в БД: " + historyDb.query(
                TABLE_NAME, null, null, null,
                null, null,
                ID + " DESC ").getCount());

        Cursor historyCursor = historyDb.query(
                TABLE_NAME, null, null, null,
                COLUMN_CONTENT, null,
                ID + " DESC ");
        historyCursor.moveToFirst();

        System.out.println("Выбрано из БД: "+ historyCursor.getCount());

        while(!historyCursor.isAfterLast()){
            String type = historyCursor.getString(historyCursor.getColumnIndex(COLUMN_TYPE));
            String date = historyCursor.getString(historyCursor.getColumnIndex(COLUMN_DATE));
            String content = historyCursor.getString(historyCursor.getColumnIndex(COLUMN_CONTENT));

            lastCodes.add(new QrRecord(type, date, content));
            historyCursor.moveToNext();
        }

        return lastCodes;
    }
}


