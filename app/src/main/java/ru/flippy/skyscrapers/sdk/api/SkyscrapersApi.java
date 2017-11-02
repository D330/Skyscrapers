package ru.flippy.skyscrapers.sdk.api;

import ru.flippy.skyscrapers.sdk.api.methods.LoginRequest;
import ru.flippy.skyscrapers.sdk.api.methods.ProfileRequest;
import ru.flippy.skyscrapers.sdk.api.methods.blacklist.SkyscrapersBlackList;
import ru.flippy.skyscrapers.sdk.api.methods.chat.SkyscrapersChat;
import ru.flippy.skyscrapers.sdk.api.methods.city.SkyscrapersCity;
import ru.flippy.skyscrapers.sdk.api.methods.friends.SkyscrapersFriends;
import ru.flippy.skyscrapers.sdk.api.methods.mail.SkyscrapersMail;
import ru.flippy.skyscrapers.sdk.api.methods.settings.SkyscrapersSettings;

public class SkyscrapersApi {

    public static LoginRequest login(String nick, String password) {
        return new LoginRequest(nick, password);
    }

    public static ProfileRequest getProfile(long userId) {
        return new ProfileRequest(userId);
    }

    public static SkyscrapersBlackList blackList() {
        return new SkyscrapersBlackList();
    }

    public static SkyscrapersChat chat() {
        return new SkyscrapersChat();
    }

    public static SkyscrapersCity city() {
        return new SkyscrapersCity();
    }

    public static SkyscrapersFriends friends() {
        return new SkyscrapersFriends();
    }

    public static SkyscrapersMail mail() {
        return new SkyscrapersMail();
    }

    public static SkyscrapersSettings settings() {
        return new SkyscrapersSettings();
    }
}
