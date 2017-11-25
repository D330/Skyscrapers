package ru.flippy.skyscrapers.sdk.api.retrofit;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import ru.flippy.skyscrapers.sdk.api.helper.Source;

class SourceConverterFactory extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return (Converter<ResponseBody, Source>) response -> new Source(Jsoup.parse(response.string()));
    }
}
