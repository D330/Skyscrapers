package ru.flippy.skyscrapers.sdk.api.request.city;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class CityInviteRequest {

    private long userId;

    public CityInviteRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().profile(userId)
                .error(listener)
                .success(profileDoc -> {
                    if (profileDoc.getLink("guildInvite") == null) {
                        listener.onError(Error.ACCESS_DENIED);
                    } else {
                        RetrofitClient.getApi().cityInvite(profileDoc.wicket(), userId)
                                .error(listener)
                                .success(resultDoc -> {
                                    if (resultDoc.hasFeedBack(Feedback.Type.INFO, "Приглащение отправлено")) {
                                        listener.onSuccess();
                                    } else if (resultDoc.hasFeedBack(Feedback.Type.ERROR, "")) {
                                        listener.onError(Error.BUSY);
                                    } else {
                                        listener.onError(Error.UNKNOWN);
                                    }
                                });
                    }
                });
    }
}