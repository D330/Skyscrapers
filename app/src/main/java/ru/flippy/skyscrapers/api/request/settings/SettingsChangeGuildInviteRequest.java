package ru.flippy.skyscrapers.api.request.settings;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class SettingsChangeGuildInviteRequest extends BaseRequest {

    public void execute(final SimpleRequestListener listener) {
        DocumentLoader.connect("http://nebo.mobi/settings").execute(new OnDocumentListener() {
            @Override
            public void onResponse(Document document, long wicket) {
                String actionUrl = "http://nebo.mobi/settings/?wicket:interface=:" + wicket + ":guildSearchLink::ILinkListener::";
                DocumentLoader.connect(actionUrl).execute(new OnDocumentListener() {
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
