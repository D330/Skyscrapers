package ru.flippy.skyscrapers.sdk.api.request.blacklist;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.BlackListUser;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class BlackListRequest {

    private int page;

    public BlackListRequest(int page) {
        this.page = page;
    }

    public void execute(final PaginationRequestListener<List<BlackListUser>> listener) {
        RetrofitClient.getApi().blacklist().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Parser parser = Parser.from(document);
                if (parser.getPageCount(Parser.TYPE_WICKET) == 1) {
                    listener.onResponse(parseBlackList(document), 1);
                } else {
                    RetrofitClient.getApi().blacklistLastPage(wicket).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            final int pageCount = Parser.from(document).getPageCount(Parser.TYPE_WICKET);
                            RetrofitClient.getApi().blacklistPagination(wicket, pageCount).setErrorPoint(listener).enqueue(new DocumentCallback() {
                                @Override
                                public void onResponse(Document document, long wicket) {
                                    listener.onResponse(parseBlackList(document), pageCount);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private List<BlackListUser> parseBlackList(Document document) {
        List<BlackListUser> blackListUsers = new ArrayList<>();
        for (Element userImg : document.select("div.m5>div>span>img[src*=/user/]")) {
            Element userSpan = userImg.parent();
            Element userA = userSpan.select("span.user>a").first();
            long id = Utils.getValueAfterLastSlash(userA.attr("href"));
            String nick = userA.text();
            int level = 0;
            for (String betweenTire : userImg.attr("src").split("\\.")[0].split("-")) {
                if (betweenTire.endsWith("0")) {
                    level = Integer.parseInt(betweenTire);
                }
            }
            int sex = userImg.attr("alt").equals("м") ? User.sex.MAN : User.sex.WOMAN;
            int online = User.online.ONLINE;
            if (userSpan.select("span.minor").first().text().equals("*")) {
                online = User.online.ONLINE_STAR;
            } else if (userImg.attr("src").contains("off")) {
                online = User.online.OFFLINE;
            }
            BlackListUser blackListUser = new BlackListUser();
            blackListUser.setId(id);
            blackListUser.setNick(nick);
            blackListUser.setLevel(level);
            blackListUser.setSex(sex);
            blackListUser.setOnline(online);
            blackListUsers.add(blackListUser);
        }
        return blackListUsers;
    }
}
