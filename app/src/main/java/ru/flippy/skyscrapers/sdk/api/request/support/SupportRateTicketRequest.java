package ru.flippy.skyscrapers.sdk.api.request.support;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class SupportRateTicketRequest {

    private long ticketId;
    private boolean fast, resolved, understand;

    public SupportRateTicketRequest(long ticketId, boolean fast, boolean resolved, boolean understand) {
        this.ticketId = ticketId;
        this.fast = fast;
        this.resolved = resolved;
        this.understand = understand;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().supportRateTicketPage(ticketId)
                .error(listener)
                .success(rateDoc -> {
                    HashMap<String, String> postData = FormParser.parse(rateDoc)
                            .findByAction("rateForm")
                            .input("rateFast", "radio" + (fast ? 0 : 1))
                            .input("rateResolved", "radio" + (resolved ? 2 : 3))
                            .input("rateUnderstand", "radio" + (understand ? 4 : 5))
                            .build();
                    RetrofitClient.getApi().supportRateTicket(ticketId, rateDoc.wicket(), postData)
                            .error(listener)
                            .success(resultDoc -> {
                                if (resultDoc.hasFeedBack(Feedback.Type.INFO, "Спасибо за оценку")) {
                                    listener.onSuccess();
                                } else {
                                    listener.onError(Error.UNKNOWN);
                                }
                            });
                });
    }
}
