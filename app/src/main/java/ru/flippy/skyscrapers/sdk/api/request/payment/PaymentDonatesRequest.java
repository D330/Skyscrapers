package ru.flippy.skyscrapers.sdk.api.request.payment;

import android.util.Pair;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.model.Donate;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.model.Payment;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;

public class PaymentDonatesRequest extends BaseRequest {

    public static final int PHONE_NOT_SPECIFIED = 0;

    private String type;

    public PaymentDonatesRequest(String type) {
        this.type = type;
    }

    public void execute(final RequestListener<Pair<Integer, List<Donate>>> listener) {
        RetrofitClient.getApi().paymentDonatePage(type).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Document document = page.getDocument();
                    if (FormParser.checkForm(document, "emptyPhoneBlock")) {
                        listener.onError(PHONE_NOT_SPECIFIED);
                    } else {
                        List<Donate> donates = new ArrayList<>();
                        int number = 0;
                        if (type.equals(Payment.TERMINALS) || type.equals(Payment.QIWI_TERMINALS)) {
                            number = Integer.parseInt(document.select("div.m5>div>div.nfl>strong.info").first().text());
                            for (Element donateElement : document.select("div.cntr>div[class=inbl txtal]>div:has(span.rc)")) {
                                Donate donate = new Donate();
                                donate.setDollars(Integer.parseInt(donateElement.select("b").first().text().replaceAll("\\D", "")));
                                donate.setBonus(Integer.parseInt(donateElement.select("span.rc").first().text().replaceAll("\\D", "")));
                                donates.add(donate);
                            }
                        } else {
                            if (type.equals(Payment.MOBILE) || type.equals(Payment.QIWI)) {
                                number = Integer.parseInt(document.select("div.m5>div>div>div.cntr:contains(+7)>span").first().text().replaceAll("\\D", ""));
                            }
                            for (Element donateElement : document.select("div.m5>div>div>div:has(a[href*=choosePanel])")) {
                                Donate donate = new Donate();
                                donate.setId(Long.parseLong(donateElement.select("a[class=btn btng]").first().attr("href").split(":link")[1].split("::I")[0]));
                                donate.setPrice(Integer.parseInt(donateElement.select("span[class=minor nshd]").first().text().replaceAll("\\D", "")));
                                donate.setDollars(Integer.parseInt(donateElement.select("b").first().text().replaceAll("\\D", "")));
                                donate.setBonus(Integer.parseInt(donateElement.select("span.rc").first().text().replaceAll("\\D", "")));
                                donates.add(donate);
                            }
                        }
                        listener.onResponse(new Pair<>(number, donates));
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
