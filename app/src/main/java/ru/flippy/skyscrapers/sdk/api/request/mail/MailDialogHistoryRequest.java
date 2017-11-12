package ru.flippy.skyscrapers.sdk.api.request.mail;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Message;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.model.time.Date;
import ru.flippy.skyscrapers.sdk.api.model.time.DateTime;
import ru.flippy.skyscrapers.sdk.api.model.time.Time;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class MailDialogHistoryRequest {

    private long dialogId;
    private int pageNumber;

    public MailDialogHistoryRequest(long dialogId, int pageNumber) {
        this.dialogId = dialogId;
        this.pageNumber = pageNumber;
    }

    public void execute(final PaginationRequestListener<List<Message>> listener) {
        RetrofitClient.getApi().mailDialogHistoryPagination(dialogId, pageNumber)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        Element unlockHistoryElement = doc.select("a[class=bl tdn nshd][href*=historyLinkBlock]:contains(История переписки)").first();
                        if (unlockHistoryElement == null) {
                            listener.onResponse(parseMessages(doc), doc.pagination());
                        } else {
                            long action = Long.parseLong(unlockHistoryElement.attr("href").split("action=")[1]);
                            RetrofitClient.getApi().mailUnlockDialogHistory(doc.wicket(), dialogId, pageNumber, action)
                                    .error(listener)
                                    .success(new SourceCallback() {
                                        @Override
                                        public void onResponse(Source doc) {
                                            listener.onResponse(parseMessages(doc), doc.pagination());
                                        }
                                    });
                        }
                    }
                });
    }

    private List<Message> parseMessages(Source doc) {
        List<Message> messages = new ArrayList<>();
        doc.select("div.hr").remove();
        for (Element messageElement : doc.select("div.cntr:contains(История переписки)").first().nextElementSibling().select("div>div:has(span.user)")) {
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
