package com.stolyarov.bogdan.lentaru.model;

import java.net.URI;
import java.util.Date;

/**
 * Created by Bagi on 11.11.2014.
 */
public class NewsModel {
    private String title;
    private String link;
//    private String description;
    private Date publicDate;
//    private URI pictureUrl;
    private String category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

//    public String getDescription() {
//        return description;
////    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

    public Date getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(Date publicDate) {
        this.publicDate = publicDate;
    }

//    public URI getPictureUrl() {
//        return pictureUrl;
//    }
//
//    public void setPictureUrl(URI pictureUrl) {
//        this.pictureUrl = pictureUrl;
//    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
