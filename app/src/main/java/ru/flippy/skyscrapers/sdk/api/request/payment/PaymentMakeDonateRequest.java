package ru.flippy.skyscrapers.sdk.api.request.payment;

import android.content.Intent;
import android.net.Uri;

import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;
import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.model.Donate;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
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
        RetrofitClient.getApi().paymentDonatePage(type)
                .error(listener)
                .success(donateDoc -> {
                    if (type.equals(Donate.Type.MOBILE) && type.equals(Donate.Type.QIWI)) {
                        if (donateDoc.hasForm("emptyPhoneBlock")) {
                            listener.onError(Error.NOT_SPECIFIED);
                        } else {
                            RetrofitClient.getApi().paymentDonate(donateDoc.wicket(), type, donate.getId())
                                    .error(listener)
                                    .success(resultDoc -> {
                                        if (resultDoc.hasFeedBack(Feedback.Type.INFO, "Превышен лимит")) {
                                            listener.onError(Error.LIMIT);
                                        } else if (resultDoc.hasFeedBack(Feedback.Type.INFO, "Счет выставлен")) {
                                            listener.onSuccess();
                                        } else {
                                            listener.onError(Error.UNKNOWN);
                                        }
                                    });
                        }
                    } else {
                        int pid = 0;
                        switch (type) {
                            case Donate.Type.YANDEX:
                                pid = Donate.Type.YANDEX_PID;
                                break;
                            case Donate.Type.WEBMONEY:
                                pid = Donate.Type.WEBMONEY_PID;
                                break;
                            case Donate.Type.CARD:
                                pid = Donate.Type.CARD_PID;
                                break;
                        }
                        String externalUrl = buildExternalDonateUrl(pid, SkyscrapersSDK.getUserId(), donate.getDollars());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        browserIntent.setData(Uri.parse(externalUrl));
                        SkyscrapersSDK.context.startActivity(browserIntent);
                        listener.onSuccess();
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
