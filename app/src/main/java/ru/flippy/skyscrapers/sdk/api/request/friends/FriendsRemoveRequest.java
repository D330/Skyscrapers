package ru.flippy.skyscrapers.sdk.api.request.friends;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class FriendsRemoveRequest {

    private long userId;

    public FriendsRemoveRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().profile(userId)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (doc.getLink("removeFriendLink") == null) {
                            listener.onError(Error.ALREADY);
                        } else {
                            RetrofitClient.getApi().friendsRemove(userId, doc.wicket())
                                    .error(listener)
                                    .success(new SourceCallback() {
                                        @Override
                                        public void onResponse(Source doc) {
                                            if (doc.checkFeedBack(Feedback.Type.INFO, "из друзей")) {
                                                listener.onSuccess();
                                            } else {
                                                listener.onError(Error.UNKNOWN);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}