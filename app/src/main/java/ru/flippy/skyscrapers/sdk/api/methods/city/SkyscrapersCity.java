package ru.flippy.skyscrapers.sdk.api.methods.city;

import ru.flippy.skyscrapers.sdk.api.methods.city.settings.SkyscrapersCitySettings;

public class SkyscrapersCity {

    public CityInviteRequest invite(long userId) {
        return new CityInviteRequest(userId);
    }

    public SkyscrapersCitySettings settings() {
        return new SkyscrapersCitySettings();
    }
}
