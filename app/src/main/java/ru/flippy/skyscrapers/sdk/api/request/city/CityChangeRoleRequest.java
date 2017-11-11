package ru.flippy.skyscrapers.sdk.api.request.city;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
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
        RetrofitClient.getApi().cityChangeRolePage(userId).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Parser parser = Parser.from(document);
                if (parser.getLink(String.valueOf(roleLevel)) == null) {
                    listener.onError(Error.ACCESS_DENIED);
                } else {
                    RetrofitClient.getApi().cityChangeRole(wicket, userId, roleLevel).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            if (roleLevel < 100) {
                                listener.onSuccess();
                            } else {
                                RetrofitClient.getApi().confirm(wicket).setErrorPoint(listener).enqueue(new DocumentCallback() {
                                    @Override
                                    public void onResponse(Document document, long wicket) {
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
