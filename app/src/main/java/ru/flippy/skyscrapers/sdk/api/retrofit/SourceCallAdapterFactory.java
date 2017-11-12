package ru.flippy.skyscrapers.sdk.api.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import ru.flippy.skyscrapers.sdk.api.helper.Source;

public class SourceCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(final Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new CallAdapter<Source, SourceCall>() {
            @Override
            public Type responseType() {
                return returnType;
            }

            @Override
            public SourceCall adapt(Call<Source> call) {
                return new SourceCall(call);
            }
        };
    }
}
