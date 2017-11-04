package ru.flippy.skyscrapers.sdk.api.request.settings;

import android.text.TextUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

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

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(newPasswordConfirm)) {
            listener.onError(EMPTY_INPUT);
        } else if (!newPassword.equalsIgnoreCase(newPasswordConfirm)) {
            listener.onError(PASSWORDS_NOT_EQUALS);
        } else {
            RetrofitClient.getApi().settings().enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    Page page = response.body();
                    if (!response.isSuccessful() || page == null) {
                        listener.onError(UNKNOWN);
                    } else {
                        HashMap<String, String> postData = FormParser.parse(page.getDocument())
                                .findByAction("passwordForm")
                                .input("oldPassword", oldPassword)
                                .input("newPassword", newPassword)
                                .input("newPassword2", newPasswordConfirm)
                                .build();
                        RetrofitClient.getApi().settingsChangePassword(page.getWicket(), postData).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    Parser parser = Parser.from(page.getDocument());
                                    if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Пароль изменен")) {
                                        SkyscrapersSDK.updateUserPassword(newPassword);
                                        listener.onSuccess();
                                    } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "Текущий пароль введен неверно")) {
                                        listener.onError(WRONG_OLD_PASSWORD);
                                    } else {
                                        listener.onError(INCORRECT_FORMAT);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Page> call, Throwable t) {
                                listener.onError(NETWORK);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Page> call, Throwable t) {
                    listener.onError(NETWORK);
                }
            });
        }
    }
}
