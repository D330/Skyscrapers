package ru.flippy.skyscrapers.sdk.api.request.settings;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SettingsChangeCityInviteRequest {

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                RetrofitClient.getApi().settingsChangeCityInvite(wicket).setErrorPoint(listener).enqueue(new DocumentCallback() {
                    @Override
                    public void onResponse(Document document, long wicket) {
                        listener.onSuccess();
                    }
                });
            }
        });
    }
}
