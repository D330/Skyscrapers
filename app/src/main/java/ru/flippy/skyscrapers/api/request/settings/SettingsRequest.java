package ru.flippy.skyscrapers.api.request.settings;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.Utils;
import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.FormParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.OnRequestListener;
import ru.flippy.skyscrapers.api.model.Date;
import ru.flippy.skyscrapers.api.model.SettingsData;
import ru.flippy.skyscrapers.api.model.User;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class SettingsRequest extends BaseRequest {

    public void execute(final OnRequestListener<SettingsData> listener) {
        DocumentLoader.connect("http://nebo.mobi/settings").execute(new OnDocumentListener() {
            @Override
            public void onResponse(Document document, long wicket) {
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

            @Override
            public void onError() {
                listener.onError(NETWORK);
            }
        });
    }
}
