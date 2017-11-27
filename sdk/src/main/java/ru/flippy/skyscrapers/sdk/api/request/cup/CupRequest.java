package ru.flippy.skyscrapers.sdk.api.request.cup;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.profile.Cup;
import ru.flippy.skyscrapers.sdk.api.model.profile.CupStep;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;

public class CupRequest {

    public void execute(final RequestListener<Cup> listener) {
        RetrofitClient.getApi().cup()
                .error(listener)
                .success(cupDoc -> listener.onResponse(parseCup(cupDoc)));
    }

    private Cup parseCup(Source cupDoc) {
        Cup cup = new Cup();
        cup.setName(cupDoc.select("div.m5.cntr>strong.amount>span").first().text());
        List<CupStep> steps = new ArrayList<>();
        for (Element stepElement : cupDoc.select("div.m5.cntr>div.inbl.txtal>div>div>span.small")) {
            CupStep step = new CupStep();
            step.setTitle(stepElement.select("*>span").first().text());
            step.setDescription(stepElement.select("div.cntr.nwr.minor").first().text());
            step.setDone(stepElement.select("img").first().attr("src").endsWith("tick.png"));
            steps.add(step);
        }
        cup.setSteps(steps);
        return cup;
    }
}
