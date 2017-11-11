package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class ForumEditTopicRequest {

    private long topicId;
    private String title, body;

    public ForumEditTopicRequest(long topicId, String title, String body) {
        this.topicId = topicId;
        this.title = title;
        this.body = body;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumEditTopicPage(topicId).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                HashMap<String, String> postData = FormParser.parse(document)
                        .findByAction("StaticPostHandler")
                        .input("staticForm:subject", title)
                        .input("staticForm:text", body)
                        .build();
                RetrofitClient.getApi().forumEditTopic(topicId, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                    @Override
                    public void onResponse(Document document, long wicket) {
                        if (document.title().equalsIgnoreCase(title)) {
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
