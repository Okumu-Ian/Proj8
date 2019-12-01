package com.c323proj8.yourname.services;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.c323proj8.yourname.ChatActivity;

import java.util.ArrayList;
import java.util.List;

public class RefresherService extends Service {

    public String uid = null;
    public String to_id = null;
    public int count = 0;
    public Handler handler = null;
    public static Runnable runnable = null;
    List<String> content;

    public RefresherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        /*throw new UnsupportedOperationException("Not yet implemented");*/
        return null;
    }

    private void refreshAfterTime(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(checkCount()){
                Toast.makeText(RefresherService.this, "You have a new message.", Toast.LENGTH_SHORT).show();
                count = content.size();
                }
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable,5000);
    }

    private boolean checkCount(){
        SQLiteDatabase database = openOrCreateDatabase("MESSAGES",MODE_PRIVATE,null);
        String query = "SELECT * FROM messages WHERE (message_to = ? AND message_from = ?) OR (message_from = ? AND message_to = ?)";
        content = new ArrayList<>();
        Cursor c = database.rawQuery(query,new String[]{to_id,uid,to_id,uid});
        if(c.moveToFirst()){
            do{
                content.add(c.getString(3));
            }while (c.moveToNext());
        }
        c.close();

        return  content.size() > count;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        refreshAfterTime();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uid = intent.getStringExtra("FROM");
        to_id = intent.getStringExtra("TO");
        count = intent.getIntExtra("COUNT",0);
        return super.onStartCommand(intent, flags, startId);
    }
}
