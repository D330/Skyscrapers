package ru.flippy.skyscrapers.sdk.api.request.payment;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Donate;
import ru.flippy.skyscrapers.sdk.api.model.DonateInfo;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class PaymentDonatesRequest {

    private String type;

    public PaymentDonatesRequest(String type) {
        this.type = type;
    }

    public void execute(final RequestListener<DonateInfo> listener) {
        RetrofitClient.getApi().paymentDonatePage(type)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        if (doc.checkForm("emptyPhoneBlock")) {
                            listener.onError(Error.NOT_SPECIFIED);
                        } else {
                            List<Donate> donates = new ArrayList<>();
                            int number = 0;
                            if (type.equals(Donate.Type.TERMINALS) || type.equals(Donate.Type.QIWI_TERMINALS)) {
                                number = Integer.parseInt(doc.select("div.m5>div>div.nfl>strong.info").first().text());
                                for (Element donateElement : doc.select("div.cntr>div[class=inbl txtal]>div:has(span.rc)")) {
                                    Donate donate = new Donate();
                                    donate.setDollars(Integer.parseInt(donateElement.select("b").first().text().replaceAll("\\D", "")));
                                    donate.setBonus(Integer.parseInt(donateElement.select("span.rc").first().text().replaceAll("\\D", "")));
                                    donates.add(donate);
                                }
                            } else {
                                if (type.equals(Donate.Type.MOBILE) || type.equals(Donate.Type.QIWI)) {
                                    number = Integer.parseInt(doc.select("div.m5>div>div>div.cntr:contains(+7)>span").first().text().replaceAll("\\D", ""));
                                }
                                for (Element donateElement : doc.select("div.m5>div>div>div:has(a[href*=choosePanel])")) {
                                    Donate donate = new Donate();
                                    donate.setId(Long.parseLong(donateElement.select("a[class=btn btng]").first().attr("href").split(":link")[1].split("::I")[0]));
                                    donate.setPrice(Integer.parseInt(donateElement.select("span[class=minor nshd]").first().text().replaceAll("\\D", "")));
                                    donate.setDollars(Integer.parseInt(donateElement.select("b").first().text().replaceAll("\\D", "")));
                                    donate.setBonus(Integer.parseInt(donateElement.select("span.rc").first().text().replaceAll("\\D", "")));
                                    donates.add(donate);
                                }
                            }
                            DonateInfo<Donate> donateInfo = new DonateInfo<>();
                            donateInfo.setNumber(number);
                            donateInfo.setDonates(donates);
                            listener.onResponse(donateInfo);
                        }
                    }
                });
    }
}
