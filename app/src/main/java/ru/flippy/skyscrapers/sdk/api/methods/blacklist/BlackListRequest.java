package ru.flippy.skyscrapers.sdk.api.methods.blacklist;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.BlackListUser;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class BlackListRequest extends BaseRequest {

    public static final int PAGE_NOT_FOUND = 0;

    private int pageNumber;

    public BlackListRequest(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void execute(final PaginationRequestListener<List<BlackListUser>> listener) {
        RetrofitClient.getApi().blacklist().enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Document document = page.getDocument();
                    long wicket = page.getWicket();
                    Parser parser = Parser.from(document);
                    if (parser.getPageCount(Parser.TYPE_WICKET) == 1) {
                        listener.onResponse(parseBlackList(document), 1);
                    } else {
                        RetrofitClient.getApi().blacklistLastPage(wicket).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    Document document = page.getDocument();
                                    long wicket = page.getWicket();
                                    final int pageCount = Parser.from(document).getPageCount(Parser.TYPE_WICKET);
                                    RetrofitClient.getApi().blacklistPagination(wicket, pageNumber).enqueue(new Callback<Page>() {
                                        @Override
                                        public void onResponse(Call<Page> call, Response<Page> response) {
                                            Page page = response.body();
                                            if (!response.isSuccessful() || page == null) {
                                                listener.onError(UNKNOWN);
                                            } else {
                                                Document document = page.getDocument();
                                                if (Parser.from(document).checkPageError()) {
                                                    listener.onError(PAGE_NOT_FOUND);
                                                } else {
                                                    listener.onResponse(parseBlackList(document), pageCount);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Page> call, Throwable t) {
                                            listener.onError(NETWORK);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<Page> call, Throwable t) {
                                listener.onError(NETWORK);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
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
