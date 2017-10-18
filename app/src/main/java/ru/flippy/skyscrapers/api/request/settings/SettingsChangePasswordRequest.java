package ru.flippy.skyscrapers.api.request.settings;

import android.text.TextUtils;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.SkyscrapersAPI;
import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.helper.FormParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class SettingsChangePasswordRequest extends BaseRequest {

    public static final int WRONG_OLD_PASSWORD = 0;
    public static final int PASSWORDS_NOT_EQUALS = 1;
    public static final int INCORRECT_FORMAT = 2;
    public static final int EMPTY_INPUT = 3;

    private String oldPassword, newPassword, newPasswordConfirm;

    public SettingsChangePasswordRequest(String oldPassword, String newPassword, String newPasswordConfirm) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public void execute(final SimpleRequestListener listener) {
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(newPasswordConfirm)) {
            listener.onError(EMPTY_INPUT);
        } else if (!newPassword.equalsIgnoreCase(newPasswordConfirm)) {
            listener.onError(PASSWORDS_NOT_EQUALS);
        } else {
            DocumentLoader.connect("http://nebo.mobi/settings").execute(new OnDocumentListener() {
                @Override
                public void onResponse(Document document, long wicket) {
                    String actionUrl = "http://nebo.mobi/settings/?wicket:interface=:" + wicket + ":passwordForm::IFormSubmitListener::";
                    FormParser.parse(document)
                            .findByAction("passwordForm")
                            .input("oldPassword", oldPassword)
                            .input("newPassword", newPassword)
                            .input("newPassword2", newPasswordConfirm)
                            .connect(actionUrl)
                            .execute(new OnDocumentListener() {
                                @Override
                                public void onResponse(Document document, long wicket) {
                                    ExtremeParser parser = new ExtremeParser(document);
                                    if (parser.checkFeedBack(ExtremeParser.FEEDBACK_INFO, "Пароль изменен")) {
                                        SkyscrapersAPI.updateUserPassword(newPassword);
                                        listener.onSuccess();
                                    } else if (parser.checkFeedBack(ExtremeParser.FEEDBACK_ERROR, "Текущий пароль введен неверно")) {
                                        listener.onError(WRONG_OLD_PASSWORD);
                                    } else {
                                        listener.onError(INCORRECT_FORMAT);
                                    }
                                }

                                @Override
                                public void onError() {
                                    listener.onError(NETWORK);
                                }
                            });
                }

                @Override
                public void onError() {
                    listener.onError(NETWORK);
                }
            });
        }
    }
}
