package ru.flippy.skyscrapers.api.method;

import ru.flippy.skyscrapers.api.request.settings.SettingsChangeAboutRequest;
import ru.flippy.skyscrapers.api.request.settings.SettingsChangeBirthdayRequest;
import ru.flippy.skyscrapers.api.request.settings.SettingsChangeGuildInviteRequest;
import ru.flippy.skyscrapers.api.request.settings.SettingsChangeNickRequest;
import ru.flippy.skyscrapers.api.request.settings.SettingsChangePasswordRequest;
import ru.flippy.skyscrapers.api.request.settings.SettingsChangeSexRequest;
import ru.flippy.skyscrapers.api.request.settings.SettingsRequest;

public class SkyscrapersSettings {

    public SettingsRequest get() {
        return new SettingsRequest();
    }

    public SettingsChangeAboutRequest changeAbout(String about) {
        return new SettingsChangeAboutRequest(about);
    }

    public SettingsChangeNickRequest changeNick(String newNick, String newNickConfirm) {
        return new SettingsChangeNickRequest(newNick, newNickConfirm);
    }

    public SettingsChangeSexRequest changeSex() {
        return new SettingsChangeSexRequest();
    }

    public SettingsChangeBirthdayRequest changeBirthday(int birthdayDay, int birthdayMonth, int birthdayYear) {
        return new SettingsChangeBirthdayRequest(birthdayDay, birthdayMonth, birthdayYear);
    }

    public SettingsChangeGuildInviteRequest changeGuildInvite() {
        return new SettingsChangeGuildInviteRequest();
    }

    public SettingsChangePasswordRequest changePassword(String oldPassword, String newPassword, String newPasswordConfirm) {
        return new SettingsChangePasswordRequest(oldPassword, newPassword, newPasswordConfirm);
    }
}
