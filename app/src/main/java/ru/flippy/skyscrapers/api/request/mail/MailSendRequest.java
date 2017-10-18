package ru.flippy.skyscrapers.api.request.mail;

import android.text.TextUtils;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.helper.FormParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class MailSendRequest extends BaseRequest {

    public static final int USER_NOT_FOUND = 0;
    public static final int NOT_ENOUGH_COINS = 1;
    public static final int EMPTY_INPUT = 2;

    private long userId;
    private String message;

    public MailSendRequest(long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public void execute(final SimpleRequestListener listener) {
        if (TextUtils.isEmpty(message)) {
            listener.onError(EMPTY_INPUT);
        } else {
            DocumentLoader.connect("http://nebo.mobi/mail/send/id/" + userId).execute(new OnDocumentListener() {
                @Override
                public void onResponse(Document document, long wicket) {
                    ExtremeParser parser = new ExtremeParser(document);
                    if (parser.checkPageError()) {
                        listener.onError(USER_NOT_FOUND);
                    } else {
                        String actionUrl = "http://nebo.mobi/mail/send/id/" + userId + "/wicket:interface/:" + wicket + ":sendMessageForm:messageForm::IFormSubmitListener::";
                        FormParser.parse(document)
                                .findByAction("sendMessageForm")
                                .input("text", message)
                                .connect(actionUrl)
                                .execute(new OnDocumentListener() {
                                    @Override
                                    public void onResponse(Document document, long wicket) {
                                        ExtremeParser parser = new ExtremeParser(document);
                                        if (parser.checkFeedBack(ExtremeParser.FEEDBACK_ERROR, "Не хватает монет для отправки сообщения")) {
                                            listener.onError(NOT_ENOUGH_COINS);
                                        } else {
                                            listener.onSuccess();
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
