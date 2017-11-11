package ru.flippy.skyscrapers.sdk.api.request.payment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.model.Payment;
import ru.flippy.skyscrapers.sdk.api.model.SmsCountryGroup;
import ru.flippy.skyscrapers.sdk.api.model.SmsDonate;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaymentSmsDonatesRequestListener;

public class PaymentSmsDonatesRequest {

    private String type;

    public PaymentSmsDonatesRequest(String type) {
        this.type = type;
    }

    public void execute(final PaymentSmsDonatesRequestListener listener) {
        RetrofitClient.getApi().paymentDonatePage(type).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Element content = document.select("div.m5>div").first();
                String sms = content.select("b.info").first().text();
                Elements countryElements = content.select("b:matches(\\D)");
                List<SmsCountryGroup> countryGroups = new ArrayList<>(countryElements.size());
                for (Element countryElement : countryElements) {
                    SmsCountryGroup countryGroup = new SmsCountryGroup();
                    countryGroup.setCountry(countryElement.text());
                    if (type.equals(Payment.SMS_OTHER)) {
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
                    Document countryDocument = Jsoup.parse(countryHtml.toString());
                    List<SmsDonate> smsDonates = new ArrayList<>();
                    for (Element smsLink : countryDocument.select("b:has(a.link)")) {
                        SmsDonate smsDonate = new SmsDonate();
                        smsDonate.setNumber(Integer.parseInt(smsLink.text()));
                        Element dollarsElements = smsLink.nextElementSibling().nextElementSibling();
                        smsDonate.setDollars(Integer.parseInt(dollarsElements.text()));
                        String priceInfo = dollarsElements.nextElementSibling().text();
                        smsDonate.setApproximately(priceInfo.contains("~"));
                        smsDonate.setNds(priceInfo.contains("с НДС"));
                        String[] splittedPriceInfo = priceInfo.replaceAll("[(~)]", "").trim().split(" ");
                        smsDonate.setPrice(splittedPriceInfo[0].replace("\\.", ","));
                        smsDonate.setCurrency(splittedPriceInfo[1]);
                        smsDonates.add(smsDonate);
                    }
                    countryGroup.setDonates(smsDonates);
                    countryGroups.add(countryGroup);
                }
                listener.onResponse(sms, countryGroups);
            }
        });
    }
}
