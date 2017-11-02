package ru.flippy.skyscrapers.sdk.api.methods.city;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class CityInviteRequest extends BaseRequest {

    public static final int USER_NOT_FOUND = 0;
    public static final int ACCESS_DENIED = 1;
    public static final int INVITE_BUSY = 2;

    private long userId;

    public CityInviteRequest(long userId) {
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
                    } else if (parser.getLink("guildInvite") != null) {
                        listener.onError(ACCESS_DENIED);
                    } else {
                        RetrofitClient.getApi().cityInvite(page.getWicket(), userId).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    Parser parser = Parser.from(page.getDocument());
                                    if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Приглащение отправлено")) {
                                        listener.onSuccess();
                                    } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "")) {
                                        listener.onError(INVITE_BUSY);
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