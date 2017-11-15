package ru.flippy.skyscrapers.sdk.api.request.mail;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
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
        RetrofitClient.getApi().mailSendPage(userId)
                .error(listener)
                .success(sendDoc -> {
                    HashMap<String, String> postData = FormParser.parse(sendDoc)
                            .findByAction("sendMessageForm")
                            .input("text", message)
                            .build();
                    RetrofitClient.getApi().mailSend(sendDoc.wicket(), userId, postData)
                            .error(listener)
                            .success(resultDoc -> {
                                if (resultDoc.hasFeedBack(Feedback.Type.ERROR, "Не хватает монет для отправки сообщения")) {
                                    listener.onError(Error.NOT_ENOUGH);
                                } else {
                                    listener.onSuccess();
                                }
                            });
                });
    }
}