package ru.flippy.skyscrapers.sdk.api.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.listener.Errorable;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;
import ru.flippy.skyscrapers.sdk.listener.SourceTagCallback;

import static ru.flippy.skyscrapers.sdk.api.Error.NETWORK;

public class SourceCall {

    private Call<Source> call;
    private Errorable errorable;
    private Object tag;

    SourceCall(Call<Source> call) {
        this.call = call;
    }

    public SourceCall setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public SourceCall error(Errorable errorable) {
        this.errorable = errorable;
        return this;
    }

    public void success(final SourceCallback callback) {
        call.enqueue(new Callback<Source>() {
            @Override
            public void onResponse(Call<Source> call, Response<Source> response) {
                Source source = response.body();
                if (source == null) {
                    handleError(NETWORK);
                } else {
                    callback.onResponse(source);
                }
            }

            @Override
            public void onFailure(Call<Source> call, Throwable t) {
                handleError(NETWORK);
            }
        });
    }

    public void success(final SourceTagCallback callback) {
        call.enqueue(new Callback<Source>() {
            @Override
            public void onResponse(Call<Source> call, Response<Source> response) {
                Source source = response.body();
                if (source == null) {
                    handleError(NETWORK);
                } else {
                    callback.onResponse(tag, source);
                }
            }

            @Override
            public void onFailure(Call<Source> call, Throwable t) {
                handleError(NETWORK);
            }
        });
    }

    private void handleError(int errorCode) {
        if (errorable != null) {
            errorable.onError(errorCode);
        }
    }
}
