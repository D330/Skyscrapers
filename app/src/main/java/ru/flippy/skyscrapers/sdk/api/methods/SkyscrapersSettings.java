package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.request.settings.SettingsChangeAboutRequest;
import ru.flippy.skyscrapers.sdk.api.request.settings.SettingsChangeBirthdayRequest;
import ru.flippy.skyscrapers.sdk.api.request.settings.SettingsChangeCityInviteRequest;
import ru.flippy.skyscrapers.sdk.api.request.settings.SettingsChangeNickRequest;
import ru.flippy.skyscrapers.sdk.api.request.settings.SettingsChangePasswordRequest;
import ru.flippy.skyscrapers.sdk.api.request.settings.SettingsChangeSexRequest;
import ru.flippy.skyscrapers.sdk.api.request.settings.SettingsRequest;

public class SkyscrapersSettings {

    public SettingsRequest get() {
        return new SettingsRequest();
    }

    public SettingsChangeAboutRequest changeAbout(String about) {
        return new SettingsChangeAboutRequest(about);
    }

    public SettingsChangeNickRequest changeNick(String nick) {
        return new SettingsChangeNickRequest(nick);
    }

    public SettingsChangeSexRequest changeSex() {
        return new SettingsChangeSexRequest();
    }

    public SettingsChangeBirthdayRequest changeBirthday(int birthdayDay, int birthdayMonth, int birthdayYear) {
        return new SettingsChangeBirthdayRequest(birthdayDay, birthdayMonth, birthdayYear);
    }

    public SettingsChangeCityInviteRequest changeGuildInvite() {
        return new SettingsChangeCityInviteRequest();
    }

    public SettingsChangePasswordRequest changePassword(String oldPassword, String newPassword) {
        return new SettingsChangePasswordRequest(oldPassword, newPassword);
    }
}
