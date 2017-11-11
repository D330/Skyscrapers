package ru.flippy.skyscrapers.sdk.api.request.friends;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class FriendsAddByIdRequest {

    private long userId;

    public FriendsAddByIdRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().profile(userId).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                if (Parser.from(document).getLink("addFriendLink") == null) {
                    listener.onError(Error.ALREADY);
                } else {
                    RetrofitClient.getApi().friendsAdd(wicket, userId).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            if (Parser.from(document).checkFeedBack(Parser.FEEDBACK_INFO, "в друзья")) {
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