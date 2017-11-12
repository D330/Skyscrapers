package ru.flippy.skyscrapers.sdk.api.request.settings;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;
import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class SettingsChangeNickRequest {

    private String nick;

    public SettingsChangeNickRequest(String nick) {
        this.nick = nick;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settingsChangeNickPage()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        HashMap<String, String> postData = FormParser.parse(doc)
                                .findByAction("changeLoginForm")
                                .input("newLogin1", nick)
                                .input("newLogin2", nick)
                                .build();
                        RetrofitClient.getApi().settingsChangeNick(doc.wicket(), postData)
                                .error(listener)
                                .success(new SourceCallback() {
                                    @Override
                                    public void onResponse(Source doc) {
                                        if (doc.checkFeedBack(Feedback.Type.INFO, "Имя изменено")) {
                                            SkyscrapersSDK.updateUserNick(nick);
                                            listener.onSuccess();
                                        } else if (doc.checkFeedBack(Feedback.Type.ERROR, "не хватает")) {
                                            listener.onError(Error.NOT_ENOUGH);
                                        } else if (doc.checkFeedBack(Feedback.Type.ERROR, "уже занято")) {
                                            listener.onError(Error.BUSY);
                                        } else {
                                            listener.onError(Error.UNKNOWN);
                                        }
                                    }
                                });
                    }
                });
    }
}
