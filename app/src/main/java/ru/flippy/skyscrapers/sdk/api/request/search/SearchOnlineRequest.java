package ru.flippy.skyscrapers.sdk.api.request.search;


import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.PaginationItem;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.api.retrofit.SourceCall;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class SearchOnlineRequest {

    private PaginationItem paginationItem;

    public SearchOnlineRequest(PaginationItem paginationItem) {
        this.paginationItem = paginationItem;
    }

    public void execute(final PaginationRequestListener<List<User>> listener) {
        SourceCall call;
        if (paginationItem == null) {
            call = RetrofitClient.getApi().searchOnline();
        } else {
            call = RetrofitClient.getApi().searchUserPagination(paginationItem.getWicket(), paginationItem.getIndex());
        }
        call.error(listener).success(
                onlineDoc -> listener.onResponse(parseOnlineUsers(onlineDoc), onlineDoc.pagination()));
    }

    private List<User> parseOnlineUsers(Source onlineDoc) {
        List<User> users = new ArrayList<>();
        for (Element userElement : onlineDoc.select("div.m5>div:has(img[src*=/user/])>span")) {
            User user = new User();
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
