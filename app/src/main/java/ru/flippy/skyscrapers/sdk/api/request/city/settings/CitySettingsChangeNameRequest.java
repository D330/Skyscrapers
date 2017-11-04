package ru.flippy.skyscrapers.sdk.api.request.city.settings;

import android.text.TextUtils;

import org.jsoup.nodes.Element;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class CitySettingsChangeNameRequest extends BaseRequest {

    public static final int NO_CITY = 0;
    public static final int ACCESS_DENIED = 1;
    public static final int NAME_BUSY = 2;
    public static final int NOT_ENOUGH_DOLLARS = 3;
    public static final int NAMES_NOT_EQUALS = 4;
    public static final int EMPTY_INPUT = 5;

    private String newName, newNameConfirm;

    public CitySettingsChangeNameRequest(String newName, String newNameConfirm) {
        this.newName = newName;
        this.newNameConfirm = newNameConfirm;
    }

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newNameConfirm)) {
            listener.onError(EMPTY_INPUT);
        } else if (!newName.equalsIgnoreCase(newNameConfirm)) {
            listener.onError(NAMES_NOT_EQUALS);
        } else {
            RetrofitClient.getApi().city().enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    Page page = response.body();
                    if (!response.isSuccessful() || page == null) {
                        listener.onError(UNKNOWN);
                    } else {
                        Parser parser = Parser.from(page.getDocument());
                        if (parser.getLink("/storage/0/") == null) {
                            listener.onError(NO_CITY);
                        } else {
                            Element settingsLink = parser.getLink("/about/0/");
                            if (settingsLink == null) {
                                listener.onError(ACCESS_DENIED);
                            } else {
                                final long cityId = Utils.getValueAfterLastSlash(settingsLink.attr("href"));
                                RetrofitClient.getApi().citySettings(cityId).enqueue(new Callback<Page>() {
                                    @Override
                                    public void onResponse(Call<Page> call, Response<Page> response) {
                                        Page page = response.body();
                                        if (!response.isSuccessful() || page == null) {
                                            listener.onError(UNKNOWN);
                                        } else {
                                            HashMap<String, String> postData = FormParser.parse(page.getDocument())
                                                    .findByAction("guildNameForm")
                                                    .input("newGuildName1", newName)
                                                    .input("newGuildName2", newNameConfirm)
                                                    .build();
                                            RetrofitClient.getApi().citySettingsChangeName(page.getWicket(), cityId, postData).enqueue(new Callback<Page>() {
                                                @Override
                                                public void onResponse(Call<Page> call, Response<Page> response) {
                                                    Page page = response.body();
                                                    if (!response.isSuccessful() || page == null) {
                                                        listener.onError(UNKNOWN);
                                                    } else {
                                                        Parser parser = Parser.from(page.getDocument());
                                                        if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "не хватает")) {
                                                            listener.onError(NOT_ENOUGH_DOLLARS);
                                                        } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "")) {
                                                            listener.onError(NAME_BUSY);
                                                        } else {
                                                            listener.onSuccess();
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
                }

                @Override
                public void onFailure(Call<Page> call, Throwable t) {
                    listener.onError(NETWORK);
                }
            });
        }
    }
}
