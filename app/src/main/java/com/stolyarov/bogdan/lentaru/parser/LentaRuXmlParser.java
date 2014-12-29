package com.stolyarov.bogdan.lentaru.parser;

import android.util.Xml;

import com.stolyarov.bogdan.lentaru.model.Item;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bagi on 12.11.2014.
 */
public class LentaRuXmlParser {

    // We don't use namespaces
    public static final String ns = null;

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
        return items;
    }

    private Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, LentaRuXmlParser.ns, "item");
        String title = null;
        long pubDate = 0;
        String link = null;
        String description = null;
        String category = null;
        String imageUrl = null;
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
            else if (name.equals("enclosure")) {
                imageUrl = readImageUrl(parser);
            }
            else {
                skip(parser);
            }
        }
        return new Item(title, pubDate, link, description, category, imageUrl);
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
    private long readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, LentaRuXmlParser.ns, "pubDate");
        Date date = null;
        String stringDate = readText(parser);
        String sub = stringDate.substring(5,stringDate.length()-6);
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            date = format.parse(sub);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        long dateInLong = date.getTime();
        parser.require(XmlPullParser.END_TAG, LentaRuXmlParser.ns, "pubDate");
        return dateInLong;
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

    // Processes link tags in the feed.
    private String readImageUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        String imageUrl = "";
        parser.require(XmlPullParser.START_TAG, ns, "enclosure");
        String tag = parser.getName();
        if (tag.equals("enclosure")) {
            imageUrl = parser.getAttributeValue(null, "url");
                parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, "enclosure");
        return imageUrl;
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
