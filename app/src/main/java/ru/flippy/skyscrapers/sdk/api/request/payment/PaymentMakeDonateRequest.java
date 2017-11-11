package ru.flippy.skyscrapers.sdk.api.request.payment;

import android.content.Intent;
import android.net.Uri;

import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;
import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Donate;
import ru.flippy.skyscrapers.sdk.api.model.Payment;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class PaymentMakeDonateRequest {

    private String type;
    private Donate donate;

    public PaymentMakeDonateRequest(String type, Donate donate) {
        this.type = type;
        this.donate = donate;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().paymentDonatePage(type).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                if (type.equals(Payment.MOBILE) && type.equals(Payment.QIWI)) {
                    if (FormParser.checkForm(document, "emptyPhoneBlock")) {
                        listener.onError(Error.NOT_SPECIFIED);
                    } else {
                        RetrofitClient.getApi().paymentDonate(wicket, type, donate.getId()).setErrorPoint(listener).enqueue(new DocumentCallback() {
                            @Override
                            public void onResponse(Document document, long wicket) {
                                Parser parser = Parser.from(document);
                                if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Превышен лимит")) {
                                    listener.onError(Error.LIMIT);
                                } else if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Счет выставлен")) {
                                    listener.onSuccess();
                                } else {
                                    listener.onError(Error.UNKNOWN);
                                }
                            }
                        });
                    }
                } else {
                    int pid = 0;
                    switch (type) {
                        case Payment.YANDEX:
                            pid = Payment.YANDEX_PID;
                            break;
                        case Payment.WEBMONEY:
                            pid = Payment.WEBMONY_PID;
                            break;
                        case Payment.CARD:
                            pid = Payment.CARD_PID;
                            break;
                    }
                    String externalUrl = buildExternalDonateUrl(pid, SkyscrapersSDK.getUserId(), donate.getDollars());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    browserIntent.setData(Uri.parse(externalUrl));
                    SkyscrapersSDK.context.startActivity(browserIntent);
                    listener.onSuccess();
                }
            }
        });
    }

    private String buildExternalDonateUrl(int pid, long userId, int dollars) {
        return "https://secure.xsolla.com/paystation2/?theme=100&project=7247&marketplace=mobile&action=directpayment&local=ru&"
                + "pid=" + pid
                + "&v1=" + userId
                + "&out=" + dollars;
    }
}
