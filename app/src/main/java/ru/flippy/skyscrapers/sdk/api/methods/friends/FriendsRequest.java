package ru.flippy.skyscrapers.sdk.api.methods.friends;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Friend;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class FriendsRequest extends BaseRequest {

    private int pageNumber;

    public FriendsRequest(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void execute(final PaginationRequestListener<List<Friend>> listener) {
        RetrofitClient.getApi().friends().enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    final int pageCount = Parser.from(page.getDocument()).getPageCount(Parser.TYPE_NORMAL);
                    if (pageCount == 1) {
                        listener.onResponse(parseFriends(page.getDocument()), 1);
                    } else {
                        RetrofitClient.getApi().friendsPagination(page.getWicket(), pageNumber).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    listener.onResponse(parseFriends(page.getDocument()), pageCount);
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
