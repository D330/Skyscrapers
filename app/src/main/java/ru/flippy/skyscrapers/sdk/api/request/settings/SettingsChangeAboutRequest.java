package ru.flippy.skyscrapers.sdk.api.request.settings;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SettingsChangeAboutRequest {

    private String about;

    public SettingsChangeAboutRequest(String about) {
        this.about = about;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                HashMap<String, String> postData = FormParser.parse(document)
                        .findByAction("aboutForm")
                        .input("about", about)
                        .build();
                RetrofitClient.getApi().settingsChangeAbout(wicket, postData).enqueue(new DocumentCallback() {
                    @Override
                    public void onResponse(Document document, long wicket) {
                        listener.onSuccess();
                    }
                });
            }
        });
    }
}
