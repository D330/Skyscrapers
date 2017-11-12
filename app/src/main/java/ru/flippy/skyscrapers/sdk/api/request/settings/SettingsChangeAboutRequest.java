package ru.flippy.skyscrapers.sdk.api.request.settings;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class SettingsChangeAboutRequest {

    private String about;

    public SettingsChangeAboutRequest(String about) {
        this.about = about;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        HashMap<String, String> postData = FormParser.parse(doc)
                                .findByAction("aboutForm")
                                .input("about", about)
                                .build();
                        RetrofitClient.getApi().settingsChangeAbout(doc.wicket(), postData)
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
