package ru.flippy.skyscrapers.sdk.api.retrofit;

import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionMarkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String originalUrl = original.url().toString();
        Log.d("QuestionMarkInterceptor", originalUrl.replace(URLEncoder.encode("¿", "UTF-8"), "?"));
        Request request = original.newBuilder()
                .method(original.method(), original.body())
                .url(originalUrl.replace(URLEncoder.encode("¿", "UTF-8"), "?"))
                .build();
        return chain.proceed(request);
    }
}
