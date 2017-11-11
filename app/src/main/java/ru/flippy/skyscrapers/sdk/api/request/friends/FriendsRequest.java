package ru.flippy.skyscrapers.sdk.api.request.friends;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Friend;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class FriendsRequest {

    private int page;

    public FriendsRequest(int page) {
        this.page = page;
    }

    public void execute(final PaginationRequestListener<List<Friend>> listener) {
        RetrofitClient.getApi().friends().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                final int pageCount = Parser.from(document).getPageCount(Parser.TYPE_NORMAL);
                if (pageCount == 1) {
                    listener.onResponse(parseFriends(document), 1);
                } else {
                    RetrofitClient.getApi().friendsPagination(wicket, page).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            listener.onResponse(parseFriends(document), pageCount);
                        }
                    });
                }
            }
        });
    }

    private List<Friend> parseFriends(Document document) {
        List<Friend> friends = new ArrayList<>();
        for (Element userImg : document.select("div.m5>div>span>img[src*=/user/]")) {
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
            Friend friend = new Friend();
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
