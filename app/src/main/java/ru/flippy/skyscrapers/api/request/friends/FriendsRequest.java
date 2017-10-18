package ru.flippy.skyscrapers.api.request.friends;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.api.Utils;
import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.OnPageRequestListener;
import ru.flippy.skyscrapers.api.model.Friend;
import ru.flippy.skyscrapers.api.model.User;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class FriendsRequest extends BaseRequest {

    private int page;

    public FriendsRequest(int page) {
        this.page = page;
    }

    public void execute(final OnPageRequestListener<List<Friend>> listener) {
        DocumentLoader.connect("http://nebo.mobi/friends").execute(new OnDocumentListener() {
            @Override
            public void onResponse(Document document, long wicket) {
                ExtremeParser paginatorParser = new ExtremeParser(document);
                final int pageCount = paginatorParser.getPageCount(ExtremeParser.TYPE_NORMAL);
                if (pageCount == 1) {
                    listener.onResponse(parseFriends(document), 1);
                } else {
                    String actionUrl = "http://nebo.mobi/friends/?wicket:interface=:" + wicket + ":paginator:container:navigation:" + (page - 1) + ":pageLink::ILinkListener::";
                    DocumentLoader.connect(actionUrl).execute(new OnDocumentListener() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            listener.onResponse(parseFriends(document), pageCount);
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
