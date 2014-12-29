package com.stolyarov.bogdan.lentaru.asynctasks;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.stolyarov.bogdan.lentaru.R;
import com.stolyarov.bogdan.lentaru.db.DatabaseHelper;
import com.stolyarov.bogdan.lentaru.model.Item;
import com.stolyarov.bogdan.lentaru.parser.LentaRuXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Bagi on 23.11.2014.
 */
public class DownloadXmlTask extends AsyncTask<String, Void, ArrayList<Item>> {

    private SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper databaseHelper;

    private static final String TIME_AT_LAST_UPDATE = "time_at_last_update";
    private static final String NUMBERS_FIRST_ITEM = "numbers_first_item";

    private final View progress;
    private final OnNewsLoadedComplete listener;
    private Context context;
    private ArrayList<Item> resultItems;
    private static final String URL = "http://lenta.ru/rss/";
    SharedPreferences sharedPreferencesTimeLastUpdate;
    SharedPreferences sharedPreferencesFirstNumbersOfItemsInDB;
    private int afterUpdate;

    public DownloadXmlTask(Context c, View v, OnNewsLoadedComplete l) {
        this.context = c;
        this.progress = v;
        this.listener = l;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sharedPreferencesTimeLastUpdate = context.getSharedPreferences(TIME_AT_LAST_UPDATE, Context.MODE_PRIVATE);
        sharedPreferencesFirstNumbersOfItemsInDB = context.getSharedPreferences(NUMBERS_FIRST_ITEM, Context.MODE_PRIVATE);
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected ArrayList<Item> doInBackground(String... urls) {
        resultItems = new ArrayList<Item>();
        databaseHelper = DatabaseHelper.getInstance(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        int counterNewItems = 0;

        int timeLastUpdate = sharedPreferencesTimeLastUpdate.getInt(TIME_AT_LAST_UPDATE, Context.MODE_PRIVATE);
        int firstNumberInDB = sharedPreferencesFirstNumbersOfItemsInDB.getInt(NUMBERS_FIRST_ITEM, Context.MODE_PRIVATE);

        try {
            resultItems = loadXmlFromNetwork(URL);

            //check for first start app
            if (!(timeLastUpdate == 0)) {
                Log.d("MyLog", String.valueOf(timeLastUpdate));

                ContentValues contentValues;

                //check numbers new news
                while (timeLastUpdate < (int) resultItems.get(counterNewItems).getPubDate()) {
                    Log.d("MyLog", String.valueOf((int) resultItems.get(counterNewItems).getPubDate()));
                    counterNewItems++;
                    contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.CATEGORY_COLUMN, resultItems.get(resultItems.size() - counterNewItems).getCategory());
                    contentValues.put(DatabaseHelper.DESCRIPTION_COLUMN, resultItems.get(resultItems.size() - counterNewItems).getDescription());
                    contentValues.put(DatabaseHelper.LINK_COLUMN, resultItems.get(resultItems.size() - counterNewItems).getLink());
                    contentValues.put(DatabaseHelper.TITLE_COLUMN, resultItems.get(resultItems.size() - counterNewItems).getTitle());
                    contentValues.put(DatabaseHelper.IMAGEURL_COLUMN, resultItems.get(resultItems.size() - counterNewItems).getImageUrl());
                    contentValues.put(DatabaseHelper.PUBDATE_COLUMN, resultItems.get(resultItems.size() - counterNewItems).getPubDate());
                    sqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE, DatabaseHelper.IMAGEURL_COLUMN, contentValues);
                }
                afterUpdate = firstNumberInDB + counterNewItems;
                sqLiteDatabase.delete(DatabaseHelper.DATABASE_TABLE, DatabaseHelper._ID + " < " + afterUpdate, null);
                //else this first start, then create DB
            } else {
                Log.d("MyLog", "Программа запускается первый раз, DB нет");
                ContentValues contentValues;
                databaseHelper.onUpgrade(sqLiteDatabase, 1, 1);
                int numberItems = resultItems.size();
                for (int i = 1; i < numberItems + 1; i++) {
                    contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.CATEGORY_COLUMN, resultItems.get(numberItems - i).getCategory());
                    contentValues.put(DatabaseHelper.DESCRIPTION_COLUMN, resultItems.get(numberItems - i).getDescription());
                    contentValues.put(DatabaseHelper.LINK_COLUMN, resultItems.get(numberItems - i).getLink());
                    contentValues.put(DatabaseHelper.TITLE_COLUMN, resultItems.get(numberItems - i).getTitle());
                    contentValues.put(DatabaseHelper.IMAGEURL_COLUMN, resultItems.get(numberItems - i).getImageUrl());
                    contentValues.put(DatabaseHelper.PUBDATE_COLUMN, resultItems.get(numberItems - i).getPubDate());
                    sqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE, DatabaseHelper.IMAGEURL_COLUMN, contentValues);
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultItems;
    }


    @Override
    protected void onPostExecute(ArrayList<Item> items) {

        //added to SharedPreferences time last news and numbers of fist item in DB
        SharedPreferences.Editor editTime = sharedPreferencesTimeLastUpdate.edit();
        editTime.putInt(TIME_AT_LAST_UPDATE, (int) resultItems.get(0).getPubDate());
        editTime.commit();

        SharedPreferences.Editor editPosition = sharedPreferencesFirstNumbersOfItemsInDB.edit();
        editPosition.putInt(NUMBERS_FIRST_ITEM, afterUpdate);
        editPosition.commit();

        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
        if (listener != null) {
            listener.onNewsLoaded(resultItems);
        }
        sqLiteDatabase.close();
        databaseHelper.close();
    }


    private ArrayList<Item> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        LentaRuXmlParser lentaRuXmlParser = new LentaRuXmlParser();
        ArrayList<Item> items = null;
        try {
            stream = downloadUrl(urlString);
            items = lentaRuXmlParser.parse(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return items;
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(7000);
        conn.setRequestMethod(context.getString(R.string.get));
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }


}