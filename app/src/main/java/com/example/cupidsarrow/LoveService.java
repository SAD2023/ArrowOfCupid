package com.example.cupidsarrow;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class LoveService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();;
        String user = extras.getString("user");

        // START YOUR TASKS
        runtask(user);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onTaskRemoved(){
        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);
    }

    public void runtask(String user){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                HomeScreen.outstanding_kisses(user, LoveService.this);
            }
        }, 0, 1000);//put here time 1000 milliseconds=1 second
    }
}

