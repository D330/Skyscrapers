package ru.flippy.skyscrapers.sdk.api.retrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

class CookieStore implements CookieJar {

    private HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.clear();
        cookieStore.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (cookieStore.containsKey(url.host())) {
            return cookieStore.get(url.host());
        }
        return new ArrayList<>();
    }
}
