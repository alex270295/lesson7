package com.example.RSSReader_v3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: Alexei
 * Date: 04.12.13
 * Time: 3:49
 * To change this template use File | Settings | File Templates.
 */
public class EditChannelActivity extends Activity {
    String name;
    String URL;
    int id;
    EditText etName;
    EditText etURL;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_channel_activity);
        etName = (EditText) findViewById(R.id.etName);
        etURL = (EditText) findViewById(R.id.etURL);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        Intent intent = getIntent();
        name = intent.getStringExtra(RSSChannelsActivity.NAME);
        URL = intent.getStringExtra(RSSChannelsActivity.URL);
        id = intent.getIntExtra(RSSChannelsActivity.ID, 0);
        if (intent.getBooleanExtra(RSSChannelsActivity.NEW, true)) {
            btnDelete.setVisibility(View.GONE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
            etName.setText(name);
            etURL.setText(URL);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelsDataBaseHelper channelsDataBaseHelper = new ChannelsDataBaseHelper(getApplicationContext());
                SQLiteDatabase sqLiteDatabase = channelsDataBaseHelper.getWritableDatabase();
                sqLiteDatabase.delete(ChannelsDataBaseHelper.DATABASE_NAME, ChannelsDataBaseHelper.ID + "=" + id, null);
                sqLiteDatabase.close();
                channelsDataBaseHelper.close();
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().length() != 0 && etURL.getText().length() != 0) {
                    if (getIntent().getBooleanExtra(RSSChannelsActivity.NEW, true)) {
                        ChannelsDataBaseHelper channelsDataBaseHelper = new ChannelsDataBaseHelper(getApplicationContext());
                        SQLiteDatabase sqLiteDatabase = channelsDataBaseHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ChannelsDataBaseHelper.NAME, etName.getText().toString());
                        contentValues.put(ChannelsDataBaseHelper.URL, etURL.getText().toString());
                        sqLiteDatabase.insert(ChannelsDataBaseHelper.DATABASE_NAME, null, contentValues);
                        sqLiteDatabase.close();
                        channelsDataBaseHelper.close();
                    } else {
                        ChannelsDataBaseHelper channelsDataBaseHelper = new ChannelsDataBaseHelper(getApplicationContext());
                        SQLiteDatabase sqLiteDatabase = channelsDataBaseHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ChannelsDataBaseHelper.NAME, etName.getText().toString());
                        contentValues.put(ChannelsDataBaseHelper.URL, etURL.getText().toString());
                        contentValues.put(ChannelsDataBaseHelper.ID, id);
                        sqLiteDatabase.update(ChannelsDataBaseHelper.DATABASE_NAME, contentValues, ChannelsDataBaseHelper.ID + "=" + id, null);
                        sqLiteDatabase.close();
                        channelsDataBaseHelper.close();
                    }
                    finish();
                }

            }
        });
    }
}