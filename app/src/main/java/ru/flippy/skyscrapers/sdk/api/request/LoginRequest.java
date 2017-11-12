package ru.flippy.skyscrapers.sdk.api.request;

import org.jsoup.nodes.Element;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;
import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class LoginRequest {

    private String nick, password;

    public LoginRequest(String nick, String password) {
        this.nick = Character.toUpperCase(nick.charAt(0)) + nick.substring(1).toLowerCase();
        this.password = password;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().loginPage()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        HashMap<String, String> postData = FormParser.parse(doc)
                                .findByAction("loginForm")
                                .input("login", nick)
                                .input("password", password)
                                .build();
                        RetrofitClient.getApi().login(doc.wicket(), postData)
                                .error(listener)
                                .success(new SourceCallback() {
                                    @Override
                                    public void onResponse(Source doc) {
                                        Element profileLink = doc.select("div.ftr>a:contains(Мой профиль)").first();
                                        if (profileLink != null) {
                                            long userId = Utils.getValueAfterLastSlash(profileLink.attr("href"));
                                            SkyscrapersSDK.saveAuthData(nick, password, userId);
                                            listener.onSuccess();
                                        } else if (doc.checkFeedBack(Feedback.Type.ERROR, "Неверное имя или пароль")) {
                                            listener.onError(Error.WRONG_DATA);
                                        } else {
                                            listener.onError(Error.UNSUPPORTED_SOCIAL_NETWORK);
                                        }
                                    }
                                });
                    }
                });
    }
}