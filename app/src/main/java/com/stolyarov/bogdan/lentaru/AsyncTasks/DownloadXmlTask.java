package com.stolyarov.bogdan.lentaru.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.stolyarov.bogdan.lentaru.R;
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

    private final View progres;
    private final OnNewsLoadedComplete listener;
    private Context context;
    private ArrayList<Item> resultItems;
    private static final String URL = "http://lenta.ru/rss/";
    public static final String myLog = "MyLog";

    public DownloadXmlTask(Context c, View v, OnNewsLoadedComplete l) {
        this.context = c;
        this.progres = v;
        this.listener = l;
    }

    @Override
    protected ArrayList<Item> doInBackground(String... urls) {
        resultItems = new ArrayList<Item>();

        try {
            resultItems = loadXmlFromNetwork(URL);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return resultItems;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> items) {
        if(progres != null) {
            progres.setVisibility(View.GONE);
        }
        if(listener != null) {
            listener.onNewsLoaded(resultItems);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progres != null) {
            progres.setVisibility(View.VISIBLE);
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
        conn.setConnectTimeout(15000);
        conn.setRequestMethod(context.getString(R.string.get));
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        Log.d(myLog, "downloadUrl");
        return conn.getInputStream();
    }


}
