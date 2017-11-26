package ru.flippy.skyscrapers.sdk.api.request.support;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.PaginationItem;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.api.retrofit.SourceCall;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class SupportTicketsRequest {

    private PaginationItem paginationItem;

    public SupportTicketsRequest(PaginationItem paginationItem) {
        this.paginationItem = paginationItem;
    }

    public void execute(final RequestListener<List<Long>> listener) {
        SourceCall call;
        if (paginationItem == null) {
            call = RetrofitClient.getApi().supportTickets();
        } else {
            call = RetrofitClient.getApi()
                    .supportTicketsPagination(paginationItem.getWicket(), paginationItem.getIndex());
        }
        call.error(listener)
                .success(supportDoc -> listener.onResponse(parseTickets(supportDoc)));
    }

    private List<Long> parseTickets(Source supportDoc) {
        List<Long> tickets = new ArrayList<>();
        for (Element ticketElement : supportDoc.select("div>table.rtg>tbody>tr>td.p5.usr>a")) {
            tickets.add(Utils.getValueAfterLastSlash(ticketElement.attr("href")));
        }
        return tickets;
    }
}
