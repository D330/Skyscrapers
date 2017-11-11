package ru.flippy.skyscrapers.sdk.api.retrofit;

import org.jsoup.nodes.Document;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.listener.Errorable;

import static ru.flippy.skyscrapers.sdk.api.Error.NETWORK;

public class AdvancedCall {

    private Call<Document> call;
    private Errorable errorable;
    private Object tag;

    public AdvancedCall(Call<Document> call) {
        this.call = call;
    }

    public AdvancedCall setErrorPoint(Errorable errorable) {
        this.errorable = errorable;
        return this;
    }

    public AdvancedCall setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public Object getTag() {
        return tag;
    }

    public void enqueue(final DocumentCallback callback) {
        call.enqueue(new Callback<Document>() {
            @Override
            public void onResponse(Call<Document> call, Response<Document> response) {
                Document document = response.body();
                if (document == null) {
                    handleError(NETWORK);
                } else {
                    long wicket = Parser.from(document).getWicket();
                    callback.onResponse(document, wicket);
                }
            }

            @Override
            public void onFailure(Call<Document> call, Throwable t) {
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
