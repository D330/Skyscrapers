package ru.flippy.skyscrapers.sdk.api.request.friends;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class FriendsRemoveRequest {

    private long userId;

    public FriendsRemoveRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().profile(userId)
                .error(listener)
                .success(profileDoc -> {
                    if (profileDoc.getLink("removeFriendLink") == null) {
                        listener.onError(Error.ALREADY);
                    } else {
                        RetrofitClient.getApi().friendsRemove(userId, profileDoc.wicket())
                                .error(listener)
                                .success(resultDoc -> {
                                    if (resultDoc.hasFeedBack(Feedback.Type.INFO, "из друзей")) {
                                        listener.onSuccess();
                                    } else {
                                        listener.onError(Error.UNKNOWN);
                                    }
                                });
                    }
                });
    }
}