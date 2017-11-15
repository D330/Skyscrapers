package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.model.Comment;
import ru.flippy.skyscrapers.sdk.api.model.Topic;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.model.time.Date;
import ru.flippy.skyscrapers.sdk.api.model.time.DateTime;
import ru.flippy.skyscrapers.sdk.api.model.time.Time;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class ForumTopicRequest {

    private long topicId;
    private int pageNumber;

    public ForumTopicRequest(long topicId, int pageNumber) {
        this.topicId = topicId;
        this.pageNumber = pageNumber;
    }

    public void execute(final PaginationRequestListener<Topic> listener) {
        RetrofitClient.getApi().forumTopic(topicId, pageNumber)
                .error(listener)
                .success(topicDoc -> {
                    Topic topic = new Topic();
                    topic.setBan(!topicDoc.select("a[class=bl amount cntr][href*=bans]:contains(Вы не можете комментировать этот топик)").isEmpty());
                    topic.setClosed(!topicDoc.select("div[class=cntr amount m5]:contains(топик закрыт)").isEmpty()
                            && !topicDoc.select("span[class=bl amount cntr]:contains(Вы не можете комментировать топик, потому что тема закрыта)").isEmpty());
                    List<Comment> comments = new ArrayList<>();
                    for (Element commentElement : topicDoc.select("ul.msgs>li")) {
                        Comment comment = new Comment();

                        User author = new User();
                        author.setId(Utils.getValueAfterLastSlash(commentElement.select("a[href*=/tower/]").first().attr("href")));
                        author.setNick(commentElement.select("span.link>span.user>span").first().text());
                        Element moderatorImageElement = commentElement.select("img").first();
                        author.setSex(moderatorImageElement.attr("alt").equals("м") ? User.sex.MAN : User.sex.WOMAN);
                        String moderatorImage = moderatorImageElement.attr("src");
                        for (String betweenTire : moderatorImage.split("\\.")[0].split("-")) {
                            if (betweenTire.endsWith("0")) {
                                author.setLevel(Integer.parseInt(betweenTire));
                            }
                        }
                        if (commentElement.select("span.link>span.minor").last().text().equals("*")) {
                            author.setOnline(User.online.ONLINE_STAR);
                        } else if (moderatorImage.contains("off")) {
                            author.setOnline(User.online.OFFLINE);
                        } else {
                            author.setOnline(User.online.ONLINE);
                        }
                        comment.setAuthor(author);

                        comment.setMessage(commentElement.select("div.ovh.m5>span.white>p").first().text());

                        DateTime sendingDateTime = new DateTime();
                        String[] dateArr = commentElement.select("span.small.minor.nshd").first().text().split(" ");
                        Date sendingDate = new Date();
                        sendingDate.setDay(Integer.parseInt(dateArr[0]));
                        sendingDate.setMonth(Utils.getMonthIndex(dateArr[1]));
                        sendingDateTime.setDate(sendingDate);
                        String[] timeArr = dateArr[2].split(":");
                        Time sendingTime = new Time();
                        sendingTime.setHour(Integer.parseInt(timeArr[0]));
                        sendingTime.setMinute(Integer.parseInt(timeArr[1]));
                        sendingDateTime.setTime(sendingTime);
                        comment.setSendingDateTime(sendingDateTime);

                        comments.add(comment);
                    }
                    topic.setComments(comments);
                    listener.onResponse(topic, topicDoc.pagination());
                });
    }
}
