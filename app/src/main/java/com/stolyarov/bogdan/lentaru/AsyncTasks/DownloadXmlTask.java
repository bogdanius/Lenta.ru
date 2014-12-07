package com.stolyarov.bogdan.lentaru.asynctasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

    private final View progress;
    private final OnNewsLoadedComplete listener;
    private Context context;
    private ArrayList<Item> resultItems;
    private static final String URL = "http://lenta.ru/rss/";

    public DownloadXmlTask(Context c, View v, OnNewsLoadedComplete l) {
        this.context = c;
        this.progress = v;
        this.listener = l;
    }

    @Override
    protected ArrayList<Item> doInBackground(String... urls) {
        resultItems = new ArrayList<Item>();
        databaseHelper = DatabaseHelper.getInstance(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            resultItems = loadXmlFromNetwork(URL);
            ContentValues contentValues;
            databaseHelper.onUpgrade(sqLiteDatabase,1,1);
            for (int i = 0; i < resultItems.size(); i++) {
                contentValues = new ContentValues();
                contentValues.put(DatabaseHelper.CATEGORY_COLUMN, resultItems.get(i).getCategory());
                contentValues.put(DatabaseHelper.DESCRIPTION_COLUMN, resultItems.get(i).getDescription());
                contentValues.put(DatabaseHelper.LINK_COLUMN, resultItems.get(i).getLink());
                contentValues.put(DatabaseHelper.PUBDATE_COLUMN, resultItems.get(i).getPubDate());
                contentValues.put(DatabaseHelper.TITLE_COLUMN, resultItems.get(i).getTitle());
                contentValues.put(DatabaseHelper.IMAGEURL_COLUMN, resultItems.get(i).getImageUrl());
                sqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE, DatabaseHelper.IMAGEURL_COLUMN, contentValues);
            }
        }

         catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultItems;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> items) {
        if(progress != null) {
            progress.setVisibility(View.GONE);
        }
        if(listener != null) {
            listener.onNewsLoaded(resultItems);
        }
        sqLiteDatabase.close();
        databaseHelper.close();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
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