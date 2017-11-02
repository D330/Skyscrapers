package ru.flippy.skyscrapers.application;

import android.app.Application;

import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;

public class SkyscrapersApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkyscrapersSDK.initialize(this);
    }
}
