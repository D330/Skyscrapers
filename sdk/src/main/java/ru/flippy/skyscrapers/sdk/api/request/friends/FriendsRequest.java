package ru.flippy.skyscrapers.sdk.api.request.friends;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class FriendsRequest {

    private int page;

    public FriendsRequest(int page) {
        this.page = page;
    }

    public void execute(final PaginationRequestListener<List<User>> listener) {
        RetrofitClient.getApi().friends()
                .error(listener)
                .success(friendsDoc -> RetrofitClient.getApi().friendsPagination(friendsDoc.wicket(), page)
                        .error(listener)
                        .success(needPageDoc -> listener.onResponse(parseFriends(needPageDoc), needPageDoc.pagination())));
    }

    private List<User> parseFriends(Source doc) {
        List<User> friends = new ArrayList<>();
        for (Element userImg : doc.select("div.m5>div>span>img[src*=/user/]")) {
            Element userSpan = userImg.parent();
            Element userA = userSpan.select("span.user>a").first();
            long id = Utils.getValueAfterLastSlash(userA.attr("href"));
            String nick = userA.text();
            int level = Integer.parseInt(userSpan.select("img[alt=у]").first().nextElementSibling().text());
            int sex = userImg.attr("alt").equals("м") ? User.sex.MAN : User.sex.WOMAN;
            int online = User.online.ONLINE;
            if (userSpan.select("span.minor").first().text().equals("*")) {
                online = User.online.ONLINE_STAR;
            } else if (userImg.attr("src").contains("off")) {
                online = User.online.OFFLINE;
            }
            User friend = new User();
            friend.setId(id);
            friend.setNick(nick);
            friend.setLevel(level);
            friend.setSex(sex);
            friend.setOnline(online);
            friends.add(friend);
        }
        return friends;
    }
}
