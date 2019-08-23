package com.endava.smartdesk.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.endava.smartdesk.R;

public class RegistrationSuccessfulActivity extends AppCompatActivity {

    private static final long ACTIVITY_LENGTH = 10000;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_successful);
        mHandler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(() -> showWelcomeScreen(), ACTIVITY_LENGTH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void showWelcomeScreen() {
        startActivity(new Intent(RegistrationSuccessfulActivity.this, WelcomeActivity.class));
    }
}
