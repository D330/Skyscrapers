package ru.flippy.skyscrapers.sdk.api.request.rating;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.PaginationItem;
import ru.flippy.skyscrapers.sdk.api.model.RatingUser;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.api.retrofit.SourceCall;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class RatingUsersRequest {

    private PaginationItem paginationItem;
    private int type;

    public RatingUsersRequest(int type, PaginationItem paginationItem) {
        this.type = type;
        this.paginationItem = paginationItem;
    }

    public void execute(PaginationRequestListener<List<RatingUser>> listener) {
        SourceCall call;
        if (paginationItem == null) {
            call = RetrofitClient.getApi().ratingUsers(type);
        } else {
            call = RetrofitClient.getApi().ratingUsersPagination(type, paginationItem.getWicket(), paginationItem.getIndex());
        }
        call.error(listener).success(
                ratingDoc -> listener.onResponse(parseRating(ratingDoc), ratingDoc.pagination())
        );
    }

    private List<RatingUser> parseRating(Source ratingDoc) {
        List<RatingUser> rating = new ArrayList<>();
        for (Element placeElement : ratingDoc.select("table.rtg>tbody>tr")) {

            RatingUser place = new RatingUser();

            place.setPlace(Integer.parseInt(placeElement.select("td.num>a>span").first().text()));

            place.setDescription(placeElement.select("td.usr>a>span.hgh.white").first().text());

            User player = new User();
            Element userElement = placeElement.select("td.usr>a").first();
            Element playerImage = userElement.select("span.name>img[src*=/user/]").first();
            player.setSex(playerImage.attr("alt").equals("м") ? User.sex.MAN : User.sex.WOMAN);
            for (String betweenTire : playerImage.attr("src").split("\\.")[0].split("-")) {
                if (betweenTire.endsWith("0")) {
                    player.setLevel(Integer.parseInt(betweenTire));
                }
            }
            player.setId(Utils.getValueAfterLastSlash(userElement.attr("href")));
            player.setNick(userElement.select("span.name>span.user>span>span").first().text());
            if (userElement.select("span.name>span.minor").last().text().equals("*")) {
                player.setOnline(User.online.ONLINE_STAR);
            } else if (playerImage.attr("src").contains("off")) {
                player.setOnline(User.online.OFFLINE);
            } else {
                player.setOnline(User.online.ONLINE);
            }
            place.setPlayer(player);
            rating.add(place);
        }
        return rating;
    }
}
