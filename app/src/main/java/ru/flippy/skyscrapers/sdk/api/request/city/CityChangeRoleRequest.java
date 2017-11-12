package ru.flippy.skyscrapers.sdk.api.request.city;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class CityChangeRoleRequest {

    private long userId;
    private int roleLevel;

    public CityChangeRoleRequest(long userId, int roleLevel) {
        this.userId = userId;
        this.roleLevel = roleLevel;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().cityChangeRolePage(userId)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (doc.getLink(String.valueOf(roleLevel)) == null) {
                            listener.onError(Error.ACCESS_DENIED);
                        } else {
                            RetrofitClient.getApi().cityChangeRole(doc.wicket(), userId, roleLevel)
                                    .error(listener)
                                    .success(new SourceCallback() {
                                        @Override
                                        public void onResponse(Source doc) {
                                            if (roleLevel < 100) {
                                                listener.onSuccess();
                                            } else {
                                                RetrofitClient.getApi().confirm(doc.wicket())
                                                        .error(listener)
                                                        .success(new SourceCallback() {
                                                            @Override
                                                            public void onResponse(Source doc) {
                                                                listener.onSuccess();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
