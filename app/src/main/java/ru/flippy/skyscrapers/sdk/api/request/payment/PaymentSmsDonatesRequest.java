package ru.flippy.skyscrapers.sdk.api.request.payment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Donate;
import ru.flippy.skyscrapers.sdk.api.model.DonateInfo;
import ru.flippy.skyscrapers.sdk.api.model.SmsCountryGroup;
import ru.flippy.skyscrapers.sdk.api.model.SmsDonate;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;

public class PaymentSmsDonatesRequest {

    private String type;

    public PaymentSmsDonatesRequest(String type) {
        this.type = type;
    }

    public void execute(final RequestListener<DonateInfo> listener) {
        RetrofitClient.getApi().paymentDonatePage(type)
                .error(listener)
                .success(donateDoc -> {
                    Element content = donateDoc.select("div.m5>div").first();
                    int sms = Integer.parseInt(content.select("b.info").first().text());
                    Elements countryElements = content.select("b:matches(\\D)");
                    List<SmsCountryGroup> countryGroups = new ArrayList<>(countryElements.size());
                    for (Element countryElement : countryElements) {
                        SmsCountryGroup countryGroup = new SmsCountryGroup();
                        countryGroup.setCountry(countryElement.text());
                        if (type.equals(Donate.Type.SMS_OTHER)) {
                            String[] operators = countryElement.nextSibling().toString().replaceAll("[(): ]", "").split(",");
                            countryGroup.setOperators(Arrays.asList(operators));
                        }
                        StringBuilder countryHtml = new StringBuilder();
                        Element cursor = countryElement;
                        while (true) {
                            Element nextElement = cursor.nextElementSibling();
                            if (cursor.tagName().equals("br") && nextElement.tagName().equals("br")) {
                                break;
                            } else {
                                countryHtml.append(nextElement.toString());
                                cursor = nextElement;
                            }
                        }
                        Source country = (Source) Jsoup.parse(countryHtml.toString());
                        List<SmsDonate> smsDonates = new ArrayList<>();
                        for (Element smsLink : country.select("b:has(a.link)")) {
                            SmsDonate smsDonate = new SmsDonate();
                            smsDonate.setNumber(Integer.parseInt(smsLink.text()));
                            Element dollarsElements = smsLink.nextElementSibling().nextElementSibling();
                            smsDonate.setDollars(Integer.parseInt(dollarsElements.text()));
                            String priceInfo = dollarsElements.nextElementSibling().text();
                            smsDonate.setApproximately(priceInfo.contains("~"));
                            smsDonate.setNds(priceInfo.contains("с НДС"));
                            String[] splitedPriceInfo = priceInfo.replaceAll("[(~)]", "").trim().split(" ");
                            smsDonate.setPrice(splitedPriceInfo[0].replace("\\.", ","));
                            smsDonate.setCurrency(splitedPriceInfo[1]);
                            smsDonates.add(smsDonate);
                        }
                        countryGroup.setDonates(smsDonates);
                        countryGroups.add(countryGroup);
                    }
                    DonateInfo<SmsCountryGroup> donateInfo = new DonateInfo<>();
                    donateInfo.setNumber(sms);
                    donateInfo.setDonates(countryGroups);
                    listener.onResponse(donateInfo);
                });
    }
}
