package ru.flippy.skyscrapers.sdk.api.request.blacklist;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class BlackListAddRequest {

    private String nick;

    public BlackListAddRequest(String nick) {
        this.nick = nick;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().blacklist()
                .error(listener)
                .success(blacklistDoc -> {
                    if (blacklistDoc.hasForm("addForm")) {
                        listener.onError(Error.LIMIT);
                    } else {
                        HashMap<String, String> postData = FormParser.parse(blacklistDoc)
                                .findByAction("addForm")
                                .input("name", nick)
                                .build();
                        RetrofitClient.getApi().blacklistAdd(blacklistDoc.wicket(), postData)
                                .error(listener)
                                .success(resultDoc -> {
                                    if (resultDoc.hasFeedBack(Feedback.Type.INFO, "в черный список")) {
                                        listener.onSuccess();
                                    } else if (resultDoc.hasFeedBack(Feedback.Type.ERROR, "Такой игрок не найден")) {
                                        listener.onError(Error.NOT_FOUND);
                                    } else {
                                        listener.onError(Error.UNKNOWN);
                                    }
                                });
                    }
                });
    }
}