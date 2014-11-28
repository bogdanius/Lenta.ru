package com.stolyarov.bogdan.lentaru.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Bagi on 28.11.2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "newsbase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "news";

    public static final String TITLE_COLUMN = "title";
    public static final String LINK_COLUMN = "link";
    public static final String PUBDATE_COLUMN = "pubDate";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String CATEGORY_COLUMN = "category";

    private static final String DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE  +
            " (" + TITLE_COLUMN + " text NOT NULL,"+ LINK_COLUMN +" text NOT NULL," +
            ""+ PUBDATE_COLUMN +" text NOT NULL,"+ DESCRIPTION_COLUMN +" text NOT NULL," +
            ""+ CATEGORY_COLUMN +" text NOT NULL)";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXIST " + DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
}
