package ru.flippy.skyscrapers.api.request;

import android.text.TextUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ru.flippy.skyscrapers.api.SkyscrapersAPI;
import ru.flippy.skyscrapers.api.Utils;
import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.helper.FormParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;

public class AuthRequest extends BaseRequest {

    public static final int WRONG_AUTH = 0;
    public static final int UNSUPPORTED_SOCIAL_NETWORK = 1;
    public static final int EMPTY_INPUT = 2;

    private String nick, password;

    public AuthRequest(String nick, String password) {
        this.nick = nick;
        this.password = password;
    }

    public void execute(final SimpleRequestListener listener) {
        if (TextUtils.isEmpty(nick) || nick.length() == 1 || TextUtils.isEmpty(password)) {
            listener.onError(EMPTY_INPUT);
        } else {
            nick = Character.toUpperCase(nick.charAt(0)) + nick.substring(1).toLowerCase();
            DocumentLoader.connect("http://nebo.mobi/login").execute(new OnDocumentListener() {
                @Override
                public void onResponse(Document document, long wicket) {
                    String actionUrl = "http://nebo.mobi/login/?wicket:interface=:" + wicket + ":loginForm:loginForm::IFormSubmitListener::";
                    FormParser.parse(document)
                            .findByAction("loginForm")
                            .input("login", nick)
                            .input("password", password)
                            .connect(actionUrl)
                            .execute(new OnDocumentListener() {
                                @Override
                                public void onResponse(Document document, long wicket) {
                                    Element profileLink = document.select("div.ftr>a:contains(Мой профиль)").first();
                                    if (profileLink != null) {
                                        long userId = Utils.getValueAfterLastSlash(profileLink.attr("href"));
                                        SkyscrapersAPI.saveAuthData(nick, password, userId);
                                        listener.onSuccess();
                                    } else {
                                        ExtremeParser parser = new ExtremeParser(document);
                                        if (parser.checkFeedBack(ExtremeParser.FEEDBACK_ERROR, "Неверное имя или пароль")) {
                                            listener.onError(WRONG_AUTH);
                                        } else {
                                            listener.onError(UNSUPPORTED_SOCIAL_NETWORK);
                                        }
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
