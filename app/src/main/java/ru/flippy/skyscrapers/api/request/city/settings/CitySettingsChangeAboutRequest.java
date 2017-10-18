package ru.flippy.skyscrapers.api.request.city.settings;

import android.text.TextUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ru.flippy.skyscrapers.api.Utils;
import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.helper.FormParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class CitySettingsChangeAboutRequest extends BaseRequest {

    public static final int NO_CITY = 0;
    public static final int ACCESS_DENIED = 1;
    public static final int EMPTY_INPUT = 2;

    private String about;

    public CitySettingsChangeAboutRequest(String about) {
        this.about = about;
    }

    public void execute(final SimpleRequestListener listener) {
        if (TextUtils.isEmpty(about)) {
            listener.onError(EMPTY_INPUT);
        } else {
            DocumentLoader.connect("http://nebo.mobi/city/").execute(new OnDocumentListener() {
                @Override
                public void onResponse(Document document, long wicket) {
                    ExtremeParser parser = new ExtremeParser(document);
                    if (parser.getLink("/storage/0/") == null) {
                        listener.onError(NO_CITY);
                    } else {
                        Element settingsLink = parser.getLink("/about/0/");
                        if (settingsLink == null) {
                            listener.onError(ACCESS_DENIED);
                        } else {
                            final long cityId = Utils.getValueAfterLastSlash(settingsLink.attr("href"));
                            DocumentLoader.connect("http://nebo.mobi/city/about/0/" + cityId).execute(new OnDocumentListener() {
                                @Override
                                public void onResponse(Document document, long wicket) {
                                    String actionUrl = "http://nebo.mobi/city/about/0/" + cityId + "/?wicket:interface=:" + wicket + ":guildAboutForm::IFormSubmitListener::";
                                    FormParser.parse(document)
                                            .findByAction("guildAboutForm")
                                            .input("about", about)
                                            .connect(actionUrl)
                                            .execute(new OnDocumentListener() {
                                                @Override
                                                public void onResponse(Document document, long wicket) {
                                                    listener.onSuccess();
                                                }

                                                @Override
                                                public void onError() {
                                                    listener.onError(NETWORK);
                                                }
                                            });
                                }

                                @Override
                                public void onError() {
                                    listener.onError(NETWORK);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onError() {
                    listener.onError(NETWORK);
                }
            });
        }
    }
}
