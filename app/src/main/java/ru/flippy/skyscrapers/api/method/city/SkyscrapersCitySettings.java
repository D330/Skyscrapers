package ru.flippy.skyscrapers.api.method.city;

import ru.flippy.skyscrapers.api.request.city.settings.CitySettingsChangeAboutRequest;

public class SkyscrapersCitySettings {

    public CitySettingsChangeAboutRequest changeAbout(String about) {
        return new CitySettingsChangeAboutRequest(about);
    }

}
