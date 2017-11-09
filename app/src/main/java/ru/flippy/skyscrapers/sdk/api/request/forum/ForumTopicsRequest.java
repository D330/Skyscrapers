package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.ForumSection;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.model.TopicItem;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class ForumTopicsRequest extends BaseRequest {

    public static final int PAGE_NOT_FOUND = 0;

    private long sectionId;
    private int page;

    public ForumTopicsRequest(long sectionId, int page) {
        this.sectionId = sectionId;
        this.page = page;
    }

    public void execute(final RequestListener<ForumSection> listener) {
        RetrofitClient.getApi().forumTopics(sectionId, page).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Document document = page.getDocument();
                    Parser parser = Parser.from(document);
                    if (parser.checkPageError()) {
                        listener.onError(PAGE_NOT_FOUND);
                    } else {
                        ForumSection section = new ForumSection();
                        Elements topicElements = document.select("ul.frm>li");
                        List<TopicItem> topicItems = new ArrayList<>(topicElements.size());
                        for (Element topicElement : topicElements) {
                            TopicItem topicItem = new TopicItem();
                            topicItem.setId(Utils.getValueAfterLastSlash(topicElement.select("a").first().attr("href")));
                            topicItem.setName(topicElement.select("a").first().text());
                            String topicImage = topicElement.select("a>img").first().attr("src");
                            topicItem.setPinned(topicImage.contains("pin"));
                            topicItem.setRead(topicImage.contains("read"));
                            topicItem.setClosed(topicImage.contains("cl"));
                            topicItems.add(topicItem);
                        }
                        section.setTopics(topicItems);
                        section.setPageCount(parser.getPageCount(Parser.TYPE_NORMAL));
                        Elements moderatorElements = document.select("div.m5>span>span:has(span.user)");
                        List<User> moderators = new ArrayList<>(moderatorElements.size());
                        for (Element moderatorElement : moderatorElements) {
                            User moderator = new User();
                            moderator.setId(Utils.getValueAfterLastSlash(moderatorElement.select("a").first().attr("href")));
                            moderator.setNick(moderatorElement.select("a").first().text());
                            Element moderatorImageElement = moderatorElement.select("img").first();
                            moderator.setSex(moderatorImageElement.attr("alt").equals("м") ? User.sex.MAN : User.sex.WOMAN);
                            String moderatorImage = moderatorImageElement.attr("src");
                            for (String betweenTire : moderatorImage.split("\\.")[0].split("-")) {
                                if (betweenTire.endsWith("0")) {
                                    moderator.setLevel(Integer.parseInt(betweenTire));
                                }
                            }
                            if (moderatorElement.select("span.minor").last().text().equals("*")) {
                                moderator.setOnline(User.online.ONLINE_STAR);
                            } else if (moderatorImage.contains("off")) {
                                moderator.setOnline(User.online.OFFLINE);
                            } else {
                                moderator.setOnline(User.online.ONLINE);
                            }
                            moderators.add(moderator);
                        }
                        section.setModerators(moderators);
                        listener.onResponse(section);
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
