package ru.flippy.skyscrapers.sdk.api.methods.friends;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class FriendsAddByIdRequest extends BaseRequest {

    public static final int USER_NOT_FOUND = 0;
    public static final int FRIEND_SPECIFIED = 1;

    private long userId;

    public FriendsAddByIdRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().profile(userId).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Parser parser = Parser.from(page.getDocument());
                    if (parser.checkPageError()) {
                        listener.onError(USER_NOT_FOUND);
                    } else if (parser.getLink("addFriendLink") == null) {
                        listener.onError(FRIEND_SPECIFIED);
                    } else {
                        RetrofitClient.getApi().friendsAdd(page.getWicket(), userId).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    if (Parser.from(page.getDocument()).checkFeedBack(Parser.FEEDBACK_INFO, "в друзья")) {
                                        listener.onSuccess();
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
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                listener.onError(NETWORK);
            }
        });
    }
}