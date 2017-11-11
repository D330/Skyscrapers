package ru.flippy.skyscrapers.sdk.api.request.chat;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class ChatSendRequest {

    private String message;

    public ChatSendRequest(String message) {
        this.message = message;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().chat().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                if (FormParser.checkForm(document, "messageForm")) {
                    HashMap<String, String> postData = FormParser.parse(document)
                            .findByAction("messageForm")
                            .input("text", message)
                            .build();
                    RetrofitClient.getApi().chatSend(wicket, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            if (Parser.from(document).checkFeedBack(Parser.FEEDBACK_ERROR, "Количество символов в")) {
                                listener.onError(Error.LIMIT);
                            } else {
                                listener.onSuccess();
                            }
                        }
                    });
                } else if (!document.select("div.nfl>div:contains(В чат можно писать с 10 уровня)").isEmpty()) {
                    listener.onError(Error.LEVEL_LESS);
                } else if (!document.select("div[class=ntfy notify]>div:contains(Вы временно не можете писать в чате, т.к. нарушали правила общения)").isEmpty()) {
                    listener.onError(Error.BAN);
                } else {
                    listener.onError(Error.UNKNOWN);
                }
            }
        });
    }
}