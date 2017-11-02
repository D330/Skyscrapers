package ru.flippy.skyscrapers.sdk.api.methods.settings;

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

    public SettingsChangeCityInviteRequest changeGuildInvite() {
        return new SettingsChangeCityInviteRequest();
    }

    public SettingsChangePasswordRequest changePassword(String oldPassword, String newPassword, String newPasswordConfirm) {
        return new SettingsChangePasswordRequest(oldPassword, newPassword, newPasswordConfirm);
    }
}
