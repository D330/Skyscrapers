package ru.flippy.skyscrapers.sdk.api.request.settings;

import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SettingsChangeSexRequest {

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings()
                .error(listener)
                .success(settingsDoc -> RetrofitClient.getApi().settingsChangeSex(settingsDoc.wicket())
                        .error(listener)
                        .success(resultDoc -> listener.onSuccess()));
    }
}
