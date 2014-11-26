package com.stolyarov.bogdan.lentaru.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

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

    public DownloadXmlTask(Context c, View v, OnNewsLoadedComplete l) {
        this.context = c;
        this.progres = v;
        this.listener = l;
    }

    @Override
    protected ArrayList<Item> doInBackground(String... urls) {
        resultItems = new ArrayList<Item>();

        try {
            resultItems = loadXmlFromNetwork(urls[0]);
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
            listener.onNewsLoaded(items);
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
        // Instantiate the parser
        LentaRuXmlParser lentaRuXmlParser = new LentaRuXmlParser();
        ArrayList<Item> items = null;
        String title = null;
        String pubDate = null;
        String summary = null;
        String description = null;
        String category = null;

        try {
            stream = downloadUrl(urlString);
            items = lentaRuXmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
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
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }


}
