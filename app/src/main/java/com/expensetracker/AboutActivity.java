package com.expensetracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_about);
        ((Button) findViewById(R.id.backButton)).setOnClickListener(v -> finish());
    }
}
