package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.model.Comment;
import ru.flippy.skyscrapers.sdk.api.model.Date;
import ru.flippy.skyscrapers.sdk.api.model.DateTime;
import ru.flippy.skyscrapers.sdk.api.model.Time;
import ru.flippy.skyscrapers.sdk.api.model.Topic;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class ForumTopicRequest {

    private long topicId;
    private int pageNumber;

    public ForumTopicRequest(long topicId, int pageNumber) {
        this.topicId = topicId;
        this.pageNumber = pageNumber;
    }

    public void execute(final RequestListener<Topic> listener) {
        RetrofitClient.getApi().forumTopic(topicId, pageNumber).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Topic topic = new Topic();
                topic.setBan(!document.select("a[class=bl amount cntr][href*=bans]:contains(Вы не можете комментировать этот топик)").isEmpty());
                topic.setClosed(!document.select("div[class=cntr amount m5]:contains(топик закрыт)").isEmpty()
                        && !document.select("span[class=bl amount cntr]:contains(Вы не можете комментировать топик, потому что тема закрыта)").isEmpty());
                Elements commentElements = document.select("ul.msgs>li");
                List<Comment> comments = new ArrayList<>(commentElements.size());
                for (Element commentElement : commentElements) {
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
                listener.onResponse(topic);
            }
        });
    }
}
