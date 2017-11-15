package ru.flippy.skyscrapers.application;

import android.app.Application;

import java.util.Date;
import java.util.function.Function;

import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;

public class SkyscrapersApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkyscrapersSDK.initialize(this);
    }
}
