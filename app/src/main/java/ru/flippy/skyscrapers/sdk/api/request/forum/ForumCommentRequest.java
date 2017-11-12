package ru.flippy.skyscrapers.sdk.api.request.forum;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class ForumCommentRequest {

    private long topicId, replyId;
    private int page;
    private String message;

    public ForumCommentRequest(long topicId, int page, long replyId, String message) {
        this.topicId = topicId;
        this.page = page;
        this.replyId = replyId;
        this.message = message;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumTopic(topicId, page)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (doc.checkForm("StaticPostHandler")) {
                            if (doc.has("a[class=bl amount cntr][href*=bans]:contains(Вы не можете комментировать этот топик)")) {
                                listener.onError(Error.BAN);
                            } else if (doc.has("div[class=cntr amount m5]:contains(топик закрыт)")
                                    && doc.has("span[class=bl amount cntr]:contains(Вы не можете комментировать топик, потому что тема закрыта)")) {
                                listener.onError(Error.TOPIC_CLOSED);
                            } else {
                                listener.onError(Error.UNKNOWN);
                            }
                        } else {
                            HashMap<String, String> postData = FormParser.parse(doc)
                                    .findByAction("StaticPostHandler")
                                    .input("staticForm:replyId", replyId)
                                    .input("staticForm:text", message)
                                    .build();
                            RetrofitClient.getApi().forumComment(topicId, page, postData)
                                    .error(listener)
                                    .success(new SourceCallback() {
                                        @Override
                                        public void onResponse(Source doc) {
                                            listener.onSuccess();
                                        }
                                    });
                        }
                    }
                });
    }
}
