package ru.flippy.skyscrapers.sdk.api.request.blacklist;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class BlackListRequest {

    private int page;

    public BlackListRequest(int page) {
        this.page = page;
    }

    public void execute(final PaginationRequestListener<List<User>> listener) {
        RetrofitClient.getApi().blacklist()
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        RetrofitClient.getApi().blacklistLastPage(doc.wicket())
                                .error(listener)
                                .success(new SourceCallback() {
                                    @Override
                                    public void onResponse(Source doc) {
                                        RetrofitClient.getApi().blacklistPagination(doc.wicket(), page)
                                                .error(listener)
                                                .success(new SourceCallback() {
                                                    @Override
                                                    public void onResponse(Source doc) {
                                                        listener.onResponse(parseBlackList(doc), doc.pagination());
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    private List<User> parseBlackList(Source doc) {
        List<User> blackListUsers = new ArrayList<>();
        for (Element userImg : doc.select("div.m5>div>span>img[src*=/user/]")) {
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
            User blackListUser = new User();
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
