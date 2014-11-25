package com.stolyarov.bogdan.lentaru.model;

/**
 * Created by Bagi on 23.11.2014.
 */
public class Item {
    private final String title;
    private final String link;
    private final String pubDate;
    private final String description;
    private final String category;


    public Item(String title, String pubDate, String link, String description, String category) {
        this.title = title;
        this.pubDate = pubDate;
        this.link = link;
        this.description = description;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }
}

