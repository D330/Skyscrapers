package ru.flippy.skyscrapers.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.flippy.skyscrapers.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Intent intent;
        //if (SkyscrapersSDK.isLoggedIn()) {
        //  intent = new Intent(this, GameActivity.class);
        // } else {
        intent = new Intent(this, AuthActivity.class);
        //  }
        startActivity(intent);
        finish();
    }
}
