package ru.flippy.skyscrapers.sdk.api.request.chat;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class ChatSendRequest {

    private String message;

    public ChatSendRequest(String message) {
        this.message = message;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().chat()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (doc.checkForm("messageForm")) {
                            HashMap<String, String> postData = FormParser.parse(doc)
                                    .findByAction("messageForm")
                                    .input("text", message)
                                    .build();
                            RetrofitClient.getApi().chatSend(doc.wicket(), postData)
                                    .error(listener)
                                    .success(new SourceCallback() {
                                        @Override
                                        public void onResponse(Source doc) {
                                            if (doc.checkFeedBack(Feedback.Type.ERROR, "Количество символов")) {
                                                listener.onError(Error.LIMIT);
                                            } else {
                                                listener.onSuccess();
                                            }
                                        }
                                    });
                        } else {
                            if (doc.has("div.nfl>div:contains(В чат можно писать с 10 уровня)")) {
                                listener.onError(Error.LEVEL_LESS);
                            } else if (doc.has("div[class=ntfy notify]>div:contains(Вы временно не можете писать в чате, т.к. нарушали правила общения)")) {
                                listener.onError(Error.BAN);
                            } else {
                                listener.onError(Error.UNKNOWN);
                            }
                        }
                    }
                });
    }
}