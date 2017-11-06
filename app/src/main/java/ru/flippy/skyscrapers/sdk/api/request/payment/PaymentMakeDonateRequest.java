package ru.flippy.skyscrapers.sdk.api.request.payment;

import android.content.Intent;
import android.net.Uri;

import org.jsoup.nodes.Document;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Donate;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.model.Payment;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class PaymentMakeDonateRequest extends BaseRequest {

    public static final int PHONE_NOT_SPECIFIED = 0;
    public static final int DONATE_LIMIT = 1;

    private String type;
    private Donate donate;

    public PaymentMakeDonateRequest(String type, Donate donate) {
        this.type = type;
        this.donate = donate;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().paymentDonatePage(type).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Document document = page.getDocument();
                    if (type.equals(Payment.MOBILE) && type.equals(Payment.QIWI)) {
                        if (FormParser.checkForm(document, "emptyPhoneBlock")) {
                            listener.onError(PHONE_NOT_SPECIFIED);
                        } else {
                            RetrofitClient.getApi().paymentDonate(page.getWicket(), type, donate.getId()).enqueue(new Callback<Page>() {
                                @Override
                                public void onResponse(Call<Page> call, Response<Page> response) {
                                    Page page = response.body();
                                    if (!response.isSuccessful() || page == null) {
                                        listener.onError(UNKNOWN);
                                    } else {
                                        Parser parser = Parser.from(page.getDocument());
                                        if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Превышен лимит")) {
                                            listener.onError(DONATE_LIMIT);
                                        } else if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Счет выставлен")) {
                                            listener.onSuccess();
                                        } else {
                                            listener.onError(UNKNOWN);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Page> call, Throwable t) {
                                    listener.onError(NETWORK);
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
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                listener.onError(NETWORK);
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
