package com.example.RSSReader_v3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alexei
 * Date: 05.12.13
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */
public class DownloadChannelFeed {
    String url;
    int id;
    ArrayList<Feed> arrayList;
    Context context;

    DownloadChannelFeed(Context context, String url, int id) throws MalformedURLException {
        this.context = context;
        this.url = url;
        this.id = id;
        arrayList = new ArrayList<Feed>();
    }

    public class RSSHandler extends DefaultHandler {

        StringBuilder builder;

        Feed feed;

        @Override
        public void startDocument() throws SAXException {
            feed = new Feed();
        }

        @Override
        public void endDocument() throws SAXException {

        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("entry") || qName.equals("item")) {
                feed = new Feed();
            } else {
                builder = new StringBuilder();
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("entry".equals(qName) || "item".equals(qName)) {
                arrayList.add(feed);
            } else {
                if ("title".equals(qName)) {
                    feed.title = builder.toString();
                } else if ("description".equals(qName) || "summary".equals(qName)) {
                    feed.description = builder.toString();
                } else if ("link".equals(qName)) {
                    feed.link = builder.toString();
                } else if ("pubDate".equals(qName) || "published".equals(qName))
                    feed.date = builder.toString();
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            builder.append(ch, start, length);
        }
    }

    void execute() throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(url));
        HttpEntity httpEntity = httpResponse.getEntity();

        String xml = EntityUtils.toString(httpEntity);
        InputSource is = new InputSource(new StringReader(xml));
        RSSHandler handler = new RSSHandler();
        parser.parse(is, handler);
        uploadToDataBase();


    }

    void uploadToDataBase() {
        FeedDataBaseHelper feedDataBaseHelper = new FeedDataBaseHelper(context, url.toString());
        SQLiteDatabase sqLiteDatabase = feedDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(feedDataBaseHelper.dropDatabase());
        sqLiteDatabase.execSQL(feedDataBaseHelper.createDatabase());
        for (int i = 0; i < arrayList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FeedDataBaseHelper.TITLE, arrayList.get(i).title);
            contentValues.put(FeedDataBaseHelper.DESCRIPTION, arrayList.get(i).description);
            contentValues.put(FeedDataBaseHelper.LINK, arrayList.get(i).link);
            contentValues.put(FeedDataBaseHelper.DATE, arrayList.get(i).date);
            sqLiteDatabase.insert(feedDataBaseHelper.dataBaseName(), null, contentValues);
        }
        sqLiteDatabase.close();
        feedDataBaseHelper.close();

    }

}
