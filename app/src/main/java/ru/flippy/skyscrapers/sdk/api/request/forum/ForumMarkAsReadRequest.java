package ru.flippy.skyscrapers.sdk.api.request.forum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class ForumMarkAsReadRequest extends BaseRequest {

    public static final int NO_UNREAD = 0;

    private long sectionId;

    public ForumMarkAsReadRequest(long sectionId) {
        this.sectionId = sectionId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumTopics(sectionId, 1).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    if (Parser.from(page.getDocument()).getLink("markAsReadLink") == null) {
                        listener.onError(NO_UNREAD);
                    } else {
                        RetrofitClient.getApi().forumMarkAsRead(sectionId, page.getWicket()).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    listener.onSuccess();
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
