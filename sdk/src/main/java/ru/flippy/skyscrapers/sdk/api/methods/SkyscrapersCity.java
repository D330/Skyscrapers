package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.request.city.CityChangeRoleRequest;
import ru.flippy.skyscrapers.sdk.api.request.city.CityInviteRequest;
import ru.flippy.skyscrapers.sdk.api.request.city.CityRolesRequest;

public class SkyscrapersCity {

    public CityInviteRequest invite(long userId) {
        return new CityInviteRequest(userId);
    }

    public CityRolesRequest roles(long userId) {
        return new CityRolesRequest(userId);
    }

    public CityChangeRoleRequest changeRole(long userId, int roleLevel) {
        return new CityChangeRoleRequest(userId, roleLevel);
    }

    public SkyscrapersCitySettings settings() {
        return new SkyscrapersCitySettings();
    }
}
