package ru.flippy.skyscrapers.sdk.api.request.mail;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class MailDeleteReadRequest {

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().mail()
                .error(listener)
                .success(mailDoc -> {
                    if (mailDoc.getLink("delete") == null) {
                        listener.onError(Error.ALREADY);
                    } else {
                        RetrofitClient.getApi().mailDeleteRead(mailDoc.wicket())
                                .error(listener)
                                .success(confirmDoc -> RetrofitClient.getApi().confirm(confirmDoc.wicket())
                                        .error(listener)
                                        .success(resultDoc -> listener.onSuccess()));
                    }
                });
    }
}
