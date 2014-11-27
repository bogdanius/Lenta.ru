package com.stolyarov.bogdan.lentaru.parser;

import android.util.Log;
import android.util.Xml;

import com.stolyarov.bogdan.lentaru.model.Item;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Bagi on 12.11.2014.
 */
public class LentaRuXmlParser {

    // We don't use namespaces
    public static final String ns = null;
    public static final String myLog = "MyLog";

    public ArrayList<Item> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }

    }

    private ArrayList<Item> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Item> items = new ArrayList<Item>();
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        parser.next();  // this doing for skip tag "chanel"
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            // Starts by looking for the item tag
            if (name.equals("channel")) {
                parser.require(XmlPullParser.START_TAG, ns, "channel");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }

                    name = parser.getName();
                    if (name.equals("item")) {
                        items.add(readItem(parser));
                    } else {
                        skip(parser);
                    }
                }
            } else {
                skip(parser);
            }

        }
        Log.d(myLog, "return items from readFeed");
        return items;

    }
    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, LentaRuXmlParser.ns, "item");
        String title = null;
        String pubDate = null;
        String link = null;
        String description = null;
        String category = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("pubDate")) {
                pubDate = readPubDate(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            }
            else if (name.equals("description")) {
                description = readDescription(parser);
            }
            else if (name.equals("category")) {
                category = readCategory(parser);
            }
            else {
                skip(parser);
            }
        }
        return new Item(title, pubDate, link, description, category);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, LentaRuXmlParser.ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, LentaRuXmlParser.ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, LentaRuXmlParser.ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, LentaRuXmlParser.ns, "link");
        return link;
    }

    // Processes publication date tags in the feed.
    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, LentaRuXmlParser.ns, "pubDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, LentaRuXmlParser.ns, "pubDate");
        return pubDate;
    }

    // Processes description tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, LentaRuXmlParser.ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, LentaRuXmlParser.ns, "description");
        return description;
    }

    // Processes publication date tags in the feed.
    private String readCategory(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, LentaRuXmlParser.ns, "category");
        String category = readText(parser);
        parser.require(XmlPullParser.END_TAG, LentaRuXmlParser.ns, "category");
        return category;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }




}
