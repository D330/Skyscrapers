package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.model.ForumSection;
import ru.flippy.skyscrapers.sdk.api.model.TopicItem;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class ForumTopicsRequest {

    private long sectionId;
    private int page;

    public ForumTopicsRequest(long sectionId, int page) {
        this.sectionId = sectionId;
        this.page = page;
    }

    public void execute(final PaginationRequestListener<ForumSection> listener) {
        RetrofitClient.getApi().forumTopics(sectionId, page)
                .error(listener)
                .success(topicsDoc -> {
                    ForumSection section = new ForumSection();
                    List<TopicItem> topicItems = new ArrayList<>();
                    for (Element topicElement : topicsDoc.select("ul.frm>li")) {
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
                    Elements moderatorElements = topicsDoc.select("div.m5>span>span:has(span.user)");
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
                    listener.onResponse(section, topicsDoc.pagination());
                });
    }
}
