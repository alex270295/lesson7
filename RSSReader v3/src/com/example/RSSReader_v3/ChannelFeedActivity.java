package com.example.RSSReader_v3;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class ChannelFeedActivity extends Activity {

    String URL;
    int id;

    ArrayList<Feed> arrayList;
    ArrayAdapter<Feed> arrayAdapter;

    void fillArray() {
        FeedDataBaseHelper feedDataBaseHelper = new FeedDataBaseHelper(getApplicationContext(), URL);
        SQLiteDatabase sqLiteDatabase = feedDataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(feedDataBaseHelper.dataBaseName(), null, null, null, null, null, null);
        int titleColumn = cursor.getColumnIndex(FeedDataBaseHelper.TITLE);
        int descriptionColumn = cursor.getColumnIndex(FeedDataBaseHelper.DESCRIPTION);
        int dateColumn = cursor.getColumnIndex(FeedDataBaseHelper.DATE);
        int linkColumn = cursor.getColumnIndex(FeedDataBaseHelper.LINK);
        arrayList.clear();
        while (cursor.moveToNext()) {
            arrayList.add(new Feed(cursor.getString(titleColumn), cursor.getString(descriptionColumn), cursor.getString(linkColumn), cursor.getString(dateColumn)));
        }
        cursor.close();
        sqLiteDatabase.close();
        feedDataBaseHelper.close();
        arrayAdapter.notifyDataSetChanged();

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_feed_activity);
        ListView listView = (ListView) findViewById(R.id.listView);
        Button button = (Button) findViewById(R.id.btnUpdate);
        Intent intent = getIntent();
        URL = intent.getStringExtra(RSSChannelsActivity.URL);
        id = intent.getIntExtra(RSSChannelsActivity.ID, 0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(FeedDataBaseHelper.TITLE, arrayList.get(position).title);
                intent.putExtra(FeedDataBaseHelper.DESCRIPTION, arrayList.get(position).description);
                intent.putExtra(FeedDataBaseHelper.DATE, arrayList.get(position).date);
                intent.putExtra(FeedDataBaseHelper.LINK, arrayList.get(position).link);
                intent.setClass(getApplicationContext(), NewsActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentServ = new Intent(getApplicationContext(), UpdateService.class);
                intentServ.putExtra(RSSChannelsActivity.FLAG, false);
                intentServ.putExtra(RSSChannelsActivity.ID, id);
                startService(intentServ);
            }
        });
        arrayList = new ArrayList<Feed>();
        arrayAdapter = new ArrayAdapter<Feed>(getApplicationContext(), R.layout.item, arrayList);
        listView.setAdapter(arrayAdapter);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fillArray();
            Toast.makeText(context, (intent.getBooleanExtra(UpdateService.RESULT, false) ? "Successful Update" : "Bad news... Update wasn't successful"), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter(RSSChannelsActivity.ACTION));
        fillArray();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
}