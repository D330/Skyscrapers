package ru.flippy.skyscrapers.sdk.api.request.mail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Date;
import ru.flippy.skyscrapers.sdk.api.model.DateTime;
import ru.flippy.skyscrapers.sdk.api.model.Message;
import ru.flippy.skyscrapers.sdk.api.model.Time;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class MailDialogHistoryRequest {

    private long dialogId;
    private int pageNumber;

    public MailDialogHistoryRequest(long dialogId, int pageNumber) {
        this.dialogId = dialogId;
        this.pageNumber = pageNumber;
    }

    public void execute(final PaginationRequestListener<List<Message>> listener) {
        RetrofitClient.getApi().mailDialogHistoryPagination(dialogId, pageNumber).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Parser parser = Parser.from(document);
                if (parser.checkPageError()) {
                    listener.onError(Error.NOT_FOUND);
                } else {
                    Element unlockHistoryElement = document.select("a[class=bl tdn nshd][href*=historyLinkBlock]:contains(История переписки)").first();
                    if (unlockHistoryElement == null) {
                        int pageCount = parser.getPageCount(Parser.TYPE_NORMAL);
                        listener.onResponse(parseMessages(document), pageCount);
                    } else {
                        long action = Long.parseLong(unlockHistoryElement.attr("href").split("action=")[1]);
                        RetrofitClient.getApi().mailUnlockDialogHistory(wicket, dialogId, pageNumber, action).setErrorPoint(listener).enqueue(new DocumentCallback() {
                            @Override
                            public void onResponse(Document document, long wicket) {
                                Parser parser = Parser.from(document);
                                int pageCount = parser.getPageCount(Parser.TYPE_NORMAL);
                                listener.onResponse(parseMessages(document), pageCount);
                            }
                        });
                    }
                }
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
