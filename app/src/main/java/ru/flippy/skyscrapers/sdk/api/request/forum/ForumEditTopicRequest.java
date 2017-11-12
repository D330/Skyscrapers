package ru.flippy.skyscrapers.sdk.api.request.forum;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class ForumEditTopicRequest {

    private long topicId;
    private String title, body;

    public ForumEditTopicRequest(long topicId, String title, String body) {
        this.topicId = topicId;
        this.title = title;
        this.body = body;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumEditTopicPage(topicId)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        HashMap<String, String> postData = FormParser.parse(doc)
                                .findByAction("StaticPostHandler")
                                .input("staticForm:subject", title)
                                .input("staticForm:text", body)
                                .build();
                        RetrofitClient.getApi().forumEditTopic(topicId, postData)
                                .error(listener)
                                .success(new SourceCallback() {
                                    @Override
                                    public void onResponse(Source doc) {
                                        if (doc.title().equalsIgnoreCase(title)) {
                                            listener.onSuccess();
                                        } else {
                                            listener.onError(Error.UNKNOWN);
                                        }
                                    }
                                });
                    }
                });
    }
}
