package ru.flippy.skyscrapers.sdk.api.request.support;


import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Ticket;
import ru.flippy.skyscrapers.sdk.api.model.TicketMessage;
import ru.flippy.skyscrapers.sdk.api.model.time.Date;
import ru.flippy.skyscrapers.sdk.api.model.time.DateTime;
import ru.flippy.skyscrapers.sdk.api.model.time.Time;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class SupportTicketRequest {

    private long ticketId;

    public SupportTicketRequest(long ticketId) {
        this.ticketId = ticketId;
    }

    public void execute(final RequestListener<Ticket> listener) {
        RetrofitClient.getApi().supportTicket(ticketId)
                .error(listener)
                .success(ticketDoc -> listener.onResponse(parseTicket(ticketDoc)));
    }

    private Ticket parseTicket(Source ticketDoc) {
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        Element content = ticketDoc.select("div>div.nfl").first();
        switch (content.select("span.info").first().text()) {
            case "открытый":
                ticket.setStatus(Ticket.Status.OPEN);
                break;
            case "ожидание":
                ticket.setStatus(Ticket.Status.WAIT);
                break;
            case "решен":
                ticket.setStatus(Ticket.Status.SOLVED);
                break;
            case "закрыт":
                ticket.setStatus(Ticket.Status.CLOSED);
                break;
        }
        ticket.setRateAvailable(ticketDoc.hasLink("/rate/0/" + ticketId));
        List<TicketMessage> messages = new ArrayList<>();
        for (Element messageElement : content.parent().select("div:has(span.nick)>div.m5")) {
            TicketMessage message = new TicketMessage();
            message.setOut(!messageElement.select("span.nick>span.usr").isEmpty());
            message.setText(messageElement.select("span").last().text());
            String[] dateTimeArray = messageElement.select("span.minor.small>span").first().text().split(" ");
            DateTime dateTime = new DateTime();
            Date date = new Date();
            date.setDay(Integer.parseInt(dateTimeArray[0]));
            date.setMonth(Utils.getMonthIndex(dateTimeArray[1]));
            dateTime.setDate(date);
            Time time = new Time();
            String[] timeArray = dateTimeArray[2].split(":");
            time.setHour(Integer.parseInt(timeArray[0]));
            time.setMinute(Integer.parseInt(timeArray[1]));
            dateTime.setTime(time);
            message.setSendingDateTime(dateTime);
            messages.add(message);
        }
        ticket.setMessages(messages);
        return ticket;
    }
}
