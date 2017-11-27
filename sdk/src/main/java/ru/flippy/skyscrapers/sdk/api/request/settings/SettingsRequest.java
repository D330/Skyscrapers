package ru.flippy.skyscrapers.sdk.api.request.settings;

import ru.flippy.skyscrapers.sdk.api.model.Settings;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.model.time.Date;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class SettingsRequest {

    public void execute(final RequestListener<Settings> listener) {
        RetrofitClient.getApi().settings()
                .error(listener)
                .success(settingDoc -> {
                    String about = settingDoc.select("form[action*=about]>textarea").first().text();
                    String nick = settingDoc.select("span.white:contains(Имя:)").first().nextElementSibling().text();
                    int sex = settingDoc.select("span.white:contains(Пол:)").first().nextElementSibling().text().equals("мужской") ? User.sex.MAN : User.sex.WOMAN;
                    Date birthday = null;
                    if (!settingDoc.hasForm("birthdayForm")) {
                        String[] birthdayArray = settingDoc.select("span.white:contains(День рождения:)").first().nextElementSibling().text().split(" ");
                        birthday = new Date();
                        birthday.setDay(Integer.parseInt(birthdayArray[0]));
                        birthday.setMonth(Utils.getMonthIndex(birthdayArray[1]));
                        birthday.setYear(Integer.parseInt(birthdayArray[2]));
                    }
                    boolean guildInvite = settingDoc.select("span.white:contains(На странице \"Без города\":)").first().nextElementSibling().text().equals("показывать");
                    Settings settingsData = new Settings();
                    settingsData.setAbout(about);
                    settingsData.setNick(nick);
                    settingsData.setSex(sex);
                    settingsData.setBirthday(birthday);
                    settingsData.setGuildInvite(guildInvite);
                    listener.onResponse(settingsData);
                });
    }
}
