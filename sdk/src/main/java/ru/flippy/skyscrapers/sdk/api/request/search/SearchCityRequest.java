package ru.flippy.skyscrapers.sdk.api.request.search;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.PaginationItem;
import ru.flippy.skyscrapers.sdk.api.model.city.City;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class SearchCityRequest {

    private String name;
    private PaginationItem paginationItem;

    public SearchCityRequest(String name) {
        this.name = name;
    }

    public SearchCityRequest(PaginationItem paginationItem) {
        this.paginationItem = paginationItem;
    }

    public void execute(final PaginationRequestListener<List<City>> listener) {
        if (paginationItem == null) {
            RetrofitClient.getApi().searchCityPage()
                    .error(listener)
                    .success(searchDoc -> {
                        HashMap<String, String> postData = FormParser.parse(searchDoc)
                                .findByAction("searchForm")
                                .input("name", name)
                                .build();
                        RetrofitClient.getApi().searchCity(searchDoc.wicket(), postData)
                                .error(listener)
                                .success(resultDoc -> listener.onResponse(parseResult(resultDoc), resultDoc.pagination()));
                    });
        } else {
            RetrofitClient.getApi()
                    .searchCityPagination(paginationItem.getWicket(), paginationItem.getIndex())
                    .error(listener)
                    .success(resultDoc -> listener.onResponse(parseResult(resultDoc), resultDoc.pagination()));
        }
    }

    private List<City> parseResult(Source resultDoc) {
        List<City> cities = new ArrayList<>();
        for (Element cityElement : resultDoc.select("div.m5>div>ul>li")) {
            City city = new City();
            city.setId(Utils.getValueAfterLastSlash(cityElement.select("span>span.nwr>a.link").first().attr("href")));
            city.setName(cityElement.select("span>span.nwr>a.link>span").first().text());
            cities.add(city);
        }
        return cities;
    }
}
