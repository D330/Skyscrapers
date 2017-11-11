package ru.flippy.skyscrapers.sdk.api.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitClient {

    private static ApiService skyscrapersApi;

    public static void initialize() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new QuestionMarkInterceptor())
                .cookieJar(new CookieStore())
                .build();
        Retrofit skyscrapersClient = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(new DocumentConverterFactory())
                .addCallAdapterFactory(new DocumentCallAdapterFactory())
                .baseUrl(ApiService.ENDPOINT)
                .build();
        skyscrapersApi = skyscrapersClient.create(ApiService.class);
    }

    public static ApiService getApi() {
        return skyscrapersApi;
    }
}
