package com.shyk.alena.booksapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "booksDb";
    public static final String TABLE_BOOKS = "books";

    private static final String KEY_ID = "_id";
    public static final String KEY_BOOK_ID = "bookId";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_NUMBER_OF_PAGES = "numberOfPages";
    public static final String KEY_PUBLISHER = "publisher";
    public static final String KEY_YEAR = "year";
    public static final String KEY_IMG = "image";
    public static final String KEY_DESCRIPTION = "description";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_BOOKS + "(" + KEY_ID
                + " integer primary key,"
                + KEY_BOOK_ID + " text,"
                + KEY_TITLE + " text,"
                + KEY_AUTHOR + " text,"
                + KEY_NUMBER_OF_PAGES + " text,"
                + KEY_PUBLISHER + " text,"
                + KEY_IMG + " text,"
                + KEY_YEAR + " text,"
                + KEY_DESCRIPTION + " text"
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_BOOKS);
        onCreate(db);

    }
}