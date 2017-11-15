package ru.flippy.skyscrapers.sdk.api.request.settings;

import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SettingsChangeCityInviteRequest {

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings()
                .error(listener)
                .success(settingsDoc -> RetrofitClient.getApi().settingsChangeCityInvite(settingsDoc.wicket())
                        .error(listener)
                        .success(resultDoc -> listener.onSuccess()));
    }
}
