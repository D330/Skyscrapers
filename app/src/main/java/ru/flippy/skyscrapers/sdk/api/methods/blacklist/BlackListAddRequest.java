package ru.flippy.skyscrapers.sdk.api.methods.blacklist;

import android.text.TextUtils;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class BlackListAddRequest extends BaseRequest {

    public static final int BLACKLIST_LIMIT = 0;
    public static final int USER_NOT_FOUND = 1;
    public static final int EMPTY_INPUT = 2;

    private String nick;

    public BlackListAddRequest(String nick) {
        this.nick = nick;
    }

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(nick)) {
            listener.onError(EMPTY_INPUT);
        } else {
            RetrofitClient.getApi().blacklist().enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    Page page = response.body();
                    if (!response.isSuccessful() || page == null) {
                        listener.onError(UNKNOWN);
                    } else {
                        Document document = page.getDocument();
                        if (!FormParser.checkForm(document, "addForm")) {
                            listener.onError(BLACKLIST_LIMIT);
                        } else {
                            HashMap<String, String> postData = FormParser.parse(document)
                                    .findByAction("addForm")
                                    .input("name", nick)
                                    .build();
                            RetrofitClient.getApi().blacklistAdd(page.getWicket(), postData).enqueue(new Callback<Page>() {
                                @Override
                                public void onResponse(Call<Page> call, Response<Page> response) {
                                    Page page = response.body();
                                    if (!response.isSuccessful() || page == null) {
                                        listener.onError(UNKNOWN);
                                    } else {
                                        Parser parser = Parser.from(page.getDocument());
                                        if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "в черный список")) {
                                            listener.onSuccess();
                                        } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "Такой игрок не найден")) {
                                            listener.onError(USER_NOT_FOUND);
                                        } else {
                                            listener.onError(UNKNOWN);
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
                }

                @Override
                public void onFailure(Call<Page> call, Throwable t) {
                    listener.onError(NETWORK);
                }
            });
        }
    }
}
