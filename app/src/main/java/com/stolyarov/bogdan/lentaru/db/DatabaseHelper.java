package com.stolyarov.bogdan.lentaru.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Bagi on 28.11.2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_NAME = "newsbase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "news";
    private static DatabaseHelper sInstance;

    public static final String TITLE_COLUMN = "title";
    public static final String LINK_COLUMN = "link";
    public static final String PUBDATE_COLUMN = "pubDate";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String CATEGORY_COLUMN = "category";
    public static final String IMAGEURL_COLUMN = "imageUrl";

    private static final String DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE  +
            " (" + TITLE_COLUMN + " VARCHAR(255),"+ LINK_COLUMN +" VARCHAR(255)," +
            PUBDATE_COLUMN +" VARCHAR(255),"+ DESCRIPTION_COLUMN +" VARCHAR(255)," +
            CATEGORY_COLUMN +" VARCHAR(255)," +
            IMAGEURL_COLUMN +" VARCHAR(255)," +
            DatabaseHelper._ID + " INTEGER PRIMARY KEY AUTOINCREMENT);";

    private static final String DELETE_DATABASE = "DROP TABLE IF EXISTS "
            + DATABASE_TABLE;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
}
