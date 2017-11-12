package ru.flippy.skyscrapers.sdk.api.request.payment;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class PaymentChangePhoneRequest {

    private String phone;

    public PaymentChangePhoneRequest(String phone) {
        this.phone = phone;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().paymentDonatePage("xMobilePayment")
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        RetrofitClient.getApi().paymentChangePhonePage(doc.wicket())
                                .error(listener)
                                .success(new SourceCallback() {
                                    @Override
                                    public void onResponse(Source doc) {
                                        HashMap<String, String> postData = FormParser.parse(doc)
                                                .findByAction("emptyPhoneBlock")
                                                .input("phone", phone)
                                                .build();
                                        RetrofitClient.getApi().paymentChangePhone(doc.wicket(), postData)
                                                .error(listener)
                                                .success(new SourceCallback() {
                                                    @Override
                                                    public void onResponse(Source doc) {
                                                        listener.onSuccess();
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }
}
