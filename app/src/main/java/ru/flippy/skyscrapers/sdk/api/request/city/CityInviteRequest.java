package ru.flippy.skyscrapers.sdk.api.request.city;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class CityInviteRequest {

    private long userId;

    public CityInviteRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().profile(userId).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Parser parser = Parser.from(document);
                if (parser.getLink("guildInvite") != null) {
                    listener.onError(Error.ACCESS_DENIED);
                } else {
                    RetrofitClient.getApi().cityInvite(wicket, userId).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            Parser parser = Parser.from(document);
                            if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Приглащение отправлено")) {
                                listener.onSuccess();
                            } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "")) {
                                listener.onError(Error.BUSY);
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