package ru.flippy.skyscrapers.sdk.api.request.forum;

import android.text.TextUtils;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class ForumEditTopicRequest extends BaseRequest {

    public static final int PAGE_NOT_FOUND = 0;
    public static final int ACCESS_DENIED = 1;
    public static final int EMPTY_INPUT = 2;

    private long topicId;
    private String title, body;

    public ForumEditTopicRequest(long topicId, String title, String body) {
        this.topicId = topicId;
        this.title = title;
        this.body = body;
    }

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(body)) {
            listener.onError(EMPTY_INPUT);
        } else {
            RetrofitClient.getApi().forumEditTopicPage(topicId).enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    Page page = response.body();
                    if (!response.isSuccessful() || page == null) {
                        listener.onError(UNKNOWN);
                    } else {
                        Document document = page.getDocument();
                        if (Parser.from(document).checkPageError()) {
                            listener.onError(PAGE_NOT_FOUND);
                        } else {
                            HashMap<String, String> postData = FormParser.parse(document)
                                    .findByAction("StaticPostHandler")
                                    .input("staticForm:subject", title)
                                    .input("staticForm:text", body)
                                    .build();
                            RetrofitClient.getApi().forumEditTopic(topicId, postData).enqueue(new Callback<Page>() {
                                @Override
                                public void onResponse(Call<Page> call, Response<Page> response) {
                                    Page page = response.body();
                                    if (!response.isSuccessful() || page == null) {
                                        listener.onError(UNKNOWN);
                                    } else if (page.getDocument().title().equalsIgnoreCase(title)) {
                                        listener.onSuccess();
                                    } else {
                                        listener.onError(UNKNOWN);
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
}
