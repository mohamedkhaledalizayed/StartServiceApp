package com.mohamed.smile.startserviceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by clive on 18-Apr-14.
 *
 * displays 3 buttons: start foreground service,
 * move foreground service to background/also
 * starts a background service & stop the service
 *
 *  www.101apps.co.za
 */
public class ForegroundActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreground);

        Button buttonPlay = (Button) findViewById(R.id.button);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startForegroundIntent = new Intent(MyForegroundService.ACTION_FOREGROUND);
                startForegroundIntent.setClass(ForegroundActivity.this, MyForegroundService.class);
                startService(startForegroundIntent);
            }
        });

        Button buttonMoveServiceBackground = (Button) findViewById(R.id.button3);
        buttonMoveServiceBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startBackgroundIntent = new Intent(
                        MyForegroundService.ACTION_BACKGROUND);
                startBackgroundIntent.setClass(
                        ForegroundActivity.this, MyForegroundService.class);
                startService(startBackgroundIntent);
            }
        });

        Button buttonStop = (Button) findViewById(R.id.button2);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(
                        ForegroundActivity.this, MyForegroundService.class);
                stopService(stopIntent);
            }
        });
    }
}
