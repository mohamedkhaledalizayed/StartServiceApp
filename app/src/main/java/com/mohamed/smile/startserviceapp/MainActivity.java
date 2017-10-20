package com.mohamed.smile.startserviceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    public static TextView textView;

    private String TAG = "services";

    public void Branch01(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        Button buttonStartSimpleService = (Button) findViewById(R.id.button);
        buttonStartSimpleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Updating score...");
                Intent startIntent = new Intent(MainActivity.this,
                        MySimpleService.class);
                startIntent.putExtra("userName", "Peter");
                startIntent.putExtra("password", "Pan");
                startIntent.putExtra("score", 10);

                startService(startIntent);
            }
        });

        Button buttonStopSimpleService = (Button) findViewById(R.id.button2);
        buttonStopSimpleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this,
                        MySimpleService.class);
                stopService(stopIntent);
                textView.setText("Service Stopped");
            }
        });

        Button buttonGoForegroundActivity = (Button) findViewById(R.id.button3);
        buttonGoForegroundActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForegroundActivity = new Intent(MainActivity.this,
                        ForegroundActivity.class);
                startActivity(intentForegroundActivity);
            }
        });

    }
}
