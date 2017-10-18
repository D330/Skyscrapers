package ru.flippy.skyscrapers.api.request.friends;

import android.text.TextUtils;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.helper.FormParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class FriendsAddByNickRequest extends BaseRequest {

    public static final int FRIENDS_LIMIT = 0;
    public static final int USER_NOT_FOUND = 1;
    public static final int EMPTY_INPUT = 2;

    private String nick;

    public FriendsAddByNickRequest(String nick) {
        this.nick = nick;
    }

    public void execute(final SimpleRequestListener listener) {
        if (TextUtils.isEmpty(nick)) {
            listener.onError(EMPTY_INPUT);
        } else {
            DocumentLoader.connect("http://nebo.mobi/friends").execute(new OnDocumentListener() {
                @Override
                public void onResponse(Document document, long wicket) {
                    if (!FormParser.checkForm(document, "addForm")) {
                        listener.onError(FRIENDS_LIMIT);
                    } else {
                        String actionUrl = "http://nebo.mobi/friends/?wicket:interface=:" + wicket + ":addForm::IFormSubmitListener::";
                        FormParser.parse(document)
                                .findByAction("addForm")
                                .input("name", nick)
                                .connect(actionUrl)
                                .execute(new OnDocumentListener() {
                                    @Override
                                    public void onResponse(Document document, long wicket) {
                                        ExtremeParser parser = new ExtremeParser(document);
                                        if (parser.checkFeedBack(ExtremeParser.FEEDBACK_INFO, "в друзья")) {
                                            listener.onSuccess();
                                        } else if (parser.checkFeedBack(ExtremeParser.FEEDBACK_ERROR, "Такой игрок не найден")) {
                                            listener.onError(USER_NOT_FOUND);
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
}
