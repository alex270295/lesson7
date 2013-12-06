package com.example.RSSReader_v3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created with IntelliJ IDEA.
 * User: Alexei
 * Date: 06.12.13
 * Time: 6:27
 * To change this template use File | Settings | File Templates.
 */
public class NewsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        WebView webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        String title = intent.getStringExtra(FeedDataBaseHelper.TITLE);
        String description = intent.getStringExtra(FeedDataBaseHelper.DESCRIPTION);
        String link = intent.getStringExtra(FeedDataBaseHelper.LINK);
        String date = intent.getStringExtra(FeedDataBaseHelper.DATE);
        String content = "<b>" + title + "</b>" + "<br>" + "<br>" + description + "<br>" + "<a href=" + link + "\">Link</a>";
        webView.loadData(content, "text/html; charset=UTF-8", null);
    }
}