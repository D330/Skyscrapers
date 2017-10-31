package ru.flippy.skyscrapers.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ru.flippy.skyscrapers.api.internal.request.AuthRequest;
import ru.flippy.skyscrapers.api.internal.retrofit.RetrofitClient;

public class SkyscrapersSDK {

    private static final String LOG_TAG = SkyscrapersSDK.class.getSimpleName();

    private static final String PREFS_USER_NICK_KEY = "skyscrapers_user_name";
    private static final String PREFS_USER_PASSWORD_KEY = "skyscrapers_user_password";
    private static final String PREFS_USER_ID_KEY = "skyscrapers_user_id";

    private static SharedPreferences prefs;

    public static void initialize(Context context) {
        RetrofitClient.initialize();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isLoggedIn() {
        return getUserNick() != null && getUserPassword() != null && getUserId() != 0;
    }

    public static String getUserNick() {
        return prefs.getString(PREFS_USER_NICK_KEY, null);
    }

    public static String getUserPassword() {
        return prefs.getString(PREFS_USER_PASSWORD_KEY, null);
    }

    public static long getUserId() {
        return prefs.getLong(PREFS_USER_ID_KEY, 0);
    }

    public static void logout() {
        prefs.edit()
                .remove(PREFS_USER_NICK_KEY)
                .remove(PREFS_USER_PASSWORD_KEY)
                .remove(PREFS_USER_ID_KEY)
                .apply();
    }

    public static void saveAuthData(String nick, String password, long userId) {
        prefs.edit()
                .putString(PREFS_USER_NICK_KEY, nick)
                .putString(PREFS_USER_PASSWORD_KEY, password)
                .putLong(PREFS_USER_ID_KEY, userId)
                .apply();
    }

    public static void updateUserNick(String newNick) {
        prefs.edit()
                .putString(PREFS_USER_NICK_KEY, newNick)
                .apply();
    }

    public static void updateUserPassword(String newPassword) {
        prefs.edit()
                .putString(PREFS_USER_PASSWORD_KEY, newPassword)
                .apply();
    }

    public static AuthRequest auth(String nick, String password) {
        return new AuthRequest(nick, password);
    }
}