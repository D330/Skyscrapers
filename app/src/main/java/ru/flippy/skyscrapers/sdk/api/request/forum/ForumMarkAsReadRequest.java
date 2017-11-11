package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class ForumMarkAsReadRequest {

    private long sectionId;

    public ForumMarkAsReadRequest(long sectionId) {
        this.sectionId = sectionId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumTopics(sectionId, 1).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                if (Parser.from(document).getLink("markAsReadLink") == null) {
                    listener.onError(Error.ALREADY);
                } else {
                    RetrofitClient.getApi().forumMarkAsRead(sectionId, wicket).setErrorPoint(listener).enqueue(new DocumentCallback() {
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
