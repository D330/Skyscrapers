package ru.flippy.skyscrapers.sdk.api.request.city.settings;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class CitySettingsChangeAboutRequest {

    private String about;

    public CitySettingsChangeAboutRequest(String about) {
        this.about = about;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().city().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Parser parser = Parser.from(document);
                if (parser.getLink("/storage/0/") == null) {
                    listener.onError(Error.NO_CITY);
                } else {
                    Element settingsLink = parser.getLink("/about/0/");
                    if (settingsLink == null) {
                        listener.onError(Error.ACCESS_DENIED);
                    } else {
                        final long cityId = Utils.getValueAfterLastSlash(settingsLink.attr("href"));
                        RetrofitClient.getApi().citySettings(cityId).setErrorPoint(listener).enqueue(new DocumentCallback() {
                            @Override
                            public void onResponse(Document document, long wicket) {
                                HashMap<String, String> postData = FormParser.parse(document)
                                        .findByAction("guildAboutForm")
                                        .input("about", about)
                                        .build();
                                RetrofitClient.getApi().citySettingsChangeAbout(cityId, wicket, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                                    @Override
                                    public void onResponse(Document document, long wicket) {
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
