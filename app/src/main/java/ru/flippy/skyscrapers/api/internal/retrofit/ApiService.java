package ru.flippy.skyscrapers.api.internal.retrofit;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.flippy.skyscrapers.api.models.Page;

public interface ApiService {

    String ENDPOINT = "http://nebo.mobi/";

    @GET("login")
    Call<Page> loginPage();

    @POST("login/¿wicket:interface=:{wicket}:loginForm:loginForm::IFormSubmitListener::")
    @FormUrlEncoded
    Call<Page> login(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);
}
