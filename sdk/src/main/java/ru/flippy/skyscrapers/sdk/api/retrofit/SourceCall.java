package ru.flippy.skyscrapers.sdk.api.retrofit;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.listener.ErrorListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;
import ru.flippy.skyscrapers.sdk.listener.SourceTagCallback;

import static ru.flippy.skyscrapers.sdk.api.Error.NETWORK;

public class SourceCall {

    private Call<Source> call;
    private ErrorListener errorListener;
    private Object tag;

    SourceCall(Call<Source> call) {
        this.call = call;
    }

    public SourceCall setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public SourceCall error(ErrorListener errorListener) {
        this.errorListener = errorListener;
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
        if (errorListener != null) {
            errorListener.onError(errorCode);
        }
    }
}
