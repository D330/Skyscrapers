package ru.flippy.skyscrapers.sdk.api.request.support;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SupportTicketCommentRequest {

    private long ticketId;
    private String comment;

    public SupportTicketCommentRequest(long ticketId, String comment) {
        this.ticketId = ticketId;
        this.comment = comment;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().supportTicket(ticketId)
                .error(listener)
                .success(ticketDoc -> {
                    HashMap<String, String> postData = FormParser.parse(ticketDoc)
                            .findByAction("commentForm")
                            .input("text", comment)
                            .build();
                    RetrofitClient.getApi().supportTicketComment(ticketId, ticketDoc.wicket(), postData)
                            .error(listener)
                            .success(resultDoc -> listener.onSuccess());
                });
    }
}
