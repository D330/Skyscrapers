package ru.flippy.skyscrapers.sdk.api.request.city;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class CityChangeRoleRequest extends BaseRequest {

    public static final int ACCESS_DENIED = 0;
    public static final String x = "role255Link";
    private long userId;
    private int roleLevel;

    public CityChangeRoleRequest(long userId, int roleLevel) {
        this.userId = userId;
        this.roleLevel = roleLevel;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().cityChangeRolePage(userId).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Parser parser = Parser.from(page.getDocument());
                    if (parser.checkPageError() || parser.getLink(String.valueOf(roleLevel)) == null) {
                        listener.onError(ACCESS_DENIED);
                    } else {
                        RetrofitClient.getApi().cityChangeRole(page.getWicket(), userId, roleLevel).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    Log.d("H", page.getDocument().text());
                                    if (roleLevel == 100) {
                                        RetrofitClient.getApi().confirm(page.getWicket()).enqueue(new Callback<Page>() {
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
                                    } else {
                                        listener.onSuccess();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Page> call, Throwable t) {
                                listener.onError(NETWORK);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                listener.onError(NETWORK);
            }
        });
    }
}
