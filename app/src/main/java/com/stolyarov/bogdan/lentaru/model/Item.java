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
    private final String imageUrl;



    public Item(String title, String pubDate, String link, String description, String category, String imageUrl) {
        this.title = title;
        this.pubDate = pubDate;
        this.link = link;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }
}

