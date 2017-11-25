package ru.flippy.skyscrapers.sdk.api.request.search;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.PaginationItem;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class SearchUserRequest {

    private String nick;
    private PaginationItem paginationItem;

    public SearchUserRequest(String nick) {
        this.nick = nick;
    }

    public SearchUserRequest(PaginationItem paginationItem) {
        this.paginationItem = paginationItem;
    }

    public void execute(final PaginationRequestListener<List<User>> listener) {
        if (paginationItem == null) {
            RetrofitClient.getApi().searchUserPage()
                    .error(listener)
                    .success(searchDoc -> {
                        HashMap<String, String> postData = FormParser.parse(searchDoc)
                                .findByAction("searchForm")
                                .input("login", nick)
                                .build();
                        RetrofitClient.getApi().searchUser(searchDoc.wicket(), postData)
                                .error(listener)
                                .success(resultDoc -> listener.onResponse(parseResult(resultDoc), resultDoc.pagination()));
                    });
        } else {
            RetrofitClient.getApi()
                    .searchUserPagination(paginationItem.getWicket(), paginationItem.getIndex())
                    .error(listener)
                    .success(resultDoc -> listener.onResponse(parseResult(resultDoc), resultDoc.pagination()));
        }
    }

    private List<User> parseResult(Source resultDoc) {
        List<User> users = new ArrayList<>();
        for (Element userElement : resultDoc.select("div.m5>div>div:has(img[src*=/user/])>span")) {
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
