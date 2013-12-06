package com.example.RSSReader_v3;

public class RSSChannel {
    String name;
    String URL;
    int id;


    public RSSChannel(String name, String URL, int id) {
        this.name = name;
        this.URL = URL;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getURL() {
        return URL;
    }

    public String getName() {
        return name;
    }
}
