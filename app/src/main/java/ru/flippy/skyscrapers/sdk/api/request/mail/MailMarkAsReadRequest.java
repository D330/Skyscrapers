package ru.flippy.skyscrapers.sdk.api.request.mail;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class MailMarkAsReadRequest {

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().mail()
                .error(listener)
                .success(mailDoc -> {
                    if (mailDoc.getLink("markAsRead") == null) {
                        listener.onError(Error.ALREADY);
                    } else {
                        RetrofitClient.getApi().mailMarkAsRead(mailDoc.wicket())
                                .error(listener)
                                .success(resultDoc -> listener.onSuccess());
                    }
                });
    }
}
