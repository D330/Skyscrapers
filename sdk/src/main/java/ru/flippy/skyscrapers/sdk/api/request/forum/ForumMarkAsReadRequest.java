package ru.flippy.skyscrapers.sdk.api.request.forum;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class ForumMarkAsReadRequest {

    private long sectionId;

    public ForumMarkAsReadRequest(long sectionId) {
        this.sectionId = sectionId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumTopics(sectionId, 1)
                .error(listener)
                .success(topicsDoc -> {
                    if (topicsDoc.getLink("markAsReadLink") == null) {
                        listener.onError(Error.ALREADY);
                    } else {
                        RetrofitClient.getApi().forumMarkAsRead(sectionId, topicsDoc.wicket())
                                .error(listener)
                                .success(resultDoc -> listener.onSuccess());
                    }
                });
    }
}
