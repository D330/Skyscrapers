package ru.flippy.skyscrapers.api.request.friends;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class FriendsRemoveRequest extends BaseRequest {

    public static final int USER_NOT_FOUND = 0;
    public static final int NOT_FRIEND_SPECIFIED = 1;

    private long userId;

    public FriendsRemoveRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final SimpleRequestListener listener) {
        DocumentLoader.connect("http://nebo.mobi/tower/id/" + userId).execute(new OnDocumentListener() {
            @Override
            public void onResponse(Document document, long wicket) {
                ExtremeParser parser = new ExtremeParser(document);
                if (parser.checkPageError()) {
                    listener.onError(USER_NOT_FOUND);
                } else if (parser.getLink("removeFriendLink") == null) {
                    listener.onError(NOT_FRIEND_SPECIFIED);
                } else {
                    String actionUrl = "http://nebo.mobi/tower/id/" + userId + "/?wicket:interface=:" + wicket + ":removeFriendLink::ILinkListener::";
                    DocumentLoader.connect(actionUrl).execute(new OnDocumentListener() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            ExtremeParser parser = new ExtremeParser(document);
                            if (parser.checkFeedBack(ExtremeParser.FEEDBACK_INFO, "из друзей")) {
                                listener.onSuccess();
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
