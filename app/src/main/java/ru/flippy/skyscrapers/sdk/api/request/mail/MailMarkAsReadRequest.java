package ru.flippy.skyscrapers.sdk.api.request.mail;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class MailMarkAsReadRequest {

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().mail().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Parser parser = Parser.from(document);
                if (parser.getLink("markAsRead") == null) {
                    listener.onError(Error.ALREADY);
                } else {
                    RetrofitClient.getApi().mailMarkAsRead(wicket).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            listener.onSuccess();
                        }
                    });
                }
            }
        });
    }
}
