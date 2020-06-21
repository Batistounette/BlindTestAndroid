package com.blindtest.batistounette.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.blindtest.batistounette.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final int SPLASH_TIME_OUT = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent MAIN_ACTIVITY_INTENT = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MAIN_ACTIVITY_INTENT);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}