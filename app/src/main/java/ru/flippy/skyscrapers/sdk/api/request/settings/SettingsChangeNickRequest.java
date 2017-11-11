package ru.flippy.skyscrapers.sdk.api.request.settings;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;
import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SettingsChangeNickRequest {

    private String nick;

    public SettingsChangeNickRequest(String nick) {
        this.nick = nick;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settingsChangeNickPage().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                HashMap<String, String> postData = FormParser.parse(document)
                        .findByAction("changeLoginForm")
                        .input("newLogin1", nick)
                        .input("newLogin2", nick)
                        .build();
                RetrofitClient.getApi().settingsChangeNick(wicket, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                    @Override
                    public void onResponse(Document document, long wicket) {
                        Parser parser = Parser.from(document);
                        if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Имя изменено")) {
                            SkyscrapersSDK.updateUserNick(nick);
                            listener.onSuccess();
                        } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "не хватает")) {
                            listener.onError(Error.NOT_ENOUGH);
                        } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "уже занято")) {
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
