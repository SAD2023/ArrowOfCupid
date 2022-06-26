package com.example.cupidsarrow;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


import com.google.firebase.FirebaseApp;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoveService extends Service {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("SERVICE BEING STOPPED", "STOOOOOOOOOOOOOOOOOOP AAA");
//        Bundle extras = intent.getExtras();
//        String user = extras.getString("user");
//        // START YOUR TASKS
//        Log.d("SERVICE BEING STOPPED", "STOOOOOOOOOOOOOOOOOOP AAA");
//        runtask(user);

        //ContextCompat.startForegroundService(this, intent);
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
            //runtask("Sadman");
        else
            startForeground(1, new Notification());
    }

    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "2";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        runtask("Sadman");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(com.google.android.gms.base.R.drawable.common_google_signin_btn_text_light_normal)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent z = new Intent(this, LoveService.class);
        //Bundle extras = rootIntent.getExtras();
        Log.d("SERVICE BEING STOPPED", "STOOOOOOOOOOOOOOOOOOP BBBBBBBBBBBB");
        //String user = extras.getString("user");
        //Log.d("SERVICE BEING STOPPED", user);
        z.putExtra("user", "Sadman");

        startForegroundService(z);

    }
//    public void onTaskRemoved(){
////        Log.d("SERVICE BEING STOPPED", "STOOOOOOOOOOOOOOOOOOP");
////        Intent intent = new Intent("com.android.ServiceStopped");
////        Log.d("SERVICE BEING STOPPED", "STOOOOOOOOOOOOOOOOOOP2");
////        sendBroadcast(intent);
////    }

    public void onStop(){
        Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "B");
    }


    public void runtask(String user){

        Runnable helloRunnable = new Runnable() {
            public void run() {
                FirebaseApp.initializeApp(LoveService.this);
                outstanding_kisses(user, LoveService.this);
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);

//        new Timer().scheduleAtFixedRate(new TimerTask() {
//
//            @Override
//            public void run() {
//                //your method
//                //FirebaseApp.initializeApp(LoveService.this);
//
//                while (true) {
//                    outstanding_kisses(user, LoveService.this);
//                }
//                //Log.d("SERVICE BEING STOPPED", "STOOOOOOOOOOOOOOOOOOP CCC");
//
//            }
//        }, 0, 1000);//put here time 1000 milliseconds=1 second
    }

    public static void outstanding_kisses(String user, Context context){
        Log.d("SERVICE BEING STOPPED", "STOOOOOOOOOOOOOOOOOOP  " + user);
        Log.d("SERVICE BEING STOPPED", "STOOOOOOOOOOOOOOOOOOP EEE");
        db.collection("incoming").whereEqualTo("For", user).get().addOnCompleteListener(result ->
        {
            Log.d("SERVICE BEING STOPPED", "STOOOOOOOOOOOOOOOOOOP EEE");
            if (result.isSuccessful() && (result.getResult().getDocuments().size() != 0)){

                DocumentSnapshot doc = result.getResult().getDocuments().get(0);
                // COMMENT THIS BACK String from = doc.get("From").toString();
                String is_for = doc.get("For").toString();
                MainActivity.sendnotification(context, is_for + ", you have a new kiss!", "From " + "Someone!");


                db.collection("incoming").document(user)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("ERROR", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("ERROR", "Error deleting document", e);
                            }
                        });

            }

            else {
                Log.d("ERROR!", "Error getting documents: ", result.getException());
            }
        });
    }
}

