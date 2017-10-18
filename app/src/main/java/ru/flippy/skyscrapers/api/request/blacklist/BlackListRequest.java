package ru.flippy.skyscrapers.api.request.blacklist;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.api.Utils;
import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.OnPageRequestListener;
import ru.flippy.skyscrapers.api.model.BlackListUser;
import ru.flippy.skyscrapers.api.model.User;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class BlackListRequest extends BaseRequest {

    public static final int PAGE_NOT_FOUND = 0;

    private int page;

    public BlackListRequest(int page) {
        this.page = page;
    }

    public void execute(final OnPageRequestListener<List<BlackListUser>> listener) {
        DocumentLoader.connect("http://nebo.mobi/black/list").execute(new OnDocumentListener() {
            @Override
            public void onResponse(Document document, long wicket) {
                ExtremeParser parser = new ExtremeParser(document);
                if (parser.getPageCount(ExtremeParser.TYPE_WICKET) == 1) {
                    listener.onResponse(parseBlackList(document), 1);
                } else {
                    String actionUrl = "http://nebo.mobi/black/list/?wicket:interface=:" + wicket + ":paginator:container:last::ILinkListener::";
                    DocumentLoader.connect(actionUrl).execute(new OnDocumentListener() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            ExtremeParser parser = new ExtremeParser(document);
                            final int pageCount = parser.getPageCount(ExtremeParser.TYPE_WICKET);
                            String actionUrl = "http://nebo.mobi/black/list/?wicket:interface=:" + wicket + ":paginator:container:navigation:" + (page - 1) + ":pageLink::ILinkListener::";
                            DocumentLoader.connect(actionUrl).execute(new OnDocumentListener() {
                                @Override
                                public void onResponse(Document document, long wicket) {
                                    ExtremeParser parser = new ExtremeParser(document);
                                    if (parser.checkPageError()) {
                                        listener.onError(PAGE_NOT_FOUND);
                                    } else {
                                        listener.onResponse(parseBlackList(document), pageCount);
                                    }
                                }

                                @Override
                                public void onError() {
                                    listener.onError(NETWORK);
                                }
                            });
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
