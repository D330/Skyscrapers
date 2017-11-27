package ru.flippy.skyscrapers.sdk.api.request.payment;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.model.Donate;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class PaymentWebMoneyInvoice {

    private long wmid;
    private String wallet;
    private int dollars;

    public PaymentWebMoneyInvoice(long wmid, String wallet, int dollars) {
        this.wmid = wmid;
        this.wallet = wallet;
        this.dollars = dollars;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().paymentDonatePage(Donate.Type.WEBMONEY_INVOICE)
                .error(listener)
                .success(donateDoc -> {
                    HashMap<String, String> postData = FormParser.parse(donateDoc)
                            .findByAction("xWebmoneyInvoice")
                            .input("wmid", wmid)
                            .input("wallet", wallet)
                            .input("amount", dollars)
                            .build();
                    RetrofitClient.getApi().paymentWebmoneyInvoice(donateDoc.wicket(), postData)
                            .error(listener)
                            .success(resultDoc -> {
                                if (resultDoc.hasFeedBack(Feedback.Type.INFO, "Счет успешно выставлен")) {
                                    listener.onSuccess();
                                } else {
                                    listener.onError(Error.UNKNOWN);
                                }
                            });
                });
    }
}
