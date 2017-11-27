package ru.flippy.skyscrapers.sdk.api.request.vendor;


import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.Vendor;
import ru.flippy.skyscrapers.sdk.api.model.VendorAuto;
import ru.flippy.skyscrapers.sdk.api.model.VendorBuff;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class VendorRequest {

    public void execute(final RequestListener<Vendor> listener) {
        RetrofitClient.getApi().vendor()
                .error(listener)
                .success(vendorDoc -> listener.onResponse(parseVendor(vendorDoc)));
    }

    private Vendor parseVendor(Source vendorDoc) {
        Vendor vendor = new Vendor();
        List<VendorBuff> buffs = new ArrayList<>();
        List<VendorAuto> autos = new ArrayList<>();
        for (Element buffElement : vendorDoc.select("div>div:has(a[href*=/buff/])>div")) {
            VendorBuff buff = new VendorBuff();
            buff.setId(Utils.getValueAfterLastSlash(buffElement.select("a").first().attr("href")));
            Element titleElement = buffElement.select("div.p5>a.tdu>span>div.buff>span.bl").first();
            buff.setName(titleElement.select("span").first().text());
            buff.setBonus(Integer.parseInt(titleElement.select("span>span.buff").first().text().replaceAll("\\D", "")));
            buff.setDescription(buffElement.select("div.p5>a.tdu>span.small").first().text());
            buff.setTime(0); //FIXME
            buffs.add(buff);
        }
        for (Element autoElement : vendorDoc.select("div>div:has(a[href*=/auto/])>div")) {
            VendorAuto auto = new VendorAuto();
            auto.setId(Utils.getValueAfterLastSlash(autoElement.select("a").first().attr("href")));
            auto.setName(autoElement.select("div>a.tdu.link>span.bl").first().text());
            auto.setDescription(autoElement.select("div>div.small").first().text());
            auto.setBought(Integer.parseInt(autoElement.select("div>span.small>span").first().text()));
            auto.setMax(Integer.parseInt(autoElement.select("div>span.small").first().text().split(" ")[3]));
            autos.add(auto);
        }
        vendor.setBuffs(buffs);
        vendor.setAutos(autos);
        return vendor;
    }
}
