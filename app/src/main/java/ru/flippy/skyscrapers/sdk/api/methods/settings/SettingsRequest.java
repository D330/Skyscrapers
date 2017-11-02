package ru.flippy.skyscrapers.sdk.api.methods.settings;

import org.jsoup.nodes.Document;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Date;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.model.SettingsData;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class SettingsRequest extends BaseRequest {

    public void execute(final RequestListener<SettingsData> listener) {
        RetrofitClient.getApi().settings().enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Document document = page.getDocument();
                    String about = document.select("form[action*=about]>textarea").first().text();
                    String nick = document.select("span.white:contains(Имя:)").first().nextElementSibling().text();
                    int sex = document.select("span.white:contains(Пол:)").first().nextElementSibling().text().equals("мужской") ? User.sex.MAN : User.sex.WOMAN;
                    Date birthday = null;
                    if (!FormParser.checkForm(document, "birthdayForm")) {
                        String[] birthdayArray = document.select("span.white:contains(День рождения:)").first().nextElementSibling().text().split(" ");
                        birthday = new Date();
                        birthday.setDay(Integer.parseInt(birthdayArray[0]));
                        birthday.setMonth(Utils.getMonthIndex(birthdayArray[1]));
                        birthday.setYear(Integer.parseInt(birthdayArray[2]));
                    }
                    boolean guildInvite = document.select("span.white:contains(На странице \"Без города\":)").first().nextElementSibling().text().equals("показывать");
                    SettingsData settingsData = new SettingsData();
                    settingsData.setAbout(about);
                    settingsData.setNick(nick);
                    settingsData.setSex(sex);
                    settingsData.setBirthday(birthday);
                    settingsData.setGuildInvite(guildInvite);
                    listener.onResponse(settingsData);
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                listener.onError(NETWORK);
            }
        });
    }
}
