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

public class SettingsChangeNickRequest extends BaseRequest {

    public static final int NOT_ENOUGH_DOLLARS = 0;
    public static final int NICK_BUSY = 1;
    public static final int NICKS_NOT_EQUALS = 2;
    public static final int INCORRECT_FORMAT = 3;
    public static final int EMPTY_INPUT = 4;

    private String newNick, newNickConfirm;

    public SettingsChangeNickRequest(String newNick, String newNickConfirm) {
        this.newNick = newNick;
        this.newNickConfirm = newNickConfirm;
    }

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(newNick) || TextUtils.isEmpty(newNickConfirm)) {
            listener.onError(EMPTY_INPUT);
        } else if (!newNick.equalsIgnoreCase(newNickConfirm)) {
            listener.onError(NICKS_NOT_EQUALS);
        } else {
            RetrofitClient.getApi().settingsChangeNickPage().enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    Page page = response.body();
                    if (!response.isSuccessful() || page == null) {
                        listener.onError(UNKNOWN);
                    } else {
                        HashMap<String, String> postData = FormParser.parse(page.getDocument())
                                .findByAction("changeLoginForm")
                                .input("newLogin1", newNick)
                                .input("newLogin2", newNickConfirm)
                                .build();
                        RetrofitClient.getApi().settingsChangeNick(page.getWicket(), postData).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    Parser parser = Parser.from(page.getDocument());
                                    if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Имя изменено")) {
                                        listener.onSuccess();
                                        SkyscrapersSDK.updateUserNick(newNick);
                                    } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "не хватает")) {
                                        listener.onError(NOT_ENOUGH_DOLLARS);
                                    } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "уже занято")) {
                                        listener.onError(NICK_BUSY);
                                    } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "только буквы")) {
                                        listener.onError(INCORRECT_FORMAT);
                                    } else {
                                        listener.onError(UNKNOWN);
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
