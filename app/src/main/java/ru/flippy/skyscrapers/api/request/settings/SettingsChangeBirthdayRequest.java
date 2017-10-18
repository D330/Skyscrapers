package ru.flippy.skyscrapers.api.request.settings;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.FormParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class SettingsChangeBirthdayRequest extends BaseRequest {

    public static final int BIRTHDAY_SPECIFIED = 0;

    private int birthdayDay, birthdayMonth, birthdayYear;

    public SettingsChangeBirthdayRequest(int birthdayDay, int birthdayMonth, int birthdayYear) {
        this.birthdayDay = birthdayDay;
        this.birthdayMonth = birthdayMonth;
        this.birthdayYear = birthdayYear;
    }

    public void execute(final SimpleRequestListener listener) {
        DocumentLoader.connect("http://nebo.mobi/settings").execute(new OnDocumentListener() {
            @Override
            public void onResponse(Document document, long wicket) {
                if (!FormParser.checkForm(document, "birthdayForm")) {
                    listener.onError(BIRTHDAY_SPECIFIED);
                } else {
                    String actionUrl = "http://nebo.mobi/settings/?wicket:interface=:" + wicket + ":birthdayForm::IFormSubmitListener::";
                    FormParser.parse(document)
                            .findByAction("birthdayForm")
                            .input("birthdayDay", String.valueOf(birthdayDay))
                            .input("birthdayMonth", String.valueOf(birthdayMonth))
                            .input("birthdayYear", String.valueOf(birthdayYear))
                            .connect(actionUrl)
                            .execute(new OnDocumentListener() {
                                @Override
                                public void onResponse(Document document, long wicket) {
                                    listener.onSuccess();
                                }

                                @Override
                                public void onError() {
                                    listener.onError(NETWORK);
                                }
                            });
                }
            }

            @Override
            public void onError() {
                listener.onError(NETWORK);
            }
        });
    }
}
