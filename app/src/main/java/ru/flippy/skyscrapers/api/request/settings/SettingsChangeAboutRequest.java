package ru.flippy.skyscrapers.api.request.settings;

import android.text.TextUtils;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.FormParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class SettingsChangeAboutRequest extends BaseRequest {

    public static final int EMPTY_INPUT = 0;

    private String about;

    public SettingsChangeAboutRequest(String about) {
        this.about = about;
    }

    public void execute(final SimpleRequestListener listener) {
        if (TextUtils.isEmpty(about)) {
            listener.onError(EMPTY_INPUT);
        } else {
            DocumentLoader.connect("http://nebo.mobi/settings").execute(new OnDocumentListener() {
                @Override
                public void onResponse(Document document, long wicket) {
                    String actionUrl = "http://nebo.mobi/settings/?wicket:interface=:" + wicket + ":aboutForm::IFormSubmitListener::";
                    FormParser.parse(document)
                            .findByAction("aboutForm")
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
