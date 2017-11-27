package ru.flippy.skyscrapers.sdk.api.request.rating;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.PaginationItem;
import ru.flippy.skyscrapers.sdk.api.model.city.City;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.api.retrofit.SourceCall;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class RatingCityRequest {

    private PaginationItem paginationItem;

    public RatingCityRequest() {

    }

    public RatingCityRequest(PaginationItem paginationItem) {
        this.paginationItem = paginationItem;
    }

    public void execute(PaginationRequestListener<List<City>> listener) {
        SourceCall call;
        if (paginationItem == null) {
            call = RetrofitClient.getApi().ratingCity();
        } else {
            call = RetrofitClient.getApi().ratingCityPagination(paginationItem.getWicket(), paginationItem.getIndex());
        }
        call.error(listener).success(
                ratingDoc -> listener.onResponse(parseRating(ratingDoc), ratingDoc.pagination())
        );
    }

    private List<City> parseRating(Source ratingDoc) {
        List<City> rating = new ArrayList<>();
        for (Element placeElement : ratingDoc.select("table.rtg>tbody>tr")) {
            City city = new City();
            city.setPlace(Integer.parseInt(placeElement.select("td.num>a>span").first().text()));
            city.setId(Utils.getValueAfterLastSlash(placeElement.select("td.usr>a").first().attr("href")));
            city.setName(placeElement.select("td.usr>a>span.name>span.nwr>span.link>span").first().text());
            city.setLevel(Integer.parseInt(placeElement.select("td.usr>a>span.hgh.white").first().text().replaceAll("\\D", "")));
            rating.add(city);
        }
        return rating;
    }
}
