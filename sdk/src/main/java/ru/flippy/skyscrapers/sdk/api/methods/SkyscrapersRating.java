package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.model.PaginationItem;
import ru.flippy.skyscrapers.sdk.api.request.rating.RatingCityRequest;
import ru.flippy.skyscrapers.sdk.api.request.rating.RatingUsersRequest;

public class SkyscrapersRating {

    public RatingUsersRequest users(int type) {
        return new RatingUsersRequest(type);
    }

    public RatingUsersRequest users(int type, PaginationItem paginationItem) {
        return new RatingUsersRequest(type, paginationItem);
    }

    public RatingCityRequest cities() {
        return new RatingCityRequest();
    }

    public RatingCityRequest cities(PaginationItem paginationItem) {
        return new RatingCityRequest(paginationItem);
    }

}
