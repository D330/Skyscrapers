package ru.flippy.skyscrapers.sdk.api.methods.city.settings;

public class SkyscrapersCitySettings {

    public CitySettingsChangeAboutRequest changeAbout(String about) {
        return new CitySettingsChangeAboutRequest(about);
    }

    public CitySettingsChangeNameRequest changeName(String name, String nameConfirm){
        return new CitySettingsChangeNameRequest(name, nameConfirm);
    }
}
