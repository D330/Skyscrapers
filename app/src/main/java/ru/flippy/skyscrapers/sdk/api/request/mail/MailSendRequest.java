package ru.flippy.skyscrapers.sdk.api.request.mail;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class MailSendRequest {

    private long userId;
    private String message;

    public MailSendRequest(long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().mailSendPage(userId).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Parser parser = Parser.from(document);
                if (parser.checkPageError()) {
                    listener.onError(Error.NOT_FOUND);
                } else {
                    HashMap<String, String> postData = FormParser.parse(document)
                            .findByAction("sendMessageForm")
                            .input("text", message)
                            .build();
                    RetrofitClient.getApi().mailSend(wicket, userId, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            if (Parser.from(document).checkFeedBack(Parser.FEEDBACK_ERROR, "Не хватает монет для отправки сообщения")) {
                                listener.onError(Error.NOT_ENOUGH);
                            } else {
                                listener.onSuccess();
                            }
                        }
                    });
                }
            }
        });
    }
}
