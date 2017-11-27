package ru.flippy.skyscrapers.sdk.api.request.support;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SupportCreateTicketRequest {

    private String type;
    private String ticket;

    public SupportCreateTicketRequest(String type, String ticket) {
        this.type = type;
        this.ticket = ticket;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().supportCreateTicketPage(type)
                .error(listener)
                .success(createTicketDoc -> {
                    HashMap<String, String> postData = FormParser.parse(createTicketDoc)
                            .findByAction("supportForm")
                            .input("text", ticket)
                            .build();
                    RetrofitClient.getApi().supportCreateTicket(type, createTicketDoc.wicket(), postData)
                            .error(listener)
                            .success(resultDoc -> {
                                if (resultDoc.hasFeedBack(Feedback.Type.INFO, "Запрос принят")) {
                                    listener.onSuccess();
                                } else {
                                    listener.onError(Error.UNKNOWN);
                                }
                            });
                });
    }
}
