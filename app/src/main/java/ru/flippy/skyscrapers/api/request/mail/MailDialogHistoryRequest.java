package ru.flippy.skyscrapers.api.request.mail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.api.Utils;
import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.OnPageRequestListener;
import ru.flippy.skyscrapers.api.model.Date;
import ru.flippy.skyscrapers.api.model.DateTime;
import ru.flippy.skyscrapers.api.model.Message;
import ru.flippy.skyscrapers.api.model.Time;
import ru.flippy.skyscrapers.api.model.User;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class MailDialogHistoryRequest extends BaseRequest {

    public static final int PAGE_NOT_FOUND = 0;

    private long dialogId;
    private int page;

    public MailDialogHistoryRequest(long dialogId, int page) {
        this.dialogId = dialogId;
        this.page = page;
    }

    public void execute(final OnPageRequestListener<List<Message>> listener) {
        DocumentLoader.connect("http://nebo.mobi/mail/read/id/" + dialogId + "/page/" + page).execute(new OnDocumentListener() {
            @Override
            public void onResponse(Document document, long wicket) {
                ExtremeParser parser = new ExtremeParser(document);
                if (parser.checkPageError()) {
                    listener.onError(PAGE_NOT_FOUND);
                } else {
                    Element unlockHistoryElement = document.select("a[class=bl tdn nshd][href*=historyLinkBlock]:contains(История переписки)").first();
                    if (unlockHistoryElement == null) {
                        int pageCount = parser.getPageCount(ExtremeParser.TYPE_NORMAL);
                        listener.onResponse(parseMessages(document), pageCount);
                    } else {
                        long action = Long.parseLong(unlockHistoryElement.attr("href").split("action=")[1]);
                        String actionUrl = "http://nebo.mobi/mail/read/id/" + dialogId + "/page/" + page + "/?wicket:interface=:" + wicket + ":historyLinkBlock:historyLink::ILinkListener::&action=" + action;
                        DocumentLoader.connect(actionUrl).execute(new OnDocumentListener() {
                            @Override
                            public void onResponse(Document document, long wicket) {
                                ExtremeParser parser = new ExtremeParser(document);
                                if (parser.checkPageError()) {
                                    listener.onError(PAGE_NOT_FOUND);
                                } else {
                                    int pageCount = parser.getPageCount(ExtremeParser.TYPE_NORMAL);
                                    listener.onResponse(parseMessages(document), pageCount);
                                }
                            }

                            @Override
                            public void onError() {
                                listener.onError(NETWORK);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError() {
                listener.onError(NETWORK);
            }
        });
    }

    private List<Message> parseMessages(Document document) {
        List<Message> messages = new ArrayList<>();
        document.select("div.hr").remove();
        for (Element messageElement : document.select("div.cntr:contains(История переписки)").first().nextElementSibling().select("div>div:has(span.user)")) {
            Message message = new Message();
            User author = new User();
            Element userElement = messageElement.select("span.tdn>span.user>a").first();
            author.setId(Utils.getValueAfterLastSlash(userElement.attr("href")));
            author.setNick(userElement.select("span").first().text());
            message.setAuthor(author);
            DateTime sendingDateTime = new DateTime();
            String[] dateArr = messageElement.select("span.small.minor.nshd").first().text().split(" ");
            Date sendingDate = new Date();
            sendingDate.setDay(Integer.parseInt(dateArr[0]));
            sendingDate.setMonth(Utils.getMonthIndex(dateArr[1]));
            sendingDateTime.setDate(sendingDate);
            String[] timeArr = dateArr[2].split(":");
            Time sendingTime = new Time();
            sendingTime.setHour(Integer.parseInt(timeArr[0]));
            sendingTime.setMinute(Integer.parseInt(timeArr[1]));
            sendingDateTime.setTime(sendingTime);
            message.setSendingDateTime(sendingDateTime);
            message.setText(messageElement.select("span.white>span>p").first().text());
            message.setOut(messageElement.select("img").first().attr("src").contains("out"));
            messages.add(message);
        }
        return messages;
    }
}
