package ru.flippy.skyscrapers.sdk.api.request.forum;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class ForumMarkAsReadRequest {

    private long sectionId;

    public ForumMarkAsReadRequest(long sectionId) {
        this.sectionId = sectionId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumTopics(sectionId, 1)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (doc.getLink("markAsReadLink") == null) {
                            listener.onError(Error.ALREADY);
                        } else {
                            RetrofitClient.getApi().forumMarkAsRead(sectionId, doc.wicket())
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
