package ru.flippy.skyscrapers.sdk.api.request.payment;

import android.util.Pair;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.model.Donate;
import ru.flippy.skyscrapers.sdk.api.model.Payment;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;

public class PaymentDonatesRequest {

    private String type;

    public PaymentDonatesRequest(String type) {
        this.type = type;
    }

    public void execute(final RequestListener<Pair<Integer, List<Donate>>> listener) {
        RetrofitClient.getApi().paymentDonatePage(type).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                if (FormParser.checkForm(document, "emptyPhoneBlock")) {
                    listener.onError(Error.NOT_SPECIFIED);
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
        });
    }
}
