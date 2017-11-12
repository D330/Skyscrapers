package ru.flippy.skyscrapers.sdk.api.request.forum;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class ForumCreateTopicRequest {

    private long sectionId;
    private String title, body;

    public ForumCreateTopicRequest(long sectionId, String title, String body) {
        this.sectionId = sectionId;
        this.title = title;
        this.body = body;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumCreateTopicPage(sectionId)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        HashMap<String, String> postData = FormParser.parse(doc)
                                .findByAction("StaticPostHandler")
                                .input("staticForm:subject", title)
                                .input("staticForm:text", body)
                                .build();
                        RetrofitClient.getApi().forumCreateTopic(sectionId, postData)
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
