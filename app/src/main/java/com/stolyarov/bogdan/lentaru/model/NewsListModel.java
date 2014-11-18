package com.stolyarov.bogdan.lentaru.model;

import java.util.Date;

/**
 * Created by Bagi on 11.11.2014.
 */
public class NewsListModel {
    private String title;
//    private String link;
    private String category;
    private String publicDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getLink() {
//        return link;
//    }
//
//    public void setLink(String link) {
//        this.link = link;
//    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String description) {
        this.category = description;
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate;
    }
}
