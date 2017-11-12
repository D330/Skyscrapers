package ru.flippy.skyscrapers.sdk.api.request.blacklist;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class BlackListAddRequest {

    private String nick;

    public BlackListAddRequest(String nick) {
        this.nick = nick;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().blacklist()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (doc.checkForm("addForm")) {
                            listener.onError(Error.LIMIT);
                        } else {
                            HashMap<String, String> postData = FormParser.parse(doc)
                                    .findByAction("addForm")
                                    .input("name", nick)
                                    .build();
                            RetrofitClient.getApi().blacklistAdd(doc.wicket(), postData)
                                    .error(listener)
                                    .success(new SourceCallback() {
                                        @Override
                                        public void onResponse(Source doc) {
                                            if (doc.checkFeedBack(Feedback.Type.INFO, "в черный список")) {
                                                listener.onSuccess();
                                            } else if (doc.checkFeedBack(Feedback.Type.ERROR, "Такой игрок не найден")) {
                                                listener.onError(Error.NOT_FOUND);
                                            } else {
                                                listener.onError(Error.UNKNOWN);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}