package ru.flippy.skyscrapers.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.androidnetworking.AndroidNetworking;

import ru.flippy.skyscrapers.api.method.SkyscrapersBlackList;
import ru.flippy.skyscrapers.api.method.SkyscrapersChat;
import ru.flippy.skyscrapers.api.method.SkyscrapersCity;
import ru.flippy.skyscrapers.api.method.SkyscrapersFriends;
import ru.flippy.skyscrapers.api.method.SkyscrapersMail;
import ru.flippy.skyscrapers.api.method.SkyscrapersSettings;
import ru.flippy.skyscrapers.api.request.AuthRequest;
import ru.flippy.skyscrapers.api.request.ProfileRequest;

public class SkyscrapersAPI {

    public static final String LOG_TAG = "NEBO_SDK";

    private static final String PREFS_USER_NICK_KEY = "skyscrapers_user_name";
    private static final String PREFS_USER_PASSWORD_KEY = "skyscrapers_user_password";
    private static final String PREFS_USER_ID_KEY = "skyscrapers_user_id";

    private static SharedPreferences mPrefs;

    public static void initialize(Context context) {
        AndroidNetworking.initialize(context, new HttpClient());
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isLoggedIn() {
        return getUserNick() != null && getUserPassword() != null && getUserId() != 0;
    }

    public static String getUserNick() {
        return mPrefs.getString(PREFS_USER_NICK_KEY, null);
    }

    public static String getUserPassword() {
        return mPrefs.getString(PREFS_USER_PASSWORD_KEY, null);
    }

    public static long getUserId() {
        return mPrefs.getLong(PREFS_USER_ID_KEY, 0);
    }

    public static void logout() {
        mPrefs.edit()
                .remove(PREFS_USER_NICK_KEY)
                .remove(PREFS_USER_PASSWORD_KEY)
                .remove(PREFS_USER_ID_KEY)
                .apply();
    }

    public static void saveAuthData(String nick, String password, long userId) {
        mPrefs.edit()
                .putString(PREFS_USER_NICK_KEY, nick)
                .putString(PREFS_USER_PASSWORD_KEY, password)
                .putLong(PREFS_USER_ID_KEY, userId)
                .apply();
    }

    public static void updateUserNick(String newNick) {
        mPrefs.edit()
                .putString(PREFS_USER_NICK_KEY, newNick)
                .apply();
    }

    public static void updateUserPassword(String newPassword) {
        mPrefs.edit()
                .putString(PREFS_USER_PASSWORD_KEY, newPassword)
                .apply();
    }

    public static AuthRequest auth(String nick, String password) {
        return new AuthRequest(nick, password);
    }

    public static ProfileRequest getProfile(long userId) {
        return new ProfileRequest(userId);
    }

    public static SkyscrapersMail mail() {
        return new SkyscrapersMail();
    }

    public static SkyscrapersSettings settings() {
        return new SkyscrapersSettings();
    }

    public static SkyscrapersFriends friends() {
        return new SkyscrapersFriends();
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
}