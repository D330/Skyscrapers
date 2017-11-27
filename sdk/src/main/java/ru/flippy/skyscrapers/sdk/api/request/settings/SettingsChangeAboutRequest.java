package ru.flippy.skyscrapers.sdk.api.request.settings;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SettingsChangeAboutRequest {

    private String about;

    public SettingsChangeAboutRequest(String about) {
        this.about = about;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings()
                .error(listener)
                .success(settingsDoc -> {
                    HashMap<String, String> postData = FormParser.parse(settingsDoc)
                            .findByAction("aboutForm")
                            .input("about", about)
                            .build();
                    RetrofitClient.getApi().settingsChangeAbout(settingsDoc.wicket(), postData)
                            .error(listener)
                            .success(resultDoc -> listener.onSuccess());
                });
    }
}
