package ru.flippy.skyscrapers.api.internal.retrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class HttpClient extends OkHttpClient {

    private HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    @Override
    public CookieJar cookieJar() {
        return new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.clear();
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                if (cookieStore.containsKey(url.host())) {
                    return cookieStore.get(url.host());
                } else {
                    return new ArrayList<>();
                }
            }
        };
    }
}
