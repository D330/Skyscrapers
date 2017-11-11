package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class ForumCreateTopicRequest {

    private long sectionId;
    private String title, body;

    public ForumCreateTopicRequest(long sectionId, String title, String body) {
        this.sectionId = sectionId;
        this.title = title;
        this.body = body;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumCreateTopicPage(sectionId).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                HashMap<String, String> postData = FormParser.parse(document)
                        .findByAction("StaticPostHandler")
                        .input("staticForm:subject", title)
                        .input("staticForm:text", body)
                        .build();
                RetrofitClient.getApi().forumCreateTopic(sectionId, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
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
