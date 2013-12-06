package com.example.RSSReader_v3;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alexei
 * Date: 04.12.13
 * Time: 5:10
 * To change this template use File | Settings | File Templates.
 */
public class UpdateService extends IntentService {
    ArrayList<RSSChannel> arrayList;
    public static final String RESULT = "result";

    public UpdateService(String name) {
        super(name);
    }

    public UpdateService() {
        super("");
    }

    void fillArray() {
        ChannelsDataBaseHelper channelsDataBaseHelper = new ChannelsDataBaseHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = channelsDataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(ChannelsDataBaseHelper.DATABASE_NAME, null, null, null, null, null, null);
        int id_column = cursor.getColumnIndex(ChannelsDataBaseHelper.ID);
        int name_column = cursor.getColumnIndex(ChannelsDataBaseHelper.NAME);
        int url_column = cursor.getColumnIndex(ChannelsDataBaseHelper.URL);
        arrayList.clear();
        while (cursor.moveToNext()) {
            arrayList.add(new RSSChannel(cursor.getString(name_column), cursor.getString(url_column), cursor.getInt(id_column)));
        }
        cursor.close();
        sqLiteDatabase.close();
        channelsDataBaseHelper.close();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        arrayList = new ArrayList<RSSChannel>();
        fillArray();
        boolean done = true;
        boolean flag = intent.getBooleanExtra(RSSChannelsActivity.FLAG, false);
        int id = intent.getIntExtra(RSSChannelsActivity.ID, 0);

        for (int i = 0; i < arrayList.size(); i++)
            if (flag || arrayList.get(i).id == id) {
                try {
                    DownloadChannelFeed downloadChannelFeed = new DownloadChannelFeed(getApplicationContext(), arrayList.get(i).URL, arrayList.get(i).id);
                    downloadChannelFeed.execute();
                } catch (Exception e) {
                    done = false;
                }
            }
        Intent intentResp = new Intent();
        intentResp.setAction(RSSChannelsActivity.ACTION);
        intentResp.putExtra(RESULT, done);

        sendBroadcast(intentResp);

    }

}
