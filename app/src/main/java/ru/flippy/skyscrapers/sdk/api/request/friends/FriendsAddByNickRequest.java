package ru.flippy.skyscrapers.sdk.api.request.friends;

import android.text.TextUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class FriendsAddByNickRequest extends BaseRequest {

    public static final int FRIENDS_LIMIT = 0;
    public static final int USER_NOT_FOUND = 1;
    public static final int EMPTY_INPUT = 2;

    private String nick;

    public FriendsAddByNickRequest(String nick) {
        this.nick = nick;
    }

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(nick)) {
            listener.onError(EMPTY_INPUT);
        } else {
            RetrofitClient.getApi().friends().enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    Page page = response.body();
                    if (!response.isSuccessful() || page == null) {
                        listener.onError(UNKNOWN);
                    } else if (!FormParser.checkForm(page.getDocument(), "addForm")) {
                        listener.onError(FRIENDS_LIMIT);
                    } else {
                        HashMap<String, String> postData = FormParser.parse(page.getDocument())
                                .findByAction("addForm")
                                .input("name", nick)
                                .build();
                        RetrofitClient.getApi().friendsAdd(page.getWicket(), postData).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    Parser parser = Parser.from(page.getDocument());
                                    if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "в друзья")) {
                                        listener.onSuccess();
                                    } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "Такой игрок не найден")) {
                                        listener.onError(USER_NOT_FOUND);
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
