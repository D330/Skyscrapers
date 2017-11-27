package ru.flippy.skyscrapers.sdk.api.request.search;


import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.NoCityUser;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class SearchNoCityRequest {

    public void execute(final RequestListener<List<NoCityUser>> listener) {
        RetrofitClient.getApi().searchNoCity()
                .error(listener)
                .success(noCityDoc -> {
                    listener.onResponse(parseNoCityUsers(noCityDoc));
                });
    }

    private List<NoCityUser> parseNoCityUsers(Source noCityDoc) {
        List<NoCityUser> users = new ArrayList<>();
        for (Element noCityUserElement : noCityDoc.select("div.m5>div:has(img[src*=/user/])")) {
            Element userElement = noCityUserElement.select("span").first();
            NoCityUser user = new NoCityUser();
            user.setFreeForInvitation(!noCityUserElement.select("span>span.amount:contains(+)").isEmpty());
            user.setDaysInGame(Integer.parseInt(noCityUserElement.select("span.small.minor.nshd>span").text()));
            user.setId(Utils.getValueAfterLastSlash(userElement.select("span.user>a").first().attr("href")));
            user.setNick(userElement.select("span.user>a>span").first().text());
            user.setLevel(Integer.parseInt(userElement.select("span").last().text()));
            Element userImage = userElement.select("img[src*=/user/]").first();
            user.setSex(userImage.attr("alt").equals("м") ? User.sex.MAN : User.sex.WOMAN);
            if (userElement.select("span.minor").last().text().equals("*")) {
                user.setOnline(User.online.ONLINE_STAR);
            } else if (userImage.attr("src").contains("off")) {
                user.setOnline(User.online.OFFLINE);
            } else {
                user.setOnline(User.online.ONLINE);
            }
            users.add(user);
        }
        return users;
    }

}
