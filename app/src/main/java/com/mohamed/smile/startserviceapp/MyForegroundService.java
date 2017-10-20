package com.mohamed.smile.startserviceapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by clive on 18-Apr-14.
 *
 * Service started as foreground service, moved into background
 * or new background service started.
 * plays a sound loop in a separate thread
 *
 *  www.101apps.co.za
 */
public class MyForegroundService extends Service {

    int NOTIFICATION_ID = 101;
    int REQUEST_CODE = 1;

    private String TAG = "services";
    private Thread backgroundThread;
    private MediaPlayer player;

    static final String ACTION_FOREGROUND =
            "com.example.services.services_app.MyForegroundService.FOREGROUND";
    static final String ACTION_BACKGROUND =
            "com.example.services.services_app.MyForegroundService.BACKGROUND";

    @Override
    public void onCreate() {
        super.onCreate();

        player = MediaPlayer.create(this, R.raw.correct_sound);
        player.setLooping(true);

        backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                playMusic();
            }
        });

        Toast.makeText(this, "Foreground Service Created",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
//        not using a binder
        return null;
    }

    //    called each time startService() called and on restarts
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "in onStartCommand ...");
        if (ACTION_FOREGROUND.equals(intent.getAction())) {
            Log.i(TAG, "Start Play Music in Foreground Service");
            //start foreground service
            startForeground(NOTIFICATION_ID, getCompatNotification());
//          start the display activity
            Toast.makeText(this, "This Message Before Display Activity ...", Toast.LENGTH_SHORT).show();
            Intent intentDisplayActivity = new Intent(
                    MyForegroundService.this, DisplayActivity.class);
            intentDisplayActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentDisplayActivity);
            Toast.makeText(this, "This Message After Display Activity ...", Toast.LENGTH_SHORT).show();
        } else if (ACTION_BACKGROUND.equals(intent.getAction())) {
            Log.i(TAG, "Moves Service to background or " +
                    "starts a new background service");
                    /*service removed from foreground
                    - still runs but now more likely to be killed.
                                passing true = removes notification*/
            stopForeground(true);
        }
//        if the music is not playing, start a thread to play the music
        if (!player.isPlaying()) {
            Log.i(TAG, "Starting Thread...");
            backgroundThread.start();
        } else {
            Log.i(TAG, "Still playing - no new thread started");
        }
        return START_STICKY;
    }

    //    build the notification which includes a pending intent
    private Notification getCompatNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Service Started")
                .setTicker("Music Playing")
                .setWhen(System.currentTimeMillis());
        Intent startIntent = new Intent(getApplicationContext(),
                ForegroundActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, REQUEST_CODE, startIntent, 0);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        return notification;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
        Toast.makeText(this, "Service Destroyed",
                Toast.LENGTH_SHORT).show();

        if (player.isPlaying()) {
            Log.i(TAG, "player released");
            player.release();
            player = null;
        }
        //      kill the thread
        if (backgroundThread != null) {
            Log.i(TAG, "Destroying thread...");
            Thread dummy = backgroundThread;
            backgroundThread = null;
            dummy.interrupt();
        }
    }

    //    starts media player playing sound loop
    private void playMusic() {
        Log.i(TAG, "Playing music in Service");
        player.start();
    }
}
