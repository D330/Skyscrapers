package ru.flippy.skyscrapers.sdk.api.methods.settings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SettingsChangeSexRequest extends BaseRequest {

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings().enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    RetrofitClient.getApi().settingsChangeSex(page.getWicket()).enqueue(new Callback<Page>() {
                        @Override
                        public void onResponse(Call<Page> call, Response<Page> response) {
                            Page page = response.body();
                            if (!response.isSuccessful() || page == null) {
                                listener.onError(UNKNOWN);
                            } else {
                                listener.onSuccess();
                            }
                        }

                        @Override
                        public void onFailure(Call<Page> call, Throwable t) {
                            listener.onError(NETWORK);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                listener.onError(NETWORK);
            }
        });
    }
}
