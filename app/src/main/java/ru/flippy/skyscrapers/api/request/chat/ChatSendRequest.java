package ru.flippy.skyscrapers.api.request.chat;

import android.text.TextUtils;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.helper.FormParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class ChatSendRequest extends BaseRequest {

    public static final int BAN = 0;
    public static final int LEVEL_LESS = 1;
    public static final int LENGTH_LIMIT = 2;
    public static final int EMPTY_INPUT = 3;

    private String message;

    public ChatSendRequest(String message) {
        this.message = message;
    }

    public void execute(final SimpleRequestListener listener) {
        if (TextUtils.isEmpty(message)) {
            listener.onError(EMPTY_INPUT);
        } else {
            DocumentLoader.connect("http://nebo.mobi/chat").execute(new OnDocumentListener() {
                @Override
                public void onResponse(Document document, long wicket) {
                    if (FormParser.checkForm(document, "messageForm")) {
                        String actionUrl = "http://nebo.mobi/chat/wicket:interface/:" + wicket + ":chatForm:messageForm::IFormSubmitListener::";
                        FormParser.parse(document)
                                .findByAction("messageForm")
                                .input("text", message)
                                .connect(actionUrl)
                                .execute(new OnDocumentListener() {
                                    @Override
                                    public void onResponse(Document document, long wicket) {
                                        ExtremeParser parser = new ExtremeParser(document);
                                        if (parser.checkFeedBack(ExtremeParser.FEEDBACK_ERROR, "Количество символов в")) {
                                            listener.onError(LENGTH_LIMIT);
                                        } else {
                                            listener.onSuccess();
                                        }
                                    }

                                    @Override
                                    public void onError() {
                                        listener.onError(NETWORK);
                                    }
                                });
                    } else if (!document.select("div.nfl>div:contains(В чат можно писать с 10 уровня)").isEmpty()) {
                        listener.onError(LEVEL_LESS);
                    } else if (!document.select("div[class=ntfy notify]>div:contains(Вы временно не можете писать в чате, т.к. нарушали правила общения)").isEmpty()) {
                        listener.onError(BAN);
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
}