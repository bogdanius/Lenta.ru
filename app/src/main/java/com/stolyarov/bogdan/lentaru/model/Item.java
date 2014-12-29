package com.stolyarov.bogdan.lentaru.model;

/**
 * Created by Bagi on 23.11.2014.
 */
public class Item {
    private  String title;
    private  String link;
    private  long pubDate;
    private  String description;
    private  String category;
    private  String imageUrl;

    public Item(String title, long pubDate, String link, String description, String category, String imageUrl) {
        setTitle(title);
        setPubDate(pubDate);
        setLink(link);
        setDescription(description);
        setCategory(category);
        setImageUrl(imageUrl);
    }

    public Item() {
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public long getPubDate() {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(long pubDate) {
        this.pubDate = pubDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

