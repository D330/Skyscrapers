package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.request.city.settings.CitySettingsChangeAboutRequest;
import ru.flippy.skyscrapers.sdk.api.request.city.settings.CitySettingsChangeNameRequest;

public class SkyscrapersCitySettings {

    public CitySettingsChangeAboutRequest changeAbout(String about) {
        return new CitySettingsChangeAboutRequest(about);
    }

    public CitySettingsChangeNameRequest changeName(String name) {
        return new CitySettingsChangeNameRequest(name);
    }
}
