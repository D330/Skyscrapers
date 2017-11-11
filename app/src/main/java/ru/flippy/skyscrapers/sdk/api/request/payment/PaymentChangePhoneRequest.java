package ru.flippy.skyscrapers.sdk.api.request.payment;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class PaymentChangePhoneRequest {

    private String phone;

    public PaymentChangePhoneRequest(String phone) {
        this.phone = phone;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().paymentDonatePage("xMobilePayment").setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                RetrofitClient.getApi().paymentChangePhonePage(wicket).setErrorPoint(listener).enqueue(new DocumentCallback() {
                    @Override
                    public void onResponse(Document document, long wicket) {
                        HashMap<String, String> postData = FormParser.parse(document)
                                .findByAction("emptyPhoneBlock")
                                .input("phone", phone)
                                .build();
                        RetrofitClient.getApi().paymentChangePhone(wicket, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                            @Override
                            public void onResponse(Document document, long wicket) {
                                listener.onSuccess();
                            }
                        });
                    }
                });
            }
        });
    }
}
