package ru.flippy.skyscrapers.api.internal.retrofit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import ru.flippy.skyscrapers.api.internal.helper.Parser;
import ru.flippy.skyscrapers.api.models.Page;

public class PageConverterFactory extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, Page>() {
            @Override
            public Page convert(ResponseBody response) throws IOException {
                Document document = Jsoup.parse(response.string());
                long wicket = Parser.from(document).getWicket();
                return new Page(document, wicket);
            }
        };
    }
}
