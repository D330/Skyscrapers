package ru.flippy.skyscrapers.api.request.settings;

import android.text.TextUtils;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.SkyscrapersAPI;
import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.helper.FormParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class SettingsChangeNickRequest extends BaseRequest {

    public static final int NOT_ENOUGH_DOLLARS = 0;
    public static final int NICK_BUSY = 1;
    public static final int NICKS_NOT_EQUALS = 2;
    public static final int INCORRECT_FORMAT = 3;
    public static final int EMPTY_INPUT = 4;

    private String newNick, newNickConfirm;

    public SettingsChangeNickRequest(String newNick, String newNickConfirm) {
        this.newNick = newNick;
        this.newNickConfirm = newNickConfirm;
    }

    public void execute(final SimpleRequestListener listener) {
        if (TextUtils.isEmpty(newNick) || TextUtils.isEmpty(newNickConfirm)) {
            listener.onError(EMPTY_INPUT);
        } else if (!newNick.equalsIgnoreCase(newNickConfirm)) {
            listener.onError(NICKS_NOT_EQUALS);
        } else {
            DocumentLoader.connect("http://nebo.mobi/changelogin").execute(new OnDocumentListener() {
                @Override
                public void onResponse(Document document, long wicket) {
                    String actionUrl = "http://nebo.mobi/changelogin/?wicket:interface=:" + wicket + ":changeLoginForm::IFormSubmitListener::";
                    FormParser.parse(document)
                            .findByAction("changeLoginForm")
                            .input("newLogin1", newNick)
                            .input("newLogin2", newNickConfirm)
                            .connect(actionUrl)
                            .execute(new OnDocumentListener() {
                                @Override
                                public void onResponse(Document document, long wicket) {
                                    ExtremeParser parser = new ExtremeParser(document);
                                    if (parser.checkFeedBack(ExtremeParser.FEEDBACK_INFO, "Имя изменено")) {
                                        listener.onSuccess();
                                        SkyscrapersAPI.updateUserNick(newNick);
                                    } else if (parser.checkFeedBack(ExtremeParser.FEEDBACK_ERROR, "не хватает")) {
                                        listener.onError(NOT_ENOUGH_DOLLARS);
                                    } else if (parser.checkFeedBack(ExtremeParser.FEEDBACK_ERROR, "уже занято")) {
                                        listener.onError(NICK_BUSY);
                                    } else if (parser.checkFeedBack(ExtremeParser.FEEDBACK_ERROR, "только буквы")) {
                                        listener.onError(INCORRECT_FORMAT);
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

                @Override
                public void onError() {
                    listener.onError(NETWORK);
                }
            });
        }
    }
}
