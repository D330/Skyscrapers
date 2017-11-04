package ru.flippy.skyscrapers.sdk.api.request.chat;

import android.text.TextUtils;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class ChatSendRequest extends BaseRequest {

    public static final int BAN = 0;
    public static final int LEVEL_LESS = 1;
    public static final int LENGTH_LIMIT = 2;
    public static final int EMPTY_INPUT = 3;

    private String message;

    public ChatSendRequest(String message) {
        this.message = message;
    }

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(message)) {
            listener.onError(EMPTY_INPUT);
        } else {
            RetrofitClient.getApi().chat().enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    Page page = response.body();
                    if (!response.isSuccessful() || page == null) {
                        listener.onError(UNKNOWN);
                    } else {
                        Document document = page.getDocument();
                        long wicket = page.getWicket();
                        if (FormParser.checkForm(document, "messageForm")) {
                            HashMap<String, String> postData = FormParser.parse(document)
                                    .findByAction("messageForm")
                                    .input("text", message)
                                    .build();
                            RetrofitClient.getApi().chatSend(wicket, postData).enqueue(new Callback<Page>() {
                                @Override
                                public void onResponse(Call<Page> call, Response<Page> response) {
                                    Page page = response.body();
                                    if (!response.isSuccessful() || page == null) {
                                        listener.onError(UNKNOWN);
                                    } else {
                                        if (Parser.from(page.getDocument()).checkFeedBack(Parser.FEEDBACK_ERROR, "Количество символов в")) {
                                            listener.onError(LENGTH_LIMIT);
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
                        } else if (!document.select("div.nfl>div:contains(В чат можно писать с 10 уровня)").isEmpty()) {
                            listener.onError(LEVEL_LESS);
                        } else if (!document.select("div[class=ntfy notify]>div:contains(Вы временно не можете писать в чате, т.к. нарушали правила общения)").isEmpty()) {
                            listener.onError(BAN);
                        } else {
                            listener.onError(UNKNOWN);
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