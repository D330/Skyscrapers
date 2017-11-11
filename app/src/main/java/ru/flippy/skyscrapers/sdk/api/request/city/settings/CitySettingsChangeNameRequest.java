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

public class CitySettingsChangeNameRequest {

    private String name;

    public CitySettingsChangeNameRequest(String name) {
        this.name = name;
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
                                        .findByAction("guildNameForm")
                                        .input("newGuildName1", name)
                                        .input("newGuildName2", name)
                                        .build();
                                RetrofitClient.getApi().citySettingsChangeName(wicket, cityId, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                                    @Override
                                    public void onResponse(Document document, long wicket) {
                                        Parser parser = Parser.from(document);
                                        if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "не хватает")) {
                                            listener.onError(Error.NOT_ENOUGH);
                                        } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "")) {
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
