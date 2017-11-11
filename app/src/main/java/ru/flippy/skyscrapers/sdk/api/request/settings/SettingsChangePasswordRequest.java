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

public class SettingsChangePasswordRequest {

    private String oldPassword, newPassword;

    public SettingsChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                HashMap<String, String> postData = FormParser.parse(document)
                        .findByAction("passwordForm")
                        .input("oldPassword", oldPassword)
                        .input("newPassword", newPassword)
                        .input("newPassword2", newPassword)
                        .build();
                RetrofitClient.getApi().settingsChangePassword(wicket, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                    @Override
                    public void onResponse(Document document, long wicket) {
                        Parser parser = Parser.from(document);
                        if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Пароль изменен")) {
                            SkyscrapersSDK.updateUserPassword(newPassword);
                            listener.onSuccess();
                        } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "Текущий пароль введен неверно")) {
                            listener.onError(Error.WRONG_DATA);
                        } else {
                            listener.onError(Error.UNKNOWN);
                        }
                    }
                });
            }
        });
    }
}
