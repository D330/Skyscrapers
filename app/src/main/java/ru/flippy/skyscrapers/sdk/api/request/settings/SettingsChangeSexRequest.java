package ru.flippy.skyscrapers.sdk.api.request.settings;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class SettingsChangeSexRequest {

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        RetrofitClient.getApi().settingsChangeSex(doc.wicket())
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
