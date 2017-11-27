package ru.flippy.skyscrapers.sdk.api.request.settings;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;
import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SettingsChangePasswordRequest {

    private String oldPassword, newPassword;

    public SettingsChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings()
                .error(listener)
                .success(settingsDoc -> {
                    HashMap<String, String> postData = FormParser.parse(settingsDoc)
                            .findByAction("passwordForm")
                            .input("oldPassword", oldPassword)
                            .input("newPassword", newPassword)
                            .input("newPassword2", newPassword)
                            .build();
                    RetrofitClient.getApi().settingsChangePassword(settingsDoc.wicket(), postData)
                            .error(listener)
                            .success(resultDoc -> {
                                if (resultDoc.hasFeedBack(Feedback.Type.INFO, "Пароль изменен")) {
                                    SkyscrapersSDK.updateUserPassword(newPassword);
                                    listener.onSuccess();
                                } else if (resultDoc.hasFeedBack(Feedback.Type.ERROR, "Текущий пароль введен неверно")) {
                                    listener.onError(Error.WRONG_DATA);
                                } else {
                                    listener.onError(Error.UNKNOWN);
                                }
                            });
                });
    }
}
