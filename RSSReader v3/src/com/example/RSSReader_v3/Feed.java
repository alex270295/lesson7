package com.example.RSSReader_v3;

/**
 * Created with IntelliJ IDEA.
 * User: Alexei
 * Date: 05.12.13
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public class Feed {
    String title;
    String description;
    String link;
    String date;

    Feed(String title, String description, String link, String date) {
        this.date = date;
        this.description = description;
        this.link = link;
        this.title = title;
    }

    Feed() {
        this.date = null;
        this.description = null;
        this.link = null;
        this.title = null;
    }


    @Override
    public String toString() {
        return title + "\n" + date;
    }
}