package com.stolyarov.bogdan.lentaru.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.stolyarov.bogdan.lentaru.model.Item;
import com.stolyarov.bogdan.lentaru.parser.LentaRuXmlParser;
import com.stolyarov.bogdan.lentaru.ui.MainActivity;

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

    private Context context;



    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Item> doInBackground(String... urls) {


        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }



    }



        @Override
    protected void onPostExecute(ArrayList<Item> items) {
        MainActivity.setItems(items);
    }

    private ArrayList<Item> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        LentaRuXmlParser lentaRuXmlParser  = new LentaRuXmlParser();
        ArrayList<Item> items = null;
        String title = null;
        String pubDate = null;
        String summary = null;
        String description = null;
        String category = null;
//        Calendar rightNow = Calendar.getInstance();
//        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

//        // Checks whether the user set the preference to include summary text
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
//        boolean pref = sharedPrefs.getBoolean("summaryPref", false);

//        StringBuilder htmlString = new StringBuilder();
//        htmlString.append("<h3>" + context.getResources().getString(R.string.page_title) + "</h3>");
//        htmlString.append("<em>" + context.getResources().getString(R.string.updated) + " " +
//                formatter.format(rightNow.getTime()) + "</em>");

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

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
/*        for (Item item : items) {
            htmlString.append("<p><a href='");
            htmlString.append(item.link);
            htmlString.append("'>" + item.title + "</a></p>");
            // If the user set the preference to include summary text,
            // adds it to the display.
            if (pref) {
                htmlString.append(item.summary);
            }
        }*/
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
