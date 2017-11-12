package ru.flippy.skyscrapers.sdk.api.request.city.settings;

import org.jsoup.nodes.Element;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class CitySettingsChangeAboutRequest {

    private String about;

    public CitySettingsChangeAboutRequest(String about) {
        this.about = about;
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
                                                        .findByAction("guildAboutForm")
                                                        .input("about", about)
                                                        .build();
                                                RetrofitClient.getApi().citySettingsChangeAbout(cityId, doc.wicket(), postData)
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
                    }
                });
    }
}
