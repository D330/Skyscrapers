package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class ForumCommentRequest {

    private long topicId, replyId;
    private int page;
    private String message;

    public ForumCommentRequest(long topicId, int page, String message) {
        this.topicId = topicId;
        this.page = page;
        this.message = message;
    }

    public ForumCommentRequest(long topicId, int page, long replyId, String message) {
        this.topicId = topicId;
        this.page = page;
        this.replyId = replyId;
        this.message = message;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumTopic(topicId, page).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                if (!FormParser.checkForm(document, "StaticPostHandler")) {
                    if (!document.select("a[class=bl amount cntr][href*=bans]:contains(Вы не можете комментировать этот топик)").isEmpty()) {
                        listener.onError(Error.BAN);
                    } else if (!document.select("div[class=cntr amount m5]:contains(топик закрыт)").isEmpty()
                            && !document.select("span[class=bl amount cntr]:contains(Вы не можете комментировать топик, потому что тема закрыта)").isEmpty()) {
                        listener.onError(Error.TOPIC_CLOSED);
                    } else {
                        listener.onError(Error.UNKNOWN);
                    }
                } else {
                    HashMap<String, String> postData = FormParser.parse(document)
                            .findByAction("StaticPostHandler")
                            .input("staticForm:replyId", replyId)
                            .input("staticForm:text", message)
                            .build();
                    RetrofitClient.getApi().forumComment(topicId, page, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            listener.onSuccess();
                        }
                    });
                }
            }
        });
    }
}
