package ru.flippy.skyscrapers.sdk.api.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static ApiService api;

    public static void initialize() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new QuestionMarkInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(new PageConverterFactory())
                .baseUrl(ApiService.ENDPOINT)
                .build();
        api = retrofit.create(ApiService.class);
    }

    public static Retrofit getClient(){
        return retrofit;
    }

    public static ApiService getApi() {
        return api;
    }
}
