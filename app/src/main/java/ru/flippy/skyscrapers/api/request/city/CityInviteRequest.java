package ru.flippy.skyscrapers.api.request.city;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class CityInviteRequest extends BaseRequest {

    public static final int USER_NOT_FOUND = 0;
    public static final int ACCESS_DENIED = 1;
    public static final int INVITE_BUSY = 2;

    private long userId;

    public CityInviteRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final SimpleRequestListener listener) {
        DocumentLoader.connect("http://nebo.mobi/tower/id/" + userId).execute(new OnDocumentListener() {
            @Override
            public void onResponse(Document document, long wicket) {
                ExtremeParser parser = new ExtremeParser(document);
                if (parser.checkPageError()) {
                    listener.onError(USER_NOT_FOUND);
                } else if (parser.getLink("guildInvite") != null) {
                    listener.onError(ACCESS_DENIED);
                } else {
                    String actionUrl = "http://nebo.mobi/tower/id/" + userId + "/?wicket:interface=:" + wicket + ":guildInviteLink::ILinkListener::";
                    DocumentLoader.connect(actionUrl).execute(new OnDocumentListener() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            ExtremeParser parser = new ExtremeParser(document);
                            if (parser.checkFeedBack(ExtremeParser.FEEDBACK_INFO, "Приглащение отправлено")) {
                                listener.onSuccess();
                            } else if (parser.checkFeedBack(ExtremeParser.FEEDBACK_ERROR, "")) {
                                listener.onError(INVITE_BUSY);
                            } else {
                                listener.onError(UNKNOWN);
                            }
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