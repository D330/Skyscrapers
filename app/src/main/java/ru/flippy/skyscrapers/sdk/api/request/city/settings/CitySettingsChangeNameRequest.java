package ru.flippy.skyscrapers.sdk.api.request.city.settings;

import org.jsoup.nodes.Element;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class CitySettingsChangeNameRequest {

    private String name;

    public CitySettingsChangeNameRequest(String name) {
        this.name = name;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().city()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (doc.getLink("/storage/0/") == null) {
                            listener.onError(Error.NO_CITY);
                        } else {
                            Element settingsLink = doc.getLink("/about/0/");
                            if (settingsLink == null) {
                                listener.onError(Error.ACCESS_DENIED);
                            } else {
                                final long cityId = Utils.getValueAfterLastSlash(settingsLink.attr("href"));
                                RetrofitClient.getApi().citySettings(cityId)
                                        .error(listener)
                                        .success(new SourceCallback() {
                                            @Override
                                            public void onResponse(Source doc) {
                                                HashMap<String, String> postData = FormParser.parse(doc)
                                                        .findByAction("guildNameForm")
                                                        .input("newGuildName1", name)
                                                        .input("newGuildName2", name)
                                                        .build();
                                                RetrofitClient.getApi().citySettingsChangeName(doc.wicket(), cityId, postData)
                                                        .error(listener)
                                                        .success(new SourceCallback() {
                                                            @Override
                                                            public void onResponse(Source doc) {
                                                                if (doc.checkFeedBack(Feedback.Type.ERROR, "не хватает")) {
                                                                    listener.onError(Error.NOT_ENOUGH);
                                                                } else if (doc.checkFeedBack(Feedback.Type.ERROR, "")) {
                                                                    listener.onError(Error.BUSY);
                                                                } else {
                                                                    listener.onSuccess();
                                                                }
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}
