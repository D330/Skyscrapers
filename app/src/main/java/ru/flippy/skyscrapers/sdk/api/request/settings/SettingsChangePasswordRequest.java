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

public class SettingsChangePasswordRequest {

    private String oldPassword, newPassword;

    public SettingsChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        HashMap<String, String> postData = FormParser.parse(doc)
                                .findByAction("passwordForm")
                                .input("oldPassword", oldPassword)
                                .input("newPassword", newPassword)
                                .input("newPassword2", newPassword)
                                .build();
                        RetrofitClient.getApi().settingsChangePassword(doc.wicket(), postData)
                                .error(listener)
                                .success(new SourceCallback() {
                                    @Override
                                    public void onResponse(Source doc) {
                                        if (doc.checkFeedBack(Feedback.Type.INFO, "Пароль изменен")) {
                                            SkyscrapersSDK.updateUserPassword(newPassword);
                                            listener.onSuccess();
                                        } else if (doc.checkFeedBack(Feedback.Type.ERROR, "Текущий пароль введен неверно")) {
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
