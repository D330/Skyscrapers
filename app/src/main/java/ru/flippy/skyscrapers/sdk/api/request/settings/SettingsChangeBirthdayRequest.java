package ru.flippy.skyscrapers.sdk.api.request.settings;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SettingsChangeBirthdayRequest {

    private int birthdayDay, birthdayMonth, birthdayYear;

    public SettingsChangeBirthdayRequest(int birthdayDay, int birthdayMonth, int birthdayYear) {
        this.birthdayDay = birthdayDay;
        this.birthdayMonth = birthdayMonth;
        this.birthdayYear = birthdayYear;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().settings().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                if (!FormParser.checkForm(document, "birthdayForm")) {
                    listener.onError(Error.ALREADY);
                } else {
                    HashMap<String, String> postData = FormParser.parse(document)
                            .findByAction("birthdayForm")
                            .input("birthdayDay", birthdayDay)
                            .input("birthdayMonth", birthdayMonth)
                            .input("birthdayYear", birthdayYear)
                            .build();
                    RetrofitClient.getApi().settingsChangeBirthday(wicket, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            listener.onSuccess();
                        }
                    });
                }
            }
        });
    }
}
