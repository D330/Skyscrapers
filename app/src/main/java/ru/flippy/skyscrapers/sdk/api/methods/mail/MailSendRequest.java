package ru.flippy.skyscrapers.sdk.api.methods.mail;

import android.text.TextUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class MailSendRequest extends BaseRequest {

    public static final int USER_NOT_FOUND = 0;
    public static final int NOT_ENOUGH_COINS = 1;
    public static final int EMPTY_INPUT = 2;

    private long userId;
    private String message;

    public MailSendRequest(long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(message)) {
            listener.onError(EMPTY_INPUT);
        } else {
            RetrofitClient.getApi().mailSendPage(userId).enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    Page page = response.body();
                    if (!response.isSuccessful() || page == null) {
                        listener.onError(UNKNOWN);
                    } else {
                        Parser parser = Parser.from(page.getDocument());
                        if (parser.checkPageError()) {
                            listener.onError(USER_NOT_FOUND);
                        } else {
                            HashMap<String, String> postData = FormParser.parse(page.getDocument())
                                    .findByAction("sendMessageForm")
                                    .input("text", message)
                                    .build();
                            RetrofitClient.getApi().mailSend(page.getWicket(), userId, postData).enqueue(new Callback<Page>() {
                                @Override
                                public void onResponse(Call<Page> call, Response<Page> response) {
                                    Page page = response.body();
                                    if (!response.isSuccessful() || page == null) {
                                        listener.onError(UNKNOWN);
                                    } else {
                                        if (Parser.from(page.getDocument()).checkFeedBack(Parser.FEEDBACK_ERROR, "Не хватает монет для отправки сообщения")) {
                                            listener.onError(NOT_ENOUGH_COINS);
                                        } else {
                                            listener.onSuccess();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Page> call, Throwable t) {
                                    listener.onError(NETWORK);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<Page> call, Throwable t) {
                    listener.onError(NETWORK);
                }
            });
        }
    }
}
