package ru.flippy.skyscrapers.sdk.api.request.mail;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class MailDeleteReadRequest {

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().mail()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (doc.getLink("delete") == null) {
                            listener.onError(Error.ALREADY);
                        } else {
                            RetrofitClient.getApi().mailDeleteRead(doc.wicket())
                                    .error(listener)
                                    .success(new SourceCallback() {
                                        @Override
                                        public void onResponse(Source doc) {
                                            RetrofitClient.getApi().confirm(doc.wicket())
                                                    .error(listener)
                                                    .success(new SourceCallback() {
                                                        @Override
                                                        public void onResponse(Source doc) {
                                                            listener.onSuccess();
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });
    }
}
