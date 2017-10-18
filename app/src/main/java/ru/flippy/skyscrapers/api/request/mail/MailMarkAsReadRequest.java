package ru.flippy.skyscrapers.api.request.mail;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class MailMarkAsReadRequest extends BaseRequest {

    public static final int NO_UNREADS = 0;

    public void execute(final SimpleRequestListener listener) {
        DocumentLoader.connect("http://nebo.mobi/mail").execute(new OnDocumentListener() {
            @Override
            public void onResponse(Document document, long wicket) {
                ExtremeParser parser = new ExtremeParser(document);
                if (parser.getLink("markAsRead") == null) {
                    listener.onError(NO_UNREADS);
                } else {
                    String actionUrl = "http://nebo.mobi/?wicket:interface=:" + wicket + ":markAsReadLink::ILinkListener::";
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
            }

            @Override
            public void onError() {
                listener.onError(NETWORK);
            }
        });
    }
}
