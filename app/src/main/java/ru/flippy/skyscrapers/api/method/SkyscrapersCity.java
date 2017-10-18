package ru.flippy.skyscrapers.api.method;

import ru.flippy.skyscrapers.api.method.city.SkyscrapersCitySettings;
import ru.flippy.skyscrapers.api.request.city.CityInviteRequest;

public class SkyscrapersCity {

    public SkyscrapersCitySettings settings() {
        return new SkyscrapersCitySettings();
    }

    public CityInviteRequest invite(long userId) {
        return new CityInviteRequest(userId);
    }
}
