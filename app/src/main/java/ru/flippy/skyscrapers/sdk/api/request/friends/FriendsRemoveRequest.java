package ru.flippy.skyscrapers.sdk.api.request.friends;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class FriendsRemoveRequest {

    private long userId;

    public FriendsRemoveRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().profile(userId).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Parser parser = Parser.from(document);
                if (parser.getLink("removeFriendLink") == null) {
                    listener.onError(Error.ALREADY);
                } else {
                    RetrofitClient.getApi().friendsRemove(userId, wicket).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            if (Parser.from(document).checkFeedBack(Parser.FEEDBACK_INFO, "из друзей")) {
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