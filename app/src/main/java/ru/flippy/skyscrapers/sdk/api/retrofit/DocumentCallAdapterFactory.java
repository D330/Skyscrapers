package ru.flippy.skyscrapers.sdk.api.retrofit;

import org.jsoup.nodes.Document;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class DocumentCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(final Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new CallAdapter<Document, AdvancedCall>() {
            @Override
            public Type responseType() {
                return returnType;
            }

            @Override
            public AdvancedCall adapt(Call<Document> call) {
                return new AdvancedCall(call);
            }
        };
    }
}
