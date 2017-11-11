package ru.flippy.skyscrapers.sdk.api.request.payment;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Payment;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
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
        RetrofitClient.getApi().paymentDonatePage(Payment.WEBMONEY_INVOICE).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                HashMap<String, String> postData = FormParser.parse(document)
                        .findByAction("xWebmoneyInvoice")
                        .input("wmid", wmid)
                        .input("wallet", wallet)
                        .input("amount", dollars)
                        .build();
                RetrofitClient.getApi().paymentWebmoneyInvoice(wicket, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                    @Override
                    public void onResponse(Document document, long wicket) {
                        Parser parser = Parser.from(document);
                        if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Счет успешно выставлен")) {
                            listener.onSuccess();
                        } else {
                            listener.onError(Error.UNKNOWN);
                        }
                    }
                });
            }
        });
    }
}
