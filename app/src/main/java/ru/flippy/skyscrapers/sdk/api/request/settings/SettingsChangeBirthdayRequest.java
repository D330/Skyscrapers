package ru.flippy.skyscrapers.sdk.api.request.settings;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class SettingsChangeBirthdayRequest {

    private int birthdayDay, birthdayMonth, birthdayYear;

    public SettingsChangeBirthdayRequest(int birthdayDay, int birthdayMonth, int birthdayYear) {
        this.birthdayDay = birthdayDay;
        this.birthdayMonth = birthdayMonth;
        this.birthdayYear = birthdayYear;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (!doc.checkForm("birthdayForm")) {
                            listener.onError(Error.ALREADY);
                        } else {
                            HashMap<String, String> postData = FormParser.parse(doc)
                                    .findByAction("birthdayForm")
                                    .input("birthdayDay", birthdayDay)
                                    .input("birthdayMonth", birthdayMonth)
                                    .input("birthdayYear", birthdayYear)
                                    .build();
                            RetrofitClient.getApi().settingsChangeBirthday(doc.wicket(), postData)
                                    .error(listener)
                                    .success(new SourceCallback() {
                                        @Override
                                        public void onResponse(Source doc) {
                                            listener.onSuccess();
                                        }
                                    });
                        }
                    }
                });
    }
}
