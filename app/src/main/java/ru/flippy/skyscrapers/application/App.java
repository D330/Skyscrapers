package ru.flippy.skyscrapers.application;

import android.app.Application;

import ru.flippy.skyscrapers.api.SkyscrapersAPI;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkyscrapersAPI.initialize(this);
    }
}
