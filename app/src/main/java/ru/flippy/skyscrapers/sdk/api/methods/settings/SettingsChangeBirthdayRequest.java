package ru.flippy.skyscrapers.sdk.api.methods.settings;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SettingsChangeBirthdayRequest extends BaseRequest {

    public static final int BIRTHDAY_SPECIFIED = 0;

    private int birthdayDay, birthdayMonth, birthdayYear;

    public SettingsChangeBirthdayRequest(int birthdayDay, int birthdayMonth, int birthdayYear) {
        this.birthdayDay = birthdayDay;
        this.birthdayMonth = birthdayMonth;
        this.birthdayYear = birthdayYear;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings().enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else if (!FormParser.checkForm(page.getDocument(), "birthdayForm")) {
                    listener.onError(BIRTHDAY_SPECIFIED);
                } else {
                    HashMap<String, String> postData = FormParser.parse(page.getDocument())
                            .findByAction("birthdayForm")
                            .input("birthdayDay", birthdayDay)
                            .input("birthdayMonth", birthdayMonth)
                            .input("birthdayYear", birthdayYear)
                            .build();
                    RetrofitClient.getApi().settingsChangeBirthday(page.getWicket(), postData).enqueue(new Callback<Page>() {
                        @Override
                        public void onResponse(Call<Page> call, Response<Page> response) {
                            Page page = response.body();
                            if (!response.isSuccessful() || page == null) {
                                listener.onError(UNKNOWN);
                            } else {
                                listener.onSuccess();
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
