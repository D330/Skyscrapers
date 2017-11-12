package ru.flippy.skyscrapers.sdk.api.request.city;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class CityInviteRequest {

    private long userId;

    public CityInviteRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().profile(userId)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (doc.checkLink("guildInvite")) {
                            listener.onError(Error.ACCESS_DENIED);
                        } else {
                            RetrofitClient.getApi().cityInvite(doc.wicket(), userId)
                                    .error(listener)
                                    .success(new SourceCallback() {
                                        @Override
                                        public void onResponse(Source doc) {
                                            if (doc.checkFeedBack(Feedback.Type.INFO, "Приглащение отправлено")) {
                                                listener.onSuccess();
                                            } else if (doc.checkFeedBack(Feedback.Type.ERROR, "")) {
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