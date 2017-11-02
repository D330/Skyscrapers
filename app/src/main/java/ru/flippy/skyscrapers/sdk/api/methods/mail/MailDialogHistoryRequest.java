package ru.flippy.skyscrapers.sdk.api.methods.mail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Date;
import ru.flippy.skyscrapers.sdk.api.model.DateTime;
import ru.flippy.skyscrapers.sdk.api.model.Message;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.model.Time;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class MailDialogHistoryRequest extends BaseRequest {

    public static final int PAGE_NOT_FOUND = 0;

    private long dialogId;
    private int pageNumber;

    public MailDialogHistoryRequest(long dialogId, int pageNumber) {
        this.dialogId = dialogId;
        this.pageNumber = pageNumber;
    }

    public void execute(final PaginationRequestListener<List<Message>> listener) {
        RetrofitClient.getApi().mailDialogHistoryPagination(dialogId, pageNumber).enqueue(new Callback<Page>() {
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
                        Element unlockHistoryElement = document.select("a[class=bl tdn nshd][href*=historyLinkBlock]:contains(История переписки)").first();
                        if (unlockHistoryElement == null) {
                            int pageCount = parser.getPageCount(Parser.TYPE_NORMAL);
                            listener.onResponse(parseMessages(document), pageCount);
                        } else {
                            long action = Long.parseLong(unlockHistoryElement.attr("href").split("action=")[1]);
                            RetrofitClient.getApi().mailUnlockDialogHistory(page.getWicket(), dialogId, pageNumber, action).enqueue(new Callback<Page>() {
                                @Override
                                public void onResponse(Call<Page> call, Response<Page> response) {
                                    Page page = response.body();
                                    if (!response.isSuccessful() || page == null) {
                                        listener.onError(UNKNOWN);
                                    } else {
                                        Parser parser = Parser.from(page.getDocument());
                                        if (parser.checkPageError()) {
                                            listener.onError(PAGE_NOT_FOUND);
                                        } else {
                                            int pageCount = parser.getPageCount(Parser.TYPE_NORMAL);
                                            listener.onResponse(parseMessages(page.getDocument()), pageCount);
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
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
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
