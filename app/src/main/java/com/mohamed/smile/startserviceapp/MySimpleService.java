package com.mohamed.smile.startserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by clive on 16-Apr-14.
 * <p/>
 * Simple Service that is started, simulates updating a user's score
 * on a server and returns success. On success, a message is sent to
 * a handler to update a text view
 * <p/>
 * www.101apps.co.za
 */
public class MySimpleService extends Service {

    private Thread backgroundServiceThread;

    private String TAG = "services";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String message = bundle.getString("message");
            MainActivity.textView.setText(message);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        //use when binding component to service - we're not using it
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*called by system each time client calls startService()
        also called on restarts*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (flags) {
            case START_FLAG_REDELIVERY:
                Log.i(TAG, "reusing original intent in Service restart");
                /*reusing original intent to restart after system killed service*/
                break;
            case START_FLAG_RETRY:
                Log.i(TAG, "retrying original intent in Service as " +
                        "first attempt failed");
                /*the Service has been restarted after an abnormal
                 termination or failed start*/
                break;
            default://value of 0
                Log.i(TAG, "default flag used");
                break;
        }
        Log.i(TAG, "Service startId: " + String.valueOf(startId));
        Toast.makeText(this, "Service created...", Toast.LENGTH_SHORT).show();
        startBackgroundThread(intent, startId);
        /*only restart if there are pending start calls/
        or killed before stopSelf called
        - it will use the original intent in restarting*/
        return START_REDELIVER_INTENT;
    }

    /*starts a background thread to do simulated work*/
    private void startBackgroundThread(Intent intent, final int startId) {
        final String userName = intent.getStringExtra("userName");
        final String password = intent.getStringExtra("password");
        final int score = intent.getIntExtra("score", 0);

        //        start a new thread for background operations
        backgroundServiceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                /*thread will run until Runtime exits,
                it completes its job, or throws an exception*/
                if (updateScore(userName, password, score)) {
                    //  work finished successfully
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", "Score updated: " + startId);
                    msg.setData(bundle);
                    Log.i(TAG, "Sending message to handler...");
                    handler.sendMessage(msg);
                    Log.i(TAG, "Service stopping self ID: " + startId);
                    stopSelf(startId);
                }
            }
        });
//        set thread priority to slightly lower than
//          main thread so has minimal affect on it
        backgroundServiceThread.setPriority(
                Process.THREAD_PRIORITY_BACKGROUND);
        Log.i(TAG, "Starting thread...");
        backgroundServiceThread.start();
    }

    //    does simulated work - updates users score on server. returns true on success
    private boolean updateScore(String userName, String password, int score) {
        Log.i(TAG, "Updating the score...");
        boolean isUpdated = false;
//        simulates doing some work - sleep for 4 seconds
        SystemClock.sleep(4000);
        Log.i(TAG, "Update successful");
        isUpdated = true;
        return isUpdated;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Destroying Service...");
        Toast.makeText(this, "Destroying Service ...", Toast.LENGTH_SHORT).show();
//      kill the thread
        if (backgroundServiceThread != null) {
            Log.i(TAG, "Destroying Thread...");
            Thread dummy = backgroundServiceThread;
            backgroundServiceThread = null;
            dummy.interrupt();
        }
    }
}
