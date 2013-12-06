package com.example.RSSReader_v3;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import java.util.Random;

public class RSSChannelsActivity extends Activity {
    public static final String ACTION = "update";
    public static final String NEW = "new";
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String ID = "id";
    public static final String FLAG = "flag";

    ListView lvRSSChannel;
    Button btnAdd;
    Button btnUpdate;
    ArrayList<RSSChannel> listChannel;
    ArrayAdapter<RSSChannel> aaChannel;

    void fillArray() {
        ChannelsDataBaseHelper channelsDataBaseHelper = new ChannelsDataBaseHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = channelsDataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(ChannelsDataBaseHelper.DATABASE_NAME, null, null, null, null, null, null);
        int id_column = cursor.getColumnIndex(ChannelsDataBaseHelper.ID);
        int name_column = cursor.getColumnIndex(ChannelsDataBaseHelper.NAME);
        int url_column = cursor.getColumnIndex(ChannelsDataBaseHelper.URL);
        listChannel.clear();
        while (cursor.moveToNext()) {
            listChannel.add(new RSSChannel(cursor.getString(name_column), cursor.getString(url_column), cursor.getInt(id_column)));
        }
        cursor.close();
        sqLiteDatabase.close();
        channelsDataBaseHelper.close();
        aaChannel.notifyDataSetChanged();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, (intent.getBooleanExtra(UpdateService.RESULT, false) ? "Successful Update" : "Bad news... Update wasn't successful"), Toast.LENGTH_SHORT).show();
            fillArray();

        }
    };

    @Override
    protected void onPause() {
        unregisterReceiver(mMessageReceiver);
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter(ACTION));
        fillArray();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_channels_activity);

        Intent intent = new Intent(getApplicationContext(), UpdateService.class);
        intent.putExtra(FLAG, true);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), new Random().nextInt(1000000000), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 15000, AlarmManager.INTERVAL_HOUR, pendingIntent);

        lvRSSChannel = (ListView) findViewById(R.id.lvRSSChannel);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdateAll);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateService.class);
                intent.putExtra(FLAG, true);
                startService(intent);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(NEW, true);
                intent.setClass(getApplicationContext(), EditChannelActivity.class);
                startActivity(intent);
            }
        });
        lvRSSChannel.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(NEW, false);
                intent.putExtra(NAME, listChannel.get(position).name);
                intent.putExtra(URL, listChannel.get(position).URL);
                intent.putExtra(ID, listChannel.get(position).id);
                intent.setClass(getApplicationContext(), EditChannelActivity.class);
                startActivity(intent);
                return false;
            }
        });
        lvRSSChannel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ChannelFeedActivity.class);
                intent.putExtra(ID, listChannel.get(position).id);
                intent.putExtra(URL, listChannel.get(position).URL);
                startActivity(intent);

            }
        });
        listChannel = new ArrayList<RSSChannel>();
        aaChannel = new ArrayAdapter<RSSChannel>(this, R.layout.item, listChannel);
        lvRSSChannel.setAdapter(aaChannel);
        fillArray();
    }

}
