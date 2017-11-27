package ru.flippy.skyscrapers.sdk.api;

import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersCup;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersBlackList;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersChat;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersCity;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersForum;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersFriends;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersMail;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersPayment;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersRating;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersSearch;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersSettings;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersSupport;
import ru.flippy.skyscrapers.sdk.api.methods.SkyscrapersVendor;
import ru.flippy.skyscrapers.sdk.api.request.LoginRequest;
import ru.flippy.skyscrapers.sdk.api.request.ProfileRequest;

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

    public static SkyscrapersCup cup() {
        return new SkyscrapersCup();
    }

    public static SkyscrapersForum forum() {
        return new SkyscrapersForum();
    }

    public static SkyscrapersFriends friends() {
        return new SkyscrapersFriends();
    }

    public static SkyscrapersMail mail() {
        return new SkyscrapersMail();
    }

    public static SkyscrapersPayment payment() {
        return new SkyscrapersPayment();
    }

    public static SkyscrapersRating rating() {
        return new SkyscrapersRating();
    }

    public static SkyscrapersSearch search() {
        return new SkyscrapersSearch();
    }

    public static SkyscrapersSettings settings() {
        return new SkyscrapersSettings();
    }

    public static SkyscrapersSupport support() {
        return new SkyscrapersSupport();
    }

    public static SkyscrapersVendor vendor() {
        return new SkyscrapersVendor();
    }
}
