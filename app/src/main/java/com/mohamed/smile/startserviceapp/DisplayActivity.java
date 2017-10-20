package com.mohamed.smile.startserviceapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by clive on 20-Apr-14.
 * <p/>
 * simply displays a counter and a button.
 * Pressing button increases counter
 * <p/>
 * www.101apps.co.za
 */
public class DisplayActivity extends Activity {

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        counter = 0;
        final TextView textView = (TextView) findViewById(R.id.textViewCounter);
        textView.setText(String.valueOf(counter));

        Button button = (Button) findViewById(R.id.buttonCounter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                textView.setText(String.valueOf(counter));
            }
        });
    }
}
