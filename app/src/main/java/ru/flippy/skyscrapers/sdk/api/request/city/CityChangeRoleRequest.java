package ru.flippy.skyscrapers.sdk.api.request.city;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

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
                .success(roleDoc -> {
                    if (roleDoc.getLink(String.valueOf(roleLevel)) == null) {
                        listener.onError(Error.ACCESS_DENIED);
                    } else {
                        RetrofitClient.getApi().cityChangeRole(roleDoc.wicket(), userId, roleLevel)
                                .error(listener)
                                .success(resultDoc -> {
                                    if (roleLevel < 100) {
                                        listener.onSuccess();
                                    } else {
                                        RetrofitClient.getApi().confirm(resultDoc.wicket())
                                                .error(listener)
                                                .success(confirmDoc -> listener.onSuccess());
                                    }
                                });
                    }
                });
    }
}
