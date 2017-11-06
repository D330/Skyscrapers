package ru.flippy.skyscrapers.sdk.api.request.payment;

import android.text.TextUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class PaymentChangePhoneRequest extends BaseRequest {

    public static final int EMPTY_INPUT = 0;
    public static final int INCORRECT_FORMAT = 1;

    private String phone;

    public PaymentChangePhoneRequest(String phone) {
        this.phone = phone;
    }

    public void execute(final ActionRequestListener listener) {
        if (TextUtils.isEmpty(phone)) {
            listener.onError(EMPTY_INPUT);
        } else if (phone.length() != 10 || phone.matches("\\D+")) {
            listener.onError(INCORRECT_FORMAT);
        } else {
            RetrofitClient.getApi().paymentDonatePage("xMobilePayment").enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    Page page = response.body();
                    if (!response.isSuccessful() || page == null) {
                        listener.onError(UNKNOWN);
                    } else {
                        RetrofitClient.getApi().paymentChangePhonePage(page.getWicket()).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else {
                                    HashMap<String, String> postData = FormParser.parse(page.getDocument())
                                            .findByAction("emptyPhoneBlock")
                                            .input("phone", phone)
                                            .build();
                                    RetrofitClient.getApi().paymentChangePhone(page.getWicket(), postData).enqueue(new Callback<Page>() {
                                        @Override
                                        public void onResponse(Call<Page> call, Response<Page> response) {
                                            Page page = response.body();
                                            if (!response.isSuccessful() || page == null) {
                                                listener.onError(UNKNOWN);
                                            } else {
                                                listener.onSuccess();
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

                @Override
                public void onFailure(Call<Page> call, Throwable t) {
                    listener.onError(NETWORK);
                }
            });
        }
    }
}
