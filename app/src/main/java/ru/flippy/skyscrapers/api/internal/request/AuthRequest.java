package ru.flippy.skyscrapers.api.internal.request;

import android.text.TextUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.api.SkyscrapersSDK;
import ru.flippy.skyscrapers.api.Utils;
import ru.flippy.skyscrapers.api.internal.helper.FormParser;
import ru.flippy.skyscrapers.api.internal.helper.Parser;
import ru.flippy.skyscrapers.api.internal.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.api.listeners.ActionRequestListener;
import ru.flippy.skyscrapers.api.models.Page;

public class AuthRequest extends BaseRequest {

    public static final int WRONG_AUTH = 0;
    public static final int UNSUPPORTED_SOCIAL_NETWORK = 1;
    public static final int EMPTY_INPUT = 2;

    private String nick, password;

    public AuthRequest(String nick, String password) {
        this.nick = nick;
        this.password = password;
    }

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(nick) || nick.length() == 1 || TextUtils.isEmpty(password)) {
            listener.onError(EMPTY_INPUT);
        } else {
            nick = Character.toUpperCase(nick.charAt(0)) + nick.substring(1).toLowerCase();
            RetrofitClient.getApi()
                    .loginPage()
                    .enqueue(new Callback<Page>() {
                        @Override
                        public void onResponse(Call<Page> call, Response<Page> response) {
                            Page page = response.body();
                            if (!response.isSuccessful() || page == null) {
                                listener.onError(UNKNOWN);
                            } else {
                                HashMap<String, String> postData = FormParser.parse(page.getDocument())
                                        .findByAction("loginForm")
                                        .input("login", nick)
                                        .input("password", password)
                                        .build();
                                RetrofitClient.getApi()
                                        .login(page.getWicket(), postData)
                                        .enqueue(new Callback<Page>() {
                                            @Override
                                            public void onResponse(Call<Page> call, Response<Page> response) {
                                                Page page = response.body();
                                                if (!response.isSuccessful() || page == null) {
                                                    listener.onError(UNKNOWN);
                                                } else {
                                                    Document document = page.getDocument();
                                                    Element profileLink = document.select("div.ftr>a:contains(Мой профиль)").first();
                                                    if (profileLink != null) {
                                                        long userId = Utils.getValueAfterLastSlash(profileLink.attr("href"));
                                                        SkyscrapersSDK.saveAuthData(nick, password, userId);
                                                        listener.onSuccess();
                                                    } else {
                                                        Parser parser = Parser.from(document);
                                                        if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "Неверное имя или пароль")) {
                                                            listener.onError(WRONG_AUTH);
                                                        } else {
                                                            listener.onError(UNSUPPORTED_SOCIAL_NETWORK);
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Page> call, Throwable t) {
                                                listener.onError(NETWORK);
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onFailure(Call<Page> call, Throwable t) {
                            listener.onError(NETWORK);
                        }
                    });
        }
    }
}
