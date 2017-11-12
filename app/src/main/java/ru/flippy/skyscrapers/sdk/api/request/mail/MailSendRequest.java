package ru.flippy.skyscrapers.sdk.api.request.mail;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class MailSendRequest {

    private long userId;
    private String message;

    public MailSendRequest(long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().mailSendPage(userId)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        HashMap<String, String> postData = FormParser.parse(doc)
                                .findByAction("sendMessageForm")
                                .input("text", message)
                                .build();
                        RetrofitClient.getApi().mailSend(doc.wicket(), userId, postData)
                                .error(listener)
                                .success(new SourceCallback() {
                                    @Override
                                    public void onResponse(Source doc) {
                                        if (doc.checkFeedBack(Feedback.Type.ERROR, "Не хватает монет для отправки сообщения")) {
                                            listener.onError(Error.NOT_ENOUGH);
                                        } else {
                                            listener.onSuccess();
                                        }
                                    }
                                });
                    }
                });
    }
}