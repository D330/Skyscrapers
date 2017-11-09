package ru.flippy.skyscrapers.sdk.api.request.payment;

import android.text.TextUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.model.Payment;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class PaymentWebmoneyInvoice extends BaseRequest {

    public static final int EMPTY_INPUT = 0;
    public static final int INCORRECT_WMID = 1;
    public static final int INCORRECT_WALLET = 2;

    private long wmid;
    private String wallet;
    private int dollars;

    public PaymentWebmoneyInvoice(long wmid, String wallet, int dollars) {
        this.wmid = wmid;
        this.wallet = wallet;
        this.dollars = dollars;
    }

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(wallet)) {
            listener.onError(EMPTY_INPUT);
        } else {
            RetrofitClient.getApi().paymentDonatePage(Payment.WEBMONEY_INVOICE).enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    Page page = response.body();
                    if (!response.isSuccessful() || page == null) {
                        listener.onError(UNKNOWN);
                    } else {
                        HashMap<String, String> postData = FormParser.parse(page.getDocument())
                                .findByAction("xWebmoneyInvoice")
                                .input("wmid", wmid)
                                .input("wallet", wallet)
                                .input("amount", dollars)
                                .build();
                        RetrofitClient.getApi().paymentWebmoneyInvoice(page.getWicket(), postData).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    Parser parser = Parser.from(page.getDocument());
                                    if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "Счет успешно выставлен")) {
                                        listener.onSuccess();
                                    } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "Неверный WMID")) {
                                        listener.onError(INCORRECT_WMID);
                                    } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "Неверный номер кошелька")) {
                                        listener.onError(INCORRECT_WALLET);
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
                }

                @Override
                public void onFailure(Call<Page> call, Throwable t) {
                    listener.onError(NETWORK);
                }
            });
        }
    }
}
